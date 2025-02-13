package vic.exceptions;

/**
 * This exception provides a generic error message
 */
public class VicException extends Exception {
    public VicException() {
        super("Something went wrong! Please contact the developer for help! (╥╯ᗝ╰╥)");
    }
}
