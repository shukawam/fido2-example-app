package shukawam.examples.fido2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Credentials")
@Table(name = "CREDENTIALS")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "getCredentialById", query = "SELECT c FROM Credentials c WHERE c.credentialId = :credentialId")
})
@Getter
@Setter
public class Credentials {
    @Id
    @Column(name = "CREDENTIAL_ID")
    private String credentialId;
    @Column(name = "ATTESTED_CREDENTIAL_DATA")
    private byte[] serializedAttestedCredentialData;
    @Column(name = "ENVELOPE")
    private byte[] serializedEnvelope;
    @Column(name = "TRANSPORTS")
    private String serializedTransports;
    @Column(name = "AUTHENTICATOR_EXTENSIONS")
    private byte[] serializedAuthenticatorExtensions;
    @Column(name = "CLIENT_EXTENSIONS")
    private String serializedClientExtensions;
    @Column(name = "COUNTER")
    private long counter;

    public Credentials() {
    }

    public Credentials(String credentialId, byte[] serializedAttestedCredentialData, byte[] serializedEnvelope, String serializedTransports, byte[] serializedAuthenticatorExtensions, String serializedClientExtensions, long counter) {
        this.credentialId = credentialId;
        this.serializedAttestedCredentialData = serializedAttestedCredentialData;
        this.serializedEnvelope = serializedEnvelope;
        this.serializedTransports = serializedTransports;
        this.serializedAuthenticatorExtensions = serializedAuthenticatorExtensions;
        this.serializedClientExtensions = serializedClientExtensions;
        this.counter = counter;
    }
}
