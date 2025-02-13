package Vic.parser;

import Vic.actions.AddAction;
import Vic.actions.MarkAction;
import Vic.actions.DeleteAction;
import Vic.actions.ListAction;
import Vic.actions.ByeAction;
import Vic.actions.Action;
import Vic.enums.Command;
import Vic.storage.Storage;
import Vic.tasks.TaskList;
import Vic.ui.Ui;
import Vic.exceptions.UnknownCommandException;
import Vic.exceptions.TaskOutOfBoundsException;

import java.time.Month;
import java.time.Year;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class is responsible for parsing user input, including commands and dates,
 * and converting them into appropriate actions or task objects.
 */
public class Parser {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    public static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(Year.now().getValue(), Month.JANUARY, 1, 23, 59);

    /**
     * Parses a date string into a LocalDateTime object. If the string is empty or null, returns a default date.
     *
     * @param dateString the date string to parse
     * @return the parsed LocalDateTime object, or the default date if input is empty or null
     */
    public static LocalDateTime parseDate(String dateString) {
        return (dateString != null && !dateString.isEmpty()) ? LocalDateTime.parse(dateString, dateTimeFormatter) : DEFAULT_DATE;
    }

    /**
     * Parses the user input command and returns the corresponding Action class object.
     *
     * @param command the command from the user input
     * @param action the full action string from the user
     * @param storage the storage object to manage tasks
     * @param taskList the task list object to manage tasks
     * @return the corresponding Action object
     * @throws UnknownCommandException if the command is unknown or unrecognized
     */
    public static Action parseCommand(Command command, String action, Storage storage, TaskList taskList) throws UnknownCommandException {
        switch (command) {
        case TODO:
        case DEADLINE:
        case EVENT:
            return new AddAction(storage, taskList, action, command);
        case MARK:
            return new MarkAction(storage, taskList, action, command, action.split(" ")[1], true);
        case UNMARK:
            return new MarkAction(storage, taskList, action, command, action.split(" ")[1], false);
        case DELETE:
            return new DeleteAction(storage, taskList, action);
        case LIST:
            return new ListAction(storage, taskList, action);
        case BYE:
            return new ByeAction(storage, taskList, action);
        default:
            throw new UnknownCommandException();
        }
    }

    /**
     * Formats a LocalDateTime object into a string according to the specified dateTime pattern.
     *
     * @param date the LocalDateTime object to format
     * @return the formatted date string
     */
    public static String formatDate(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    /**
     * Formats the default date into a string.
     * The default date is set to 1st January of the current year at 23:59.
     *
     * @return the formatted default date string
     */
    public static String formatDefaultDate() {
        return DEFAULT_DATE.format(dateTimeFormatter);
    }

    /**
     * Parses the user-provided task ID and returns the corresponding index.
     * Validates the task ID by ensuring it is a valid number and within the bounds of the task list.
     *
     * @param option the task ID string provided by the user
     * @param taskList the list of tasks to validate against
     * @return the index of the task in the task list, or -1 if invalid
     * @throws TaskOutOfBoundsException if the task ID is out of bounds in the task list
     */
    public static int parseTaskId(String option, TaskList taskList) throws TaskOutOfBoundsException {
        int taskID = -1;
        try {
            taskID = Integer.parseInt(option);
        }
        catch(NumberFormatException e) {
            Ui.out("No Number detected! Please try again! (⚆_⚆)");
            return -1;
        }
        taskID = taskID - 1;
        if (taskID < 0 || taskID > taskList.getTasks().size() - 1) {
            throw new TaskOutOfBoundsException();
        }
        return taskID;
    }
}
