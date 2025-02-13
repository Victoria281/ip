package vic.ui;

import java.util.List;
import java.util.Scanner;

import vic.tasks.Task;
import vic.tasks.TaskList;

/**
 * UI class formats user interaction and messages.
 * It is responsible for displaying information and handling user input.
 */
public class Ui {

    private static final String LINE = "\t ___________________________________________________"
            + "___________________________\n";
    private static final String NAME = "vic";
    private static final String INTRO = "\n\n\t Hello! I'm "
            + NAME
            + "\n"
            + "\t What can I do for you?\n"
            + LINE;
    private static final String OUTRO = LINE + "\t Bye. Hope to see you again soon!\n" + LINE;
    private Scanner scanner;

    /**
     * Constructs a Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the provided message to the console, formatted with a border.
     *
     * @param message The message to be printed
     */
    public static void out(String message) {
        System.out.println(replyFormatter(message));
    }

    /**
     * Prints the provided message to the console, without format
     *
     * @param message The message to be printed
     */
    public static void pOut(String message) {
        System.out.println(message);
    }

    /**
     * Returns the reply message formatted with lines.
     *
     * @param msg The reply from the bot
     * @return A formatted message with lines
     */
    public static String replyFormatter(String msg) {
        return LINE + "\t " + msg + "\n" + LINE;
    }

    /**
     * Prints the introduction message when the application starts.
     */
    public static void showIntro() {
        pOut(INTRO);
    }

    /**
     * Prints the outro message when the application ends.
     */
    public static void showOutro() {
        pOut(OUTRO);
    }

    /**
     * Reads the user's input command from the console.
     *
     * @return The command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a line separator to the console.
     */
    public static void showLine() {
        pOut(LINE);
    }

    /**
     * Displays the list of tasks.
     *
     * @param taskList The list of tasks to be displayed
     */
    public static void showTaskList(TaskList taskList) {
        showLine();
        pOut("\tHere are the tasks in your list:\n");
        for (int i = 0; i < taskList.getTasks().size(); i++) {
            pOut("\t " + (i + 1) + ". " + taskList.getTasks().get(i).toString());
        }
        showLine();
    }

    /**
     * Shows a message indicating that a task was removed.
     *
     * @param size         The current number of tasks after removal
     * @param removedTask  The task that was removed
     */
    public static void showRemoveMsg(int size, String removedTask) {
        String removeMsg = "Noted. I've removed this task:\n\t\t\t"
                + removedTask;
        out(removeMsg
                + "\n\t Now you have "
                + size
                + " tasks in the list."
        );
    }

    /**
     * Displays a message indicating that a task was added.
     *
     * @param newItem      The task that was added
     * @param taskList     The current task list
     */
    public static void showAddMsg(Task newItem, TaskList taskList) {
        out(
                "Got it. I've added this task:\n"
                        + "\t\t\t" + newItem.toString()
                        + "\n\t Now you have "
                        + taskList.getTasks().size()
                        + " tasks in the list."
        );
    }

    /**
     * Displays a message indicating whether a task was marked as done or not.
     *
     * @param taskID       The ID of the task to be marked
     * @param taskList     The list of tasks
     * @param isDone       A boolean indicating if the task is marked as done
     */
    public static void showMarkAndUnmarkMsg(int taskID, TaskList taskList, boolean isDone) {
        if (isDone) {
            out("Nice! I've marked this task as done:\n\t\t\t"
                    + taskList.getTask(taskID).toString());
        } else {
            out("OK, I've marked this task as not done yet:\n\t\t\t"
                    + taskList.getTask(taskID).toString());
        }
    }

    /**
     * Displays a message indicating the found tasks based on the search query.
     *
     * @param matchedTasks The list of tasks that matched the search query.
     */
    public static void showFoundMsg(List<Task> matchedTasks) {
        showLine();
        pOut("\tHere are the matching tasks in your list:\t");
        for (int i = 0; i < matchedTasks.size(); i++) {
            pOut("\t " + (i + 1) + "." + matchedTasks.get(i).toString());
        }
        showLine();
    }
}
