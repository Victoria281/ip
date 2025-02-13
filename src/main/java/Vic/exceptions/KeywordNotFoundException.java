package Vic.exceptions;

/**
 * This exception is thrown when no matching keyword is found in the task list.
 */
public class KeywordNotFound extends Exception {
    public KeywordNotFound() {
        super("Not Found! Please try again! (◔_◔)");
    }
}