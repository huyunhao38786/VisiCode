package VisiCode.Domain;

import com.google.cloud.datastore.Key;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Descendants;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

import java.util.HashSet;

@Entity
public class User {
    @Id
    private Long id;

    private final String username;

    private final String password;

    private final HashSet<Long> projects;

    public User(String username, String password, HashSet<Long> projects) {
        this.username = username;
        this.password = password;
        this.projects = projects;
    }

    public HashSet<Long> getProjects() { return projects; }

    public boolean removeProject(Long projectId) {
        return projects.remove(projectId);
    }

    public boolean addProject(Long projectId) {
        return projects.add(projectId);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return "ROLE_USER";
    }

    public Long getId() {
        return id;
    }
}
