package main.java.Vic.tasks;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Format task description as chat query
     *
     * @return formatted description of task
     */
    public String toString() {
        return ("[" + getStatusIcon() + "] " + description);
    }

    /**
     * Get pure task description
     *
     * @return description of task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns status of task
     *
     * @return status of task as X for completed and " " for incomplete
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns status
     *
     * @return status of task
     */
    public boolean getStatus() {
        return isDone;
    }

    /**
     * Mark task as completed
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Mark task as incomplete
     */
    public void markAsUndone() {
        isDone = false;
    }



}
