package vic.actions;

import vic.exceptions.ActionCompletedException;
import vic.exceptions.EmptyContentException;
import vic.exceptions.TaskOutOfBoundsException;
import vic.parser.Parser;
import vic.response.ErrorResponse;
import vic.response.MessageResponse;
import vic.response.Response;
import vic.storage.Storage;
import vic.tag.Tag;
import vic.tasks.Task;
import vic.tasks.TaskList;

/**
 * Handles adding tags to tasks
 */
public class TagAction extends Action {
    private boolean isToAdd;

    /**
     * Constructor for class
     */
    public TagAction(Storage storage, TaskList taskList, String action, boolean isToAdd) {
        super(storage, taskList, action);
        this.isToAdd = isToAdd;
    }

    private String parseAndValidateTag() throws EmptyContentException {
        String[] actionParts = action.split(" ");
        if (actionParts.length < 3 || actionParts[2].trim().isEmpty()) {
            throw new EmptyContentException();
        }
        return actionParts[2];
    }

    /**
     * Adds a tag to a task.
     *
     * @param taskID The ID of the task to add the tag to.
     * @param tag The tag to add.
     * @return A message indicating the result of the operation.
     * @throws ActionCompletedException If the tag already exists on the task.
     */
    private String addTagToTask(int taskID, String tag) throws ActionCompletedException {
        Task task = taskList.getTask(taskID);
        if (task.hasTag(tag)) {
            throw new ActionCompletedException();
        }
        Tag tagObj = new Tag(tag);
        task.addTag(tagObj);
        return "Tag added to task: " + tag;
    }

    /**
     * Removes a tag from a task.
     *
     * @param taskID The ID of the task to remove the tag from.
     * @param tag The tag to remove.
     * @return A message indicating the result of the operation.
     * @throws ActionCompletedException If the tag does not exist on the task.
     */
    private String removeTagFromTask(int taskID, String tag) throws ActionCompletedException {
        Task task = taskList.getTask(taskID);
        if (!task.hasTag(tag)) {
            throw new ActionCompletedException();
        }
        Tag tagObj = new Tag(tag);
        task.removeTag(tagObj);
        return "Tag removed from task: " + tag;
    }

    @Override
    public Response execute() {
        String response = "";
        try {
            int taskID = Parser.parseTaskId(action.split(" ")[1], taskList);
            String tag = parseAndValidateTag();

            if (isToAdd) {
                response = addTagToTask(taskID, tag);
            } else {
                response = removeTagFromTask(taskID, tag);
            }

            storage.saveEditedTaskAtIndex(taskID, taskList.getTask(taskID));
            return new MessageResponse(response);

        } catch (ActionCompletedException e) {
            return new ErrorResponse(e.getMessage());
        } catch (TaskOutOfBoundsException e) {
            return new ErrorResponse(e.getMessage());
        } catch (EmptyContentException e) {
            return new ErrorResponse(e.getMessage());
        }
    }

}
