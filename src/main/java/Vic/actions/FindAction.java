package Vic.actions;

import Vic.exceptions.KeywordNotFoundException;
import Vic.exceptions.NoInputException;
import Vic.storage.Storage;
import Vic.tasks.TaskList;
import Vic.tasks.Task;
import Vic.ui.Ui;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Handles finding tasks in the task list based on a search query
 */
public class FindAction extends Action {
    private String query;

    public FindAction(Storage storage, TaskList taskList, String action) {
        super(storage, taskList, action);
        this.query = action.split(" ", 2).length > 1 ? action.split(" ", 2)[1] : "";
    }

    /**
     * Searches for tasks that contain the query string.
     *
     * @return false as the method does not need to exit the application.
     * @throws NoInputException If search query is empty.
     * @throws KeywordNotFoundException If no tasks match the search query.
     */
    @Override
    public boolean execute() {
        try {
            if (query.isEmpty()) {
                throw new NoInputException();
            }

            List<Task> matchedTasks = taskList.getTasks().stream()
                .filter(task -> task.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

            if (matchedTasks.isEmpty()) {
                throw new KeywordNotFoundException(query);
            } else {
                Ui.showFoundMsg(matchedTasks);
            }

        } catch (KeywordNotFoundException e) {
            Ui.out(e.getMessage());
        } catch (NoInputException e) {
            Ui.out(e.getMessage());
        }

        return false;
    }
}