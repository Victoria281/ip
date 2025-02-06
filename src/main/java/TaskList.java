package main.java;

import main.java.tasks.Task;

import java.util.ArrayList;

import main.java.enums.FileCodes;
import main.java.exceptions.*;
import main.java.tasks.Deadline;
import main.java.tasks.Event;
import main.java.tasks.Task;
import main.java.tasks.ToDo;
import main.java.enums.Command;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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