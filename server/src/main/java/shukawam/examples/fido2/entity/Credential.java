package shukawam.examples.fido2.entity;


import javax.persistence.*;

@Entity(name = "Credential")
@Table(name = "CREDENTIAL")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "getCredentialById", query = "SELECT c FROM Credential c WHERE c.credentialId = :credentialId")
})
public class Credential {
    @Id
    @Column(name = "CREDENTIAL_ID")
    public String credentialId;
    @Column(name = "ATTESTED_CREDENTIAL_DATA")
    public byte[] serializedAttestedCredentialData;
    @Column(name = "ENVELOPE")
    public byte[] serializedEnvelope;
    @Column(name = "TRANSPORTS")
    public String serializedTransports;
    @Column(name = "AUTHENTICATOR_EXTENSIONS")
    public byte[] serializedAuthenticatorExtensions;
    @Column(name = "CLIENT_EXTENSIONS")
    public String serializedClientExtensions;
    @Column(name = "COUNTER")
    public long counter;

    public Credential() {
    }

    public Credential(String credentialId, byte[] serializedAttestedCredentialData, byte[] serializedEnvelope, String serializedTransports, byte[] serializedAuthenticatorExtensions, String serializedClientExtensions, long counter) {
        this.credentialId = credentialId;
        this.serializedAttestedCredentialData = serializedAttestedCredentialData;
        this.serializedEnvelope = serializedEnvelope;
        this.serializedTransports = serializedTransports;
        this.serializedAuthenticatorExtensions = serializedAuthenticatorExtensions;
        this.serializedClientExtensions = serializedClientExtensions;
        this.counter = counter;
    }
}
