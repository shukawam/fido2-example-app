package shukawam.examples.fido2.dto;

import com.webauthn4j.data.PublicKeyCredentialDescriptor;
import com.webauthn4j.data.UserVerificationRequirement;
import com.webauthn4j.data.extension.client.AuthenticationExtensionClientInput;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs;

import java.io.Serializable;
import java.util.List;

public class AssertionServerOptions implements Serializable {
    public String challenge;
    public Long timeout;
    public String rpId;
    public List<MyPublicKeyCredentialDescriptor> allowCredentials;
    public UserVerificationRequirement userVerification;
    public AuthenticationExtensionsClientInputs<AuthenticationExtensionClientInput> extensions;

    public AssertionServerOptions() {
    }

    public AssertionServerOptions(String challenge, Long timeout, String rpId, List<MyPublicKeyCredentialDescriptor> allowCredentials, UserVerificationRequirement userVerification, AuthenticationExtensionsClientInputs<AuthenticationExtensionClientInput> extensions) {
        this.challenge = challenge;
        this.timeout = timeout;
        this.rpId = rpId;
        this.allowCredentials = allowCredentials;
        this.userVerification = userVerification;
        this.extensions = extensions;
    }
}
