package vic.response;

/**
 * Represents an error message response from the chatbot.
 */
public class ErrorResponse extends Response {

    /**
     * Constructs an ErrorResponse object.
     *
     * @param message The error message content.
     */
    public ErrorResponse(String message) {
        super(message, true);
    }
}
