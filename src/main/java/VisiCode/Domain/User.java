package LocationSearch.Domain;

import com.google.cloud.datastore.Key;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    private Key id;

    private final String username;

    private final String password;

    private String role = "ROLE_USER";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Key getId() {
        return id;
    }
}
