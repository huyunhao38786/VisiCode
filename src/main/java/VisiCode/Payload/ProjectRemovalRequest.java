package VisiCode.Payload;

import javax.validation.constraints.NotBlank;

public class ProjectRemovalRequest {

    private Long id;

    public static ProjectRemovalRequest forTest(Long id) {
        ProjectRemovalRequest r = new ProjectRemovalRequest();
        r.id = id;
        return r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
