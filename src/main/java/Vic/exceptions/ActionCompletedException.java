package main.java.Vic.exceptions;

public class ActionCompletedException extends Exception {
    public ActionCompletedException() {
        super("Action has already been done! (◔_◔)");
    }
}