package shukawam.examples.fido2;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.authenticator.Authenticator;
import com.webauthn4j.authenticator.AuthenticatorImpl;
import com.webauthn4j.converter.AttestedCredentialDataConverter;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.Base64UrlUtil;
import com.webauthn4j.util.UUIDUtil;
import com.webauthn4j.validator.exception.ValidationException;
import shukawam.examples.fido2.dto.AttestationStatementEnvelope;
import shukawam.examples.fido2.entity.Credential;
import shukawam.examples.fido2.entity.Users;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Dependent
public class WebAuthnService {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    private static Logger LOGGER = Logger.getLogger(WebAuthnService.class.getName());

    private static PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity("localhost", "OCHaCafe fido");

    @Transactional(Transactional.TxType.REQUIRED)
    public PublicKeyCredentialCreationOptions createServerOptions(String email) {
        var challenge = new DefaultChallenge();
        LOGGER.info("Create Challenge(Base64Url Encoded)");
        LOGGER.info(Base64UrlUtil.encodeToString(challenge.getValue()));
        // create user
        LOGGER.info("Create UserInfo");
        var id = UUIDUtil.convertUUIDToBytes(UUID.randomUUID());
        var user = new Users(email, id, challenge.getValue());
        entityManager.persist(user);
        // find user
        var users = entityManager
                .createNamedQuery("getUserByEmail", Users.class)
                .setParameter("email", email)
                .getResultList();
        if (users.isEmpty()) {
            throw new RuntimeException("Something bad!");
        }
        PublicKeyCredentialUserEntity userInfo = new PublicKeyCredentialUserEntity(
                users.get(0).id, // id
                users.get(0).email, // username(email)
                users.get(0).email // displayName
        );
        var excludeCredentials = entityManager
                .createNamedQuery("getCredentialById", Credential.class)
                .setParameter("credentialId", users.get(0).credentialId)
                .getResultStream()
                .map(credential -> new PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        Base64UrlUtil.decode(credential.credentialId),
                        Collections.emptySet())
                ).collect(Collectors.toList());
        var pubKeyCredParams = Arrays.asList(
                new PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.ES256),
                new PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.RS256)
        );
        var authenticatorSelectionCriteria = new AuthenticatorSelectionCriteria(
                AuthenticatorAttachment.CROSS_PLATFORM,
                false,
                UserVerificationRequirement.PREFERRED
        );
        var publicKeyCredentialCreationOptions = new PublicKeyCredentialCreationOptions(
                rp,
                userInfo,
                challenge,
                pubKeyCredParams
        );
        return publicKeyCredentialCreationOptions;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public boolean creationFinish(String email, byte[] clientDataJSON, byte[] attestationObject) {
        LOGGER.info("WebAuthnService.creationFinish()");
        var origin = Origin.create("http://localhost:4200");
        var challenges = entityManager.createNamedQuery("getUserByEmail", Users.class)
                .setParameter("email", email)
                .getResultStream()
                .map(users -> new DefaultChallenge(users.challenge))
                .collect(Collectors.toList());
        if (challenges.isEmpty()) {
            throw new BadRequestException("challenge is not found.");
        }
        var challenge = challenges.get(0);
        var serverProperty = new ServerProperty(origin, rp.getId(), challenge, null);
        var registrationRequest = new RegistrationRequest(attestationObject, clientDataJSON);
        var registrationParameters = new RegistrationParameters(serverProperty, true);
        // CBOR parse
        RegistrationData registrationData;
        try {
            registrationData = WebAuthnManager.createNonStrictWebAuthnManager().parse(registrationRequest);
        } catch (DataConversionException e) {
            // do anything.
            e.printStackTrace();
            throw e;
        }
        // attestation validation
        try {
            WebAuthnManager.createNonStrictWebAuthnManager().validate(registrationData, registrationParameters);
        } catch (ValidationException e) {
            // do anything.
            e.printStackTrace();
            throw e;
        }
        // persist authenticator object, which will be used in authentication process.
        var authenticator = new AuthenticatorImpl(
                registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData(),
                registrationData.getAttestationObject().getAttestationStatement(),
                registrationData.getAttestationObject().getAuthenticatorData().getSignCount()
        );
        var credentialId = registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getCredentialId();
        LOGGER.info("credentialId");
        LOGGER.info(Base64UrlUtil.encodeToString(credentialId));
        // store credential to user table
        persistCredential(email, credentialId);
        // store authenticator
        persistAuthenticator(credentialId, authenticator);
        return true;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public PublicKeyCredentialRequestOptions requestOptions(String email) {
        var user = entityManager.find(Users.class, email);
        var challenge = new DefaultChallenge();
        user.challenge = challenge.getValue();
        var allowCredentials = entityManager.createNamedQuery("getCredentialById", Credential.class)
                .setParameter("credentialId", user.credentialId)
                .getResultStream()
                .map(credential -> new PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        Base64UrlUtil.decode(credential.credentialId),
                        new HashSet<AuthenticatorTransport>(Arrays.asList(AuthenticatorTransport.USB,
                                AuthenticatorTransport.BLE,
                                AuthenticatorTransport.INTERNAL,
                                AuthenticatorTransport.NFC)
                        )
                )).collect(Collectors.toList());
        System.out.println(allowCredentials.isEmpty());
        return new PublicKeyCredentialRequestOptions(
                challenge,
                TimeUnit.SECONDS.toMillis(60),
                rp.getId(),
                allowCredentials,
                UserVerificationRequirement.PREFERRED,
                null
        );
    }

    public boolean assertionFinish(byte[] credentialId, byte[] clientDataJSON, byte[] authenticatorData, byte[] signature, byte[] userHandle) {
        LOGGER.info("WebAuthnService.creationFinish()");
        var origin = Origin.create("http://localhost:4200");
        var serverProperty = entityManager.createNamedQuery("getUserByCredentialId", Users.class)
                .setParameter("credentialId", Base64UrlUtil.encodeToString(credentialId))
                .getResultStream()
                .map((users -> new ServerProperty(origin, rp.getId(), new DefaultChallenge(users.challenge), null)))
                .collect(Collectors.toList())
                .get(0);
        var authenticators = entityManager.createNamedQuery("getCredentialById", Credential.class)
                .setParameter("credentialId", Base64UrlUtil.encodeToString(credentialId))
                .getResultList();
        if (authenticators.isEmpty()) {
            throw new NotFoundException();
        }
        var authenticationRequest = new AuthenticationRequest(credentialId, userHandle, authenticatorData, clientDataJSON, signature);
        var authenticationParameter = new AuthenticationParameters(serverProperty, getAuthenticator(credentialId), false);
        try {
            WebAuthnManager.createNonStrictWebAuthnManager().validate(authenticationRequest, authenticationParameter);
        } catch (DataConversionException | ValidationException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    private void persistAuthenticator(byte[] credentialId, Authenticator authenticator) {
        LOGGER.info("WebAuthnService.persistAuthenticator()");
        // serialize authenticator
        var objectConverter = new ObjectConverter();
        var attestedCredentialDataConverter = new AttestedCredentialDataConverter(objectConverter);
        var serializedAttestedCredentialData = attestedCredentialDataConverter.convert(authenticator.getAttestedCredentialData());
        var attestationStatementEnvelope = new AttestationStatementEnvelope(authenticator.getAttestationStatement());
        var serializedEnvelope = objectConverter.getCborConverter().writeValueAsBytes(attestationStatementEnvelope);
        var serializedTransports = objectConverter.getJsonConverter().writeValueAsString(authenticator.getTransports());
        var serializedAuthenticatorExtensions = objectConverter.getCborConverter().writeValueAsBytes(authenticator.getAuthenticatorExtensions());
        var serializedClientExtensions = objectConverter.getJsonConverter().writeValueAsString(authenticator.getClientExtensions());
        entityManager.persist(new Credential(
                Base64UrlUtil.encodeToString(credentialId),
                serializedAttestedCredentialData,
                serializedEnvelope,
                serializedTransports,
                serializedAuthenticatorExtensions,
                serializedClientExtensions,
                authenticator.getCounter()
        ));
    }

    private void persistCredential(String email, byte[] credentialId) {
        LOGGER.info("start: WebAuthnService.persistCredential");
        var user = entityManager.find(Users.class, email);
        user.credentialId = Base64UrlUtil.encodeToString(credentialId);
        LOGGER.info("user.credentialId");
        LOGGER.info(user.credentialId);
//        entityManager.persist(user);
        LOGGER.info("end: WebAuthnService.persistCredential");
    }

    private AuthenticatorImpl getAuthenticator(byte[] credentialId) {
        var objectConverter = new ObjectConverter();
        var attestedCredentialDataConverter = new AttestedCredentialDataConverter(objectConverter);
        return entityManager.createNamedQuery("getCredentialById", Credential.class)
                .setParameter("credentialId", Base64UrlUtil.encodeToString(credentialId))
                .getResultStream()
                .map(credential -> new AuthenticatorImpl(
                        attestedCredentialDataConverter.convert(credential.serializedAttestedCredentialData),
                        null,
                        credential.counter
                )).collect(Collectors.toList())
                .get(0);
    }
}
