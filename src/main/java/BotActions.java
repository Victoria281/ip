package main.java;
import main.java.enums.Command;
import main.java.exceptions.*;
import main.java.tasks.Deadline;
import main.java.tasks.Event;
import main.java.tasks.Task;
import main.java.tasks.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class BotActions {

    /**
     * decipher action to take and execute it
     *
     * @param action user requested action from the bot
     */
    static void execute(Storage storage, TaskList taskList, Command command, String action) {
        try {
            String[] responseLst = action.split(" ");
            switch (command) {
                case MARK:
                    findTaskToCheck(command, responseLst[1], taskList, storage, true);
                    break;
                case UNMARK:
                    findTaskToCheck(command, responseLst[1], taskList, storage, false);
                    break;
                case LIST:
                    printToDoList(storage, taskList);
                    break;
                case DELETE:
                    deleteListItem(action, taskList, storage);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    addListItem(command, action, taskList, storage);
                    break;
                default:
                    throw new UnknownCommandException();
            }
        } catch (UnknownCommandException e) {
            Ui.out(e.getMessage());
        }
    }


    /**
     * add item to toDoList
     *
     * @param command item response to verify and add
     */
    static void addListItem(Command command, String action, TaskList taskList, Storage storage) {
        String[] responseLst = action.split(" ");
        Task newItem = null;
        String description = "";
        try {
            if (responseLst.length <= 1) throw new EmptyContentException();
            switch (command) {
                case TODO:
                    description = action.split(" ", 2)[1];
                    if (description.length() < 1) throw new EmptyContentException();

                    newItem = new ToDo(description);
                    break;
                case DEADLINE:
                    description = action.split(" ", 2)[1].split("/")[0];
                    if (description.length() < 1) throw new EmptyContentException();

                    String by = action.split("/by ").length > 1 ? action.split("/by ")[1] : "";
                    if (by.length() < 1) throw new EmptyContentException();

                    LocalDateTime byDate = Parser.parseDate(by);
                    newItem = new Deadline(description, byDate);
                    break;
                case EVENT:
                    description = action.split(" ", 2)[1].split("/")[0];
                    if (description.length() < 1) throw new EmptyContentException();

                    String[] part2 = action.split("/from", 2);
                    if (part2.length < 2) throw new EmptyContentException();

                    String[] fromSplit = part2[1].split("/to", 2);
                    String from = fromSplit[0].trim();
                    if (from.isEmpty()) throw new EmptyContentException();

                    String to = fromSplit.length > 1 ? fromSplit[1].trim() : "";
                    if (to.isEmpty()) throw new EmptyContentException();

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
    }

    /**
     * prints the toDoList
     */
    static void printToDoList(Storage storage, TaskList tasks) {
        storage.loadTasksFromFile(tasks);
        Ui.showTaskList(tasks);
    }


    /**
     * finds the corresponding task and mark as done or undone
     *
     * @param option task id provided by user
     * @param toMarkDone boolean action to mark as done or undone
     * @return message of the action completed
     */
    static void findTaskToCheck(Command command, String option, TaskList taskList, Storage storage, boolean toMarkDone) {
        int taskID = Parser.parseTaskId(option, taskList);
        if (taskID == -1) { return; }
        try {
            if (toMarkDone) {
                if (!taskList.getTask(taskID).getStatus()) {
                    taskList.getTask(taskID).markAsDone();
                    storage.saveEditedTaskAtIndex(taskID, taskList.getTask(taskID));
                    Ui.showMarkAndUnmarkMsg(taskID, taskList, toMarkDone);
                } else {
                    throw new ActionCompletedException();
                }
            } else {
                if (taskList.getTask(taskID).getStatus()) {
                    taskList.getTask(taskID).markAsUndone();
                    storage.saveEditedTaskAtIndex(taskID, taskList.getTask(taskID));
                    Ui.showMarkAndUnmarkMsg(taskID, taskList, toMarkDone);
                } else {
                    throw new ActionCompletedException();
                }
            }
        } catch (ActionCompletedException e) {
            Ui.out(e.getMessage());
        }

    }

    /**
     * finds the corresponding task and delete it
     *
     * @param action user command to exceute
     */
    static void deleteListItem(String action, TaskList taskList, Storage storage) {
        String[] responseLst = action.split(" ");
        try {
            if (responseLst.length <= 1) throw new EmptyContentException();
        } catch (EmptyContentException e) {
            Ui.out(e.getMessage());
            return;
        }
        int taskID = Parser.parseTaskId(responseLst[1], taskList);
        if (taskID == -1) { return; }
        Task removedTask = taskList.getTask(taskID);
        taskList.removeTask(taskID);
        storage.deleteTaskAtIndex(taskID, removedTask);
        Ui.showRemoveMsg(taskList.getTasks().size(), removedTask.toString());
    }


}
