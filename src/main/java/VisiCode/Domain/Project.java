package VisiCode.Domain;

import VisiCode.Domain.Exceptions.EntityException;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Descendants;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.UUID;

@Entity
public class Project {
    @Id
    private Long id;
    private final String name;

    @Field(name = "editor_id")
    private String editorId;
    @Field(name = "viewer_id")
    private final String viewerId;
    private final HashSet<Long> notes;

    // set here to prevent
    static final int MAX_NOTES = 4096;

    private Project (String name, String editorId, String viewerId, HashSet<Long> notes) {
        this.name = name;
        this.editorId = editorId;
        this.viewerId = viewerId;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEditorId() { return editorId; }
    public void clearEditorId() { editorId = null; }
    public String getViewerId() { return viewerId; }
    public HashSet<Long> getNotes() { return notes; }

    public static Project create(String name) {
        return new Project(
                name,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new HashSet<>());
    }

    public boolean addNote(Note note) throws ProjectSizeException {
        if (notes.add(note.getId())) {
            if (notes.size() > MAX_NOTES) throw new ProjectSizeException();
            return true;
        } else return false;
    }

    public boolean removeNote(Note note) {
        return notes.removeIf(n -> n == note.getId());
    }

    public static class ProjectSizeException extends EntityException {
        public ProjectSizeException() {
            super(String.format("Project size cannot exceed %d notes", MAX_NOTES));
        }
    }
}