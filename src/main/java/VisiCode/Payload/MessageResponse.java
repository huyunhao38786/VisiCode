package VisiCode.Payload;

public class MessageResponse {
    private final String message;
    private final String error;

    public static MessageResponse makeMessage(String message) {
        return new MessageResponse(message, null);
    }

    private MessageResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}