package Vic.actions;

import Vic.storage.Storage;
import Vic.tasks.TaskList;

/**
 * Handles marking tasks as done or undone
 */
public class ByeAction extends Action {

    public ByeAction(Storage storage, TaskList taskList, String action) {
        super(storage, taskList, action);
    }

    /**
     * Ends the action
     *
     * @return true to exit the application
     */
    @Override
    public boolean execute() {
        return true;
    }
}