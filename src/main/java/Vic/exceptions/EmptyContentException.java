package Vic.exceptions;

/**
 * This exception is thrown when the content provided by the user is empty.
 */
public class EmptyContentException extends Exception {
    public EmptyContentException() {
        super("Content cannot be empty! Please Try Again! (╯︿╰)");
    }
}