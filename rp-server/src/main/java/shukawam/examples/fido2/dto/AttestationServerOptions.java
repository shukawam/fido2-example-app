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
    public List<MyPublicKeyCredentialParameters> pubKeyCredParams;
    public Long timeout;
    public List<MyPublicKeyCredentialDescriptor> excludeCredentials;
    public MyAuthenticatorSelectionCriteria authenticatorSelection;
    public String attestation;
    public AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput> extensions;

    @JsonCreator
    public AttestationServerOptions(@JsonProperty("rp") PublicKeyCredentialRpEntity rp, @JsonProperty("user") MyPublicKeyCredentialUserEntity user, @JsonProperty("challenge") String challenge, @JsonProperty("pubKeyCredParams") List<MyPublicKeyCredentialParameters> pubKeyCredParams, @JsonProperty("timeout") Long timeout, @JsonProperty("excludeCredentials") List<MyPublicKeyCredentialDescriptor> excludeCredentials, @JsonProperty("authenticatorSelection") MyAuthenticatorSelectionCriteria authenticatorSelection, @JsonProperty("attestation") String attestation, @JsonProperty("extensions") AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput> extensions) {
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
