package LocationSearch.Domain;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Entity
public class HelloData {

    @Id
    private Long id;

    private String message;
    public HelloData(String message) { this.message = message; }

    public String getMessage() {
        return message;
    }
}
