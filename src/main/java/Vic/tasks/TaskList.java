package main.java.Vic.tasks;

import java.util.ArrayList;

public class TaskList {
    private static ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void clearTasks() {
        tasks.clear();
    }
    public void addTask(Task task) {
        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public Task removeTask(int index) {
        return tasks.remove(index);
    }

}