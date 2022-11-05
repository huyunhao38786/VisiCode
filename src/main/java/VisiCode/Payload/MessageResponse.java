package VisiCode.Payload;

public class MessageResponse {
    private String message;
    private String error;

    public static MessageResponse makeMessage(String message) {
        return new MessageResponse(message, null);
    }

    public static MessageResponse makeError(String error) {
        return new MessageResponse(null, error);
    }

    private MessageResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() { return error; }
}