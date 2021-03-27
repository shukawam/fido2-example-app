package shukawam.examples.fido2.dto;

import com.webauthn4j.data.AuthenticatorAttachment;
import com.webauthn4j.data.ResidentKeyRequirement;
import com.webauthn4j.data.UserVerificationRequirement;

public class MyAuthenticatorSelectionCriteria {
    public String authenticatorAttachment;
    public boolean requireResidentKey;
    public ResidentKeyRequirement residentKey;
    public String userVerification;

    public MyAuthenticatorSelectionCriteria() {
    }

    public MyAuthenticatorSelectionCriteria(String authenticatorAttachment, boolean requireResidentKey, String userVerification) {
        this.authenticatorAttachment = authenticatorAttachment;
        this.requireResidentKey = requireResidentKey;
        this.userVerification = userVerification;
    }

    public MyAuthenticatorSelectionCriteria(String authenticatorAttachment, ResidentKeyRequirement residentKey, String userVerification) {
        this.authenticatorAttachment = authenticatorAttachment;
        this.residentKey = residentKey;
        this.userVerification = userVerification;
    }
}
