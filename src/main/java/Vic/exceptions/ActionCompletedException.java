package Vic.exceptions;

/**
 * This exception is thrown when an action has already been completed and cannot be performed again.
 */
public class ActionCompletedException extends Exception {
    public ActionCompletedException() {
        super("Action has already been done! (◔_◔)");
    }
}