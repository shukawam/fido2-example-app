package shukawam.examples.fido2.dto;

import com.webauthn4j.data.AuthenticatorAttestationResponse;

public class AttestationRequest {
    public String email;
    public String attestationObject;
    public String clientDataJSON;
}
