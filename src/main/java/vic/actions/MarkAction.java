package vic.actions;

import vic.enums.Command;
import vic.exceptions.ActionCompletedException;
import vic.exceptions.TaskOutOfBoundsException;
import vic.parser.Parser;
import vic.storage.Storage;
import vic.tasks.TaskList;
import vic.ui.Ui;

/**
 * Handles marking tasks as done or undone
 */
public class MarkAction extends Action {
    private Command command;
    private String option;
    private boolean toMarkDone;

    /**
     * Constructor for class
     */
    public MarkAction(Storage storage, TaskList taskList, String action, Command command, String option,
                      boolean toMarkDone) {
        super(storage, taskList, action);
        this.command = command;
        this.option = option;
        this.toMarkDone = toMarkDone;
    }

    /**
     * Executes the action of marking a task as done or undone
     *
     * @return false as the method does not need to exit the application.
     * @throws ActionCompletedException If the task is already in the desired state
     */
    @Override
    public boolean execute() {
        try {
            int taskID = Parser.parseTaskId(option, taskList);
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
            return false;
        } catch (TaskOutOfBoundsException e) {
            return false;
        }
        return false;
    }
}
