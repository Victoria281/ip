package vic.tasks;

/**
 * Represents a To-Do task.
 */
public class ToDo extends Task {

    /**
     * Constructor for class
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the To Do task
     *
     * @return A string representation of the To Do task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
