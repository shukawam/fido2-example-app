package shukawam.examples.fido2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Users")
@Table(name = "USERS")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "getUserById", query = "SELECT u FROM Users u WHERE u.id = :id"),
        @NamedQuery(name = "getUserByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
        @NamedQuery(name = "getUserByCredentialId", query = "SELECT u FROM Users u WHERE u.credentialId = :credentialId")
})
@Setter
@Getter
public class Users {
    @Id
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ID")
    private byte[] id;
    @Column(name = "CHALLENGE")
    private byte[] challenge;
    @Column(name = "CREDENTIAL_ID")
    private String credentialId;

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
