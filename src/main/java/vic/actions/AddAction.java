package vic.actions;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import vic.enums.Command;
import vic.exceptions.EmptyContentException;
import vic.parser.Parser;
import vic.storage.Storage;
import vic.tasks.Deadline;
import vic.tasks.Event;
import vic.tasks.Task;
import vic.tasks.TaskList;
import vic.tasks.ToDo;
import vic.ui.Ui;

/**
 * Handles adding tasks to the task list
 */
public class AddAction extends Action {
    private Command command;

    /**
     * Constructor for class
     */
    public AddAction(Storage storage, TaskList taskList, String action, Command command) {
        super(storage, taskList, action);
        this.command = command;
    }

    /**
     * Checks if the input data format is valid.
     *
     * @param splitByStart The starting delimiter used to split the input.
     * @param splitByEnd The ending delimiter used to split the input.
     * @return Extracted and validated content.
     * @throws EmptyContentException If any required content is missing.
     */
    public String formatData(String splitByStart, String splitByEnd) throws EmptyContentException {
        String[] parts = action.split(splitByStart);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EmptyContentException();
        }
        String[] subParts = parts[1].split(splitByEnd);
        if (subParts.length < 1 || subParts[0].trim().isEmpty()) {
            throw new EmptyContentException();
        }
        return subParts[0].trim();
    }

    /**
     * Executes the add task to list action.
     * Parses the input and creates the appropriate task
     *
     * @return false as the method does not need to exit the application.
     * @throws DateTimeParseException If the date format is incorrect.
     */
    @Override
    public boolean execute() {
        String[] responseLst = action.split(" ");
        Task newItem = null;
        String description = "";
        try {
            if (responseLst.length <= 1) {
                throw new EmptyContentException();
            }
            switch (command) {
            case TODO:
                description = action.split(" ", 2)[1];
                if (description.length() < 1) {
                    throw new EmptyContentException();
                }
                newItem = new ToDo(description);
                break;
            case DEADLINE:
                description = formatData(" ", "/by");
                String by = formatData("/by ", "");
                LocalDateTime byDate = Parser.parseDate(by);
                newItem = new Deadline(description, byDate);
                break;
            case EVENT:
                description = formatData(" ", "/from");
                String from = formatData("/from ", "/to");
                String to = formatData("/to ", "");
                LocalDateTime fromDate = Parser.parseDate(from);
                LocalDateTime toDate = Parser.parseDate(to);
                newItem = new Event(description, fromDate, toDate);
                break;
            default:
                break;
            }
            taskList.addTask(newItem);
            storage.saveNewTaskToFile(newItem);
            Ui.showAddMsg(newItem, taskList);
        } catch (EmptyContentException e) {
            Ui.out(e.getMessage());
        } catch (DateTimeParseException e) {
            Ui.out("Date given is in wrong format, Please try again! ಥ‿ಥ");
        }
        return false;
    }
}
