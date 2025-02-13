package vic.exceptions;

/**
 * This exception is thrown when there is an issue with the content of a file.
 */
public class FileContentCorruptedException extends Exception {
    public FileContentCorruptedException() {
        super("Error with file content! Please contact developers if this issue persist! (╯︿╰)");
    }
}
