package VisiCode.Domain.Exceptions;

public class UserException extends VisiCodeException {
    private UserException(String message) {
        super(message);
    }

    public static UserException notOwner(String username) {
        return new UserException(String.format("User %s does not own the requested project", username));
    }

    public static UserException noUser(String username) {
        return new UserException(String.format("User %s does not exist", username));
    }

    public static UserException duplicateUser(String username) {
        return new UserException(String.format("User %s is already registered", username));
    }
}
