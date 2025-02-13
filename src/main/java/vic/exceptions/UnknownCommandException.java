package vic.exceptions;

/**
 * This exception is thrown when an unknown command is encountered.
 */
public class UnknownCommandException extends Exception {
    public UnknownCommandException() {
        super("Sorry, I do not know what it means as I am still learning! (╥╯ᗝ╰╥)");
    }
}
