package main.java;

import main.java.tasks.Task;

import java.util.*;

/**
 * UI class formats user interaction and messages.
 */
class Ui {
    private Scanner scanner;
    private static final String line = "\t ______________________________________________________________________________\n";
    private static final String name = "Vic";
    private static final String intro = "\n\n\t Hello! I'm "
            + name
            +"\n"
            + "\t What can I do for you?\n"
            + line;
    private static final String outro = line + "\t Bye. Hope to see you again soon!\n" + line;

    /**
     * Constructs a Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    public static void out(String message) {
        System.out.println(replyFormatter(message));
    }

    /**
     * Returns reply message
     *
     * @param msg reply from bot
     * @return formatted message with lines
     */
    static String replyFormatter(String msg) {
        return line + "\t " + msg + "\n" + line;
    }

    public static void showIntro() {
        System.out.println(intro);
    }

    public static void showOutro() {
        System.out.println(outro);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public static void showLine() {
        System.err.println(line);
    }

    public static void showTaskList(TaskList taskList) {
        Ui.showLine();
        System.out.println("\tHere are the tasks in your list:\n");
        for (int i = 0; i < taskList.getTasks().size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + taskList.getTasks().get(i).toString());
        }
        Ui.showLine();
    }

    public static void showRemoveMsg(int size, String removedTask) {
        String removeMsg = "Noted. I've removed this task:\n\t\t\t"
                + removedTask;
        System.out.println(replyFormatter(removeMsg
                + "\n\t Now you have "
                + size
                + " tasks in the list."
        ));
    }

    public static void showAddMsg(Task newItem, TaskList taskList) {
        System.out.println(replyFormatter(
                "Got it. I've added this task:\n"
                        + "\t\t\t" + newItem.toString()
                        + "\n\t Now you have "
                        + taskList.getTasks().size()
                        + " tasks in the list."
        ));
    }

    public static void showMarkAndUnmarkMsg(int taskID, TaskList taskList, boolean isDone) {
        if (isDone) {
            System.out.println(replyFormatter("Nice! I've marked this task as done:\n\t\t\t"
                    + taskList.getTask(taskID).toString()));
        } else {
            System.out.println(replyFormatter("OK, I've marked this task as not done yet:\n\t\t\t"
                    + taskList.getTask(taskID).toString()));
        }
    }

}