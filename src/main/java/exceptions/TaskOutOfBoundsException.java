package main.java.exceptions;

public class TaskOutOfBoundsException extends Exception {
    public TaskOutOfBoundsException() {
        super("The task id provided is invalid! (⚆_⚆)");
    }
}