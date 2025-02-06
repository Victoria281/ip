package main.java.Vic.exceptions;

public class FileContentCorruptedException extends Exception {
    public FileContentCorruptedException() {
        super("Error with file content! Please contact developers if this issue persist! (╯︿╰)");
    }
}
