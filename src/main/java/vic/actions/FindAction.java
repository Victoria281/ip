package vic.actions;

import java.util.List;
import java.util.stream.Collectors;

import vic.exceptions.KeywordNotFoundException;
import vic.exceptions.NoInputException;
import vic.response.ErrorResponse;
import vic.storage.Storage;
import vic.tasks.Task;
import vic.tasks.TaskList;
import vic.ui.Ui;
import vic.response.ErrorResponse;
import vic.response.MessageResponse;
import vic.response.Response;

/**
 * Handles finding tasks in the task list based on a search query
 */
public class FindAction extends Action {
    private String query;

    /**
     * Constructor for class
     */
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
    public Response execute() {
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
                String response = Ui.getFoundMsg(matchedTasks);
                return new MessageResponse(response);
            }

        } catch (KeywordNotFoundException e) {
            return new ErrorResponse(e.getMessage());
        } catch (NoInputException e) {
            return new ErrorResponse(e.getMessage());
        }
    }
}
