package VisiCode.Payload;

import javax.validation.constraints.NotBlank;

public class ProjectCreationRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}