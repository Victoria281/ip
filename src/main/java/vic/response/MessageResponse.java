package vic.response;

/**
 * Represents a normal message response from the chatbot.
 */
public class MessageResponse extends Response {

    /**
     * Constructs a MessageResponse object.
     *
     * @param message The message content of the normal response.
     */
    public MessageResponse(String message) {
        super(message, false);
    }
}
