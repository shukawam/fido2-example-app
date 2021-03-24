package shukawam.examples.fido2;

import com.webauthn4j.util.Base64UrlUtil;
import shukawam.examples.fido2.dto.*;
import shukawam.examples.fido2.filter.Logged;
import shukawam.examples.fido2.interceptor.Debug;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("webauthn")
@Logged
@Debug
public class WebAuthnResource {
    private final Logger logger;
    private final WebAuthnService webAuthnService;

    @Inject
    public WebAuthnResource(WebAuthnService webAuthnService, Logger logger) {
        this.webAuthnService = webAuthnService;
        this.logger = logger;
    }

    @GET
    @Path("attestation/options/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public AttestationServerOptions attestationOptions(@PathParam("email") String email) {
        var publicKeyCredentialCreationOptions = webAuthnService.createServerOptions(email);
        return new AttestationServerOptions(
                publicKeyCredentialCreationOptions.getRp(),
                new MyPublicKeyCredentialUserEntity(
                        Base64UrlUtil.encodeToString(publicKeyCredentialCreationOptions.getUser().getId()),
                        publicKeyCredentialCreationOptions.getUser().getName(),
                        publicKeyCredentialCreationOptions.getUser().getDisplayName()
                ),
                Base64UrlUtil.encodeToString(publicKeyCredentialCreationOptions.getChallenge().getValue()),
                publicKeyCredentialCreationOptions.getPubKeyCredParams(),
                publicKeyCredentialCreationOptions.getTimeout(),
                publicKeyCredentialCreationOptions.getExcludeCredentials(),
                publicKeyCredentialCreationOptions.getAuthenticatorSelection(),
                publicKeyCredentialCreationOptions.getAttestation(),
                publicKeyCredentialCreationOptions.getExtensions()
        );
    }

    @POST
    @Path("attestation/result")
    @Consumes(MediaType.APPLICATION_JSON)
    public AttestationResult attestationResult(AttestationRequest attestationRequest) {
        var isSuccess = false;
        isSuccess = webAuthnService.creationFinish(attestationRequest.email,
                Base64UrlUtil.decode(attestationRequest.clientDataJSON),
                Base64UrlUtil.decode(attestationRequest.attestationObject)
        );
        return new AttestationResult(isSuccess);
    }

    @GET
    @Path("assertion/options/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public AssertionServerOptions assertionOptions(@PathParam("email") String email) {
        var publicKeyCredentialRequestOptions = webAuthnService.requestServerOptions(email);
        var allowCredential = publicKeyCredentialRequestOptions.getAllowCredentials()
                .stream()
                .map(publicKeyCredentialDescriptor -> new MyPublicKeyCredentialDescriptor(
                        "public-key",
                        Base64UrlUtil.encodeToString(publicKeyCredentialDescriptor.getId()),
                        publicKeyCredentialDescriptor.getTransports())
                ).collect(Collectors.toList());
        return new AssertionServerOptions(
                Base64UrlUtil.encodeToString(publicKeyCredentialRequestOptions.getChallenge().getValue()),
                publicKeyCredentialRequestOptions.getTimeout(),
                publicKeyCredentialRequestOptions.getRpId(),
                allowCredential,
                publicKeyCredentialRequestOptions.getUserVerification(),
                publicKeyCredentialRequestOptions.getExtensions()
        );
    }

    @POST
    @Path("assertion/result")
    @Consumes(MediaType.APPLICATION_JSON)
    public AssertionResult assertionResult(AssertionRequest assertionRequest) {
        var isSuccess = false;
        isSuccess = webAuthnService.assertionFinish(
                Base64UrlUtil.decode(assertionRequest.credentialId),
                Base64UrlUtil.decode(assertionRequest.clientDataJSON),
                Base64UrlUtil.decode(assertionRequest.authenticatorData),
                Base64UrlUtil.decode(assertionRequest.signature),
                Base64UrlUtil.decode(assertionRequest.userHandle));
        return new AssertionResult(isSuccess);
    }

}
