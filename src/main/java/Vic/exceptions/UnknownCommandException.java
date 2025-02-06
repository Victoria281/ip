package main.java.Vic.exceptions;

public class UnknownCommandException extends Exception {
    public UnknownCommandException() {
        super("Sorry, I do not know what it means as I am still learning! (╥╯ᗝ╰╥)");
    }
}