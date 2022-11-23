package VisiCode.Payload;

import javax.validation.constraints.NotBlank;

public class ProjectRemovalRequest {

    @NotBlank
    private Long id;

    private ProjectRemovalRequest(Long id) {
        this.id = id;
    }

    public static ProjectRemovalRequest forTest(Long id) {
        return new ProjectRemovalRequest(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
