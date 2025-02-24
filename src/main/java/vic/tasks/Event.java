package vic.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

import vic.parser.Parser;
import vic.tag.Tag;

/**
 * Represents an Event task.
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;
    protected ArrayList<Tag> tags;

    /**
     * Constructor for class
     */
    public Event(String description, LocalDateTime from, LocalDateTime to, ArrayList<Tag> tags) {
        super(description, tags);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a string representation of the Event task.
     *
     * @return A string representation of the Event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + Parser.formatDate(from)
                + " to: " + Parser.formatDate(to) + ")";
    }

    /**
     * Returns the start time of the Event task formatted as a string.
     *
     * @return The formatted start time of the event.
     */
    public String getFrom() {
        return Parser.formatDate(from);
    }

    /**
     * Returns the end time of the Event task formatted as a string.
     *
     * @return The formatted end time of the event.
     */
    public String getTo() {
        return Parser.formatDate(to);
    }
}
