package Vic.actions;

import Vic.exceptions.EmptyContentException;
import Vic.exceptions.TaskOutOfBoundsException;
import Vic.parser.Parser;
import Vic.storage.Storage;
import Vic.tasks.Task;
import Vic.tasks.TaskList;
import Vic.ui.Ui;


/**
 * Handles deleting tasks from the task list
 */
public class DeleteAction extends Action {

    public DeleteAction(Storage storage, TaskList taskList, String action) {
        super(storage, taskList, action);
    }

    /**
     * Executes the action of deleting a task from the task list
     *
     * @return false as the method does not need to exit the application.
     * @throws EmptyContentException If the user command does not provide a valid task ID
     */
    @Override
    public boolean execute() {
        String[] responseLst = action.split(" ");
        int taskID;
        try {
            if (responseLst.length <= 1) throw new EmptyContentException();
            taskID = Parser.parseTaskId(responseLst[1], taskList);
        } catch (EmptyContentException e) {
            Ui.out(e.getMessage());
            return false;
        } catch (TaskOutOfBoundsException e) {
            return false;
        }

        Task removedTask = taskList.getTask(taskID);
        taskList.removeTask(taskID);
        storage.deleteTaskAtIndex(taskID, removedTask);
        Ui.showRemoveMsg(taskList.getTasks().size(), removedTask.toString());
        return false;
    }

}