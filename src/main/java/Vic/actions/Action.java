package Vic.actions;

import Vic.storage.Storage;
import Vic.tasks.TaskList;

/**
 * Abstract class for the Bot actions
 */
public abstract class Action {
    protected Storage storage;
    protected TaskList taskList;
    protected String action;

    public Action(Storage storage, TaskList taskList, String action) {
        this.storage = storage;
        this.taskList = taskList;
        this.action = action;
    }

    /**
     * Executes the specific bot action
     *
     * @return status for exit
     */
    public abstract boolean execute();
}