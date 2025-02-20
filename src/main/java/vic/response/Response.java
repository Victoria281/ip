package vic.response;

/**
 * Abstract class representing a response from the chatbot.
 * Specific response types (intro, outro, message, error) will extend this class.
 */
public abstract class Response {
    protected final String message;  // The message content of the response
    protected final boolean isError; // Flag to indicate if this is an error message

    /**
     * Constructs a Response object.
     *
     * @param message The message content of the response.
     * @param isError A boolean flag indicating whether the response is an error message (true) or a normal message (false).
     */
    public Response(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    /**
     * Gets the message of the response.
     *
     * @return The message content of the response.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks if this response is an error.
     *
     * @return True if the response is an error message, false if it's a normal message.
     */
    public boolean isError() {
        return isError;
    }
}
