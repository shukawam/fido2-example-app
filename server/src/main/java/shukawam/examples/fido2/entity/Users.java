package shukawam.examples.fido2.entity;

import javax.persistence.*;

@Entity(name = "Users")
@Table(name = "USERS")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "getUserById", query = "SELECT u FROM Users u WHERE u.id = :id"),
        @NamedQuery(name = "getUserByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
        @NamedQuery(name = "getUserByCredentialId", query = "SELECT u FROM Users u WHERE u.credentialId = :credentialId")
})
public class Users {
    @Id
    @Column(name = "EMAIL")
    public String email;
    @Column(name = "ID")
    public byte[] id;
    @Column(name = "CHALLENGE")
    public byte[] challenge;
    @Column(name = "CREDENTIAL_ID")
    public String credentialId;

    public Users() {
    }

    public Users(String email, byte[] id) {
        this.email = email;
        this.id = id;
    }

    public Users(String email, byte[] id, byte[] challenge) {
        this.email = email;
        this.id = id;
        this.challenge = challenge;
    }

    public Users(String email, byte[] id, byte[] challenge, String credentialId) {
        this.email = email;
        this.id = id;
        this.challenge = challenge;
        this.credentialId = credentialId;
    }
}
