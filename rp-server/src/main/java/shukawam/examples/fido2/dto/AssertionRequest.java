package shukawam.examples.fido2.dto;

public class AssertionRequest {
    public String credentialId;
    public String clientDataJSON;
    public String authenticatorData;
    public String signature;
    public String userHandle;

    public AssertionRequest() {
    }

    public AssertionRequest(String credentialId, String clientDataJSON, String authenticatorData, String signature, String userHandle) {
        this.credentialId = credentialId;
        this.clientDataJSON = clientDataJSON;
        this.authenticatorData = authenticatorData;
        this.signature = signature;
        this.userHandle = userHandle;
    }
}
