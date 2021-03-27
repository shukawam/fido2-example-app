package shukawam.examples.fido2.dto;

public class MyPublicKeyCredentialParameters {
    public String type;
    public long alg;

    public MyPublicKeyCredentialParameters() {
    }

    public MyPublicKeyCredentialParameters(String type, long alg) {
        this.type = type;
        this.alg = alg;
    }
}
