package vic.response;

/**
 * Represents an introductory response from the chatbot.
 */
public class IntroResponse extends Response {

    private static final String INTRO = "\n\n\t Hello! I'm "
            + "Vic"
            + "\n"
            + "\t What can I do for you?\n";

    /**
     * Constructs an IntroResponse object.
     */
    public IntroResponse() {
        super(INTRO, false);
    }
}
