package VisiCode.Domain;

import VisiCode.Domain.Exceptions.EntityException;
import com.google.cloud.datastore.Key;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
public class Note {

    public enum ENoteType {
        MARKDOWN,
        IMAGE
    }

    @Id
    private Long id;

    private final ENoteType type;

    @Unindexed
    String data;

    private Note(ENoteType type, String data) {
        this.type = type;
        this.data = data;
    }

    public Long getId() { return id; }

    // https://cloud.google.com/datastore/docs/concepts/limits, minus two longs
    public static final int MAX_BLOB_SIZE = 1048572 - 8 - 8;

    public static Note makeTextNote(String text) throws BlobSizeException {
        // here we assume 1 character is 2 bytes, Java treats Strings messily so this is not the definitive answer
        // the worst case is the note could not be uploaded due to size issue, this will throw a harmless exception in the controller
        if (text.length() * 2 > MAX_BLOB_SIZE) throw new BlobSizeException(text.length() * 2L);
        return new Note(ENoteType.MARKDOWN, text);
    }

    public static Note makeFileNote(MultipartFile file) throws BlobSizeException, IOException {
        if (file.getSize() > MAX_BLOB_SIZE) throw new BlobSizeException(file.getSize());
        return new Note(ENoteType.IMAGE, Base64.encodeBase64String(file.getBytes()));
    }

    public String getType() {
        return type.name();
    }

    public String getData() {
        return data;
    }

    public static class BlobSizeException extends EntityException {
        public BlobSizeException(long size) {
            super(String.format("Note size too large: %s", size));
        }
    }

    @Override
    public boolean equals(@NotNull Object other) {
        if (other instanceof Note) {
            return ((Note) other).id.equals(id);
        } else
            return false;
    }
}
