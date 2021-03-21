package shukawam.examples.fido2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webauthn4j.data.*;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientInput;

import java.util.List;

public class AttestationServerOptions {
    public PublicKeyCredentialRpEntity rp;
    public MyPublicKeyCredentialUserEntity user;
    public String challenge;
    public List<PublicKeyCredentialParameters> pubKeyCredParams;
    public Long timeout;
    public List<PublicKeyCredentialDescriptor> excludeCredentials;
    public AuthenticatorSelectionCriteria authenticatorSelection;
    public AttestationConveyancePreference attestation;
    public AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput> extensions;

    @JsonCreator
    public AttestationServerOptions(@JsonProperty("rp") PublicKeyCredentialRpEntity rp, @JsonProperty("user") MyPublicKeyCredentialUserEntity user, @JsonProperty("challenge") String challenge, @JsonProperty("pubKeyCredParams") List<PublicKeyCredentialParameters> pubKeyCredParams, @JsonProperty("timeout") Long timeout, @JsonProperty("excludeCredentials") List<PublicKeyCredentialDescriptor> excludeCredentials, @JsonProperty("authenticatorSelection") AuthenticatorSelectionCriteria authenticatorSelection, @JsonProperty("attestation") AttestationConveyancePreference attestation, @JsonProperty("extensions") AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput> extensions) {
        this.rp = rp;
        this.user = user;
        this.challenge = challenge;
        this.pubKeyCredParams = pubKeyCredParams;
        this.timeout = timeout;
        this.excludeCredentials = excludeCredentials;
        this.authenticatorSelection = authenticatorSelection;
        this.attestation = attestation;
        this.extensions = extensions;
    }
}
