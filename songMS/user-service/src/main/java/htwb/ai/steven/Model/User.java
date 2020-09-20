package htwb.ai.steven.Model;


import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * User entity as reference for user entries
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * A nickname a user chooses
     */
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "userid", nullable = false)
    private String userId;

    /**
     * A password the user chooses
     */
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * First Name of the user
     */
    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstName;

    /**
     * Surname of the user
     */
    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastName;

    /**
     * Authorization token for the user
     */
    @Column(name = "token")
    private String token;

    public User() {
    }

    public User(String userId, String password, String firstName, String lastName) {
        this.userId = userId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = createToken();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setToken() {
        this.token = createToken();
    }


    public String getToken() {
        return this.token;
    }

    private String createToken() {

        String token = UUID.randomUUID().toString();

        token = token.replaceAll("-", "");

        if (token.length() > 18) {
            token = token.substring(0, 17);
        }

        return token;
    }

}
