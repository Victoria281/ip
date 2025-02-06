package main.java;

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

public class Parser {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(Year.now().getValue(), Month.JANUARY, 1, 23, 59);

    public static LocalDateTime parseDate(String dateString) {
        return (dateString != null && !dateString.isEmpty()) ? LocalDateTime.parse(dateString, dateTimeFormatter) : DEFAULT_DATE;
    }

    public static String formatDate(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public static String formatDefaultDate() {
        return DEFAULT_DATE.format(dateTimeFormatter);
    }


    /**
     * parse given task number as integer
     *
     * @param option task id provided by user
     * @return task id or -1 for error
     */
    public static int parseTaskId(String option, TaskList taskList) {
        int taskID = -1;
        try {
            taskID = Integer.parseInt(option);
        }
        catch(NumberFormatException e) {
            Ui.out("No Number detected! Please try again! (⚆_⚆)");
            return -1;
        }
        taskID = taskID - 1;
        try {
            if (taskID < 0 || taskID > taskList.getTasks().size() - 1) {
                throw new TaskOutOfBoundsException();
            }
            return taskID;
        }
        catch(TaskOutOfBoundsException e) {
            Ui.out(e.getMessage());
            return -1;
        }
    }
}
