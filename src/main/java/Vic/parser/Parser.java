package main.java.Vic.parser;

import main.java.Vic.tasks.TaskList;
import main.java.Vic.ui.Ui;
import main.java.Vic.exceptions.*;

import java.time.Month;
import java.time.Year;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
