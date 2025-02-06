package main.java.Vic.exceptions;

public class EmptyContentException extends Exception {
    public EmptyContentException() {
        super("Content cannot be empty! Please Try Again! (╯︿╰)");
    }
}