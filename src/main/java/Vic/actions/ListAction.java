package Vic.actions;

import Vic.storage.Storage;
import Vic.ui.Ui;
import Vic.tasks.TaskList;

/**
 * Handles listing tasks from the task list.
 */
public class ListAction extends Action {

    public ListAction(Storage storage, TaskList taskList, String action) {
        super(storage, taskList, action);
    }

    /**
     * Prints the to-do list.
     *
     * @return false as the method does not need to exit the application.
     */
    @Override
    public boolean execute() {
        storage.loadTasksFromFile(taskList);
        Ui.showTaskList(taskList);
        return false;
    }
}