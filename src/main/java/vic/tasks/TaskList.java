package vic.tasks;

import vic.tag.Tag;

import java.util.ArrayList;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;


    /**
     * Constructor for class
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Clears all tasks from the list.
     */
    public void clearTasks() {
        tasks.clear();
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to be added to the list.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the list of tasks.
     *
     * @return The list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the task at the specified index in the list.
     *
     * @param index The index of the task to retrieve.
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index from the list.
     *
     * @param index The index of the task to remove.
     * @return The task that was removed.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves all tasks that have a specific tag.
     *
     * @param tag The tag to filter tasks by.
     * @return A list of tasks that contain the given tag.
     */
    public ArrayList<Task> getTasksByTag(Tag tag) {
        ArrayList<Task> taggedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTags().contains(tag)) {
                taggedTasks.add(task);
            }
        }
        return taggedTasks;
    }

    /**
     * Finds a task with a specific tag and adds it if not already added.
     *
     * @param task The task to modify
     * @param tag  The tag to find and add to the task
     */
    public void findAndAddTagToTask(Task task, Tag tag) {
        if (!task.getTags().contains(tag)) {
            task.addTag(tag);
        }
    }

    /**
     * Removes a specific tag from all tasks in the list.
     *
     * @param tag The tag to remove from tasks.
     */
    public void removeTagFromAllTasks(Tag tag) {
        for (Task task : tasks) {
            task.removeTag(tag);
        }
    }
}
