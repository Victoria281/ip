package vic.actions;

import vic.storage.Storage;
import vic.tasks.TaskList;
import vic.ui.Ui;

/**
 * Handles listing tasks from the task list.
 */
public class ListAction extends Action {

    /**
     * Constructor for class
     */
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
