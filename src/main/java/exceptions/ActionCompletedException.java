package main.java.exceptions;

public class ActionCompletedException extends Exception {
    public ActionCompletedException() {
        super("Action has already been done! (◔_◔)");
    }
}