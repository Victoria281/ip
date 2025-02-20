package vic.response;

/**
 * Represents an introductory response from the chatbot.
 */
public class OutroResponse extends Response {

    private static final String OUTRO = "\t Bye. Hope to see you again soon!\n";


    /**
     * Constructs an IntroResponse object.
     */
    public OutroResponse() {
        super(OUTRO, false);
    }
}
