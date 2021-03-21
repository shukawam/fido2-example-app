package shukawam.examples.fido2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webauthn4j.data.AuthenticatorTransport;
import com.webauthn4j.data.PublicKeyCredentialType;

import java.util.Set;

public class MyPublicKeyCredentialDescriptor {
    public String type;
    public String id;
    public Set<AuthenticatorTransport> transports;

    public MyPublicKeyCredentialDescriptor() {
    }

    @JsonCreator
    public MyPublicKeyCredentialDescriptor(@JsonProperty("type") String type, @JsonProperty("id") String id, @JsonProperty("transports") Set<AuthenticatorTransport> transports) {
        this.type = type;
        this.id = id;
        this.transports = transports;
    }
}
