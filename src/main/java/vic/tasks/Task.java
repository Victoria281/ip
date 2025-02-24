package vic.tasks;

import vic.tag.Tag;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected ArrayList<Tag> tags;

    /**
     * Constructor for class
     */
    public Task(String description, ArrayList<Tag> tags) {
        this.description = description;
        this.isDone = false;
        this.tags = tags;
    }

    /**
     * Returns a string representation of the task, including its completion status.
     *
     * @return A string representing the task's status and description and Tags if any.
     */
    @Override
    public String toString() {
        StringBuilder tagStr = new StringBuilder();
        if (!tags.isEmpty()) {
            tagStr.append(" Tags: [");
            for (int i = 0; i < tags.size(); i++) {
                tagStr.append(tags.get(i).getName());
                if (i < tags.size() - 1) {
                    tagStr.append(", ");
                }
            }
            tagStr.append("]");
        }
        return "[" + getStatusIcon() + "] " + description + tagStr.toString();
    }

    /**
     * Adds a tag to the task.
     *
     * @param tag The tag to add.
     */
    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    /**
     * Removes a tag from the task.
     *
     * @param tag The tag to remove.
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * Returns the list of tags
     *
     * @return An ArrayList of tags associated with the task.
     */
    public ArrayList<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Returns a comma-separated string of the names of tags associated with the task.
     *
     * @return A string containing tag names, separated by commas.
     */
    public String getTagsStr() {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(","));
    }


    /**
     * Check if the task has a specific tag.
     *
     * @param tag The tag to check for
     * @return true if the task has the tag, false otherwise.
     */
    public boolean hasTag(String tag) {
        for (Tag t : tags) {
            if (t.getName().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the description of the task.
     *
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status of the task as an icon.
     * "X" indicates the task is completed, while " " indicates it is incomplete.
     *
     * @return A string representation of the task's status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the completion status of the task.
     *
     * @return True if the task is marked as done, false otherwise.
     */
    public boolean getStatus() {
        return isDone;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as incomplete.
     */
    public void markAsUndone() {
        isDone = false;
    }
}
