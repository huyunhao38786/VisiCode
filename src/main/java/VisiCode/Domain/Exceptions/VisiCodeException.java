package VisiCode.Domain.Exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class VisiCodeException extends RuntimeException {
    protected VisiCodeException(String message) {
        super(message);
    }

    public VisiCodeExceptionDto toDto(HttpStatus status) {
        return new VisiCodeExceptionDto(this, status);
    }

    public static class VisiCodeExceptionDto {
        private final String message;
        private final String timestamp;
        private final HttpStatus status;

        private VisiCodeExceptionDto(RuntimeException e, HttpStatus status) {
            message = e.getLocalizedMessage();
            timestamp = DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now());
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
