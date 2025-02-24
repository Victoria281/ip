package vic.tasks;

import vic.tag.Tag;

import java.util.ArrayList;

/**
 * Represents a To-Do task.
 */
public class ToDo extends Task {

    protected ArrayList<Tag> tags;

    /**
     * Constructor for class
     */
    public ToDo(String description, ArrayList<Tag> tags) {
        super(description, tags);
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
