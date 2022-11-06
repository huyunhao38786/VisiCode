package VisiCode.Payload;

import VisiCode.Domain.Note;

public class NoteResponse {
    private final long id;
    public NoteResponse(Note note) {
        id = note.getId();
    }
    public NoteResponse(Long id) {
        this.id = id;
    }

    public long getId() { return id; }
}
