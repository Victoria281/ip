package main.java.Vic.tasks;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    public Event(String description, LocalDateTime from, LocalDateTime  to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(outputFormatter) + " to: " + to.format(outputFormatter) + ")";
    }

    /**
     * Get task from date
     *
     * @return task from date
     */
    public String getFrom() {
        return from.format(outputFormatter);
    }

    /**
     * Get task to date
     *
     * @return task to date
     */
    public String getTo() {
        return to.format(outputFormatter);
    }

}
