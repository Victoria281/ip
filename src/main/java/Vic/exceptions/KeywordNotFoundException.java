package Vic.exceptions;

/**
 * This exception is thrown when no matching keyword is found in the task list.
 */
public class KeywordNotFoundException extends Exception {
    public KeywordNotFoundException(String keyword) {
        super("The word " + keyword + " is not found in any tasks! Please try again! (◔_◔)");
    }
}