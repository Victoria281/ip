package vic.exceptions;

/**
 * This exception is thrown when no input is detected
 */
public class NoInputException extends Exception {
    public NoInputException() {
        super("Please specify a task to find! (╥╯ᗝ╰╥)");
    }
}
