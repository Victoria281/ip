package vic.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

import vic.parser.Parser;
import vic.tag.Tag;

/**
 * Represents a Deadline task.
 */
public class Deadline extends Task {

    protected LocalDateTime by;
    protected ArrayList<Tag> tags;

    /**
     * Constructor for class
     */
    public Deadline(String description, LocalDateTime by, ArrayList<Tag> tags) {
        super(description, tags);
        this.by = by;
    }

    /**
     * Returns a string representation of the Deadline task.
     *
     * @return A string representation of the Deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Parser.formatDate(by) + ")";
    }

    /**
     * Returns the due date of the Deadline task formatted as a string.
     *
     * @return The formatted due date of the task.
     */
    public String getBy() {
        return Parser.formatDate(by);
    }
}
