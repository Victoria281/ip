package vic.actions;

import vic.response.OutroResponse;
import vic.storage.Storage;
import vic.tasks.TaskList;

/**
 * Handles marking tasks as done or undone
 */
public class ByeAction extends Action {

    /**
     * Constructor for class
     */
    public ByeAction(Storage storage, TaskList taskList, String action) {
        super(storage, taskList, action);
    }

    /**
     * Ends the action
     *
     * @return true to exit the application
     */
    @Override
    public OutroResponse execute() {
        return new OutroResponse();
    }


    @Override
    public boolean isExit() {
        return false;
    }
}
