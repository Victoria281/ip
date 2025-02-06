package main.java.tasks;

public class Event extends Task {

    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Get task from date
     *
     * @return task from date
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get task to date
     *
     * @return task to date
     */
    public String getTo() {
        return to;
    }

}
