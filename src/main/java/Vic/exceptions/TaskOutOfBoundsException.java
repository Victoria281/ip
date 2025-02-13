package Vic.exceptions;

/**
 * This exception is thrown when an invalid task ID is provided.
 */
public class TaskOutOfBoundsException extends Exception {
    public TaskOutOfBoundsException() {
        super("The task id provided is invalid! (⚆_⚆)");
    }
}