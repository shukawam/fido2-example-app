package shukawam.examples.fido2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webauthn4j.data.PublicKeyCredentialEntity;

public class MyPublicKeyCredentialUserEntity extends PublicKeyCredentialEntity {
    public String id;
    public String displayName;

    @JsonCreator
    public MyPublicKeyCredentialUserEntity(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("displayName") String displayName, @JsonProperty("icon") String icon) {
        super(name, icon);
        this.id = id;
        this.displayName = displayName;
    }

    @JsonCreator
    public MyPublicKeyCredentialUserEntity(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("displayName") String displayName) {
        super(name);
        this.id = id;
        this.displayName = displayName;
    }

    public MyPublicKeyCredentialUserEntity() {
    }
}
