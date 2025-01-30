import main.java.Task;

import java.util.Scanner;
import java.util.ArrayList;

public class Vic {
    private static final String line = "\t ____________________________________________________________\n";
    private static final String name = "Vic";
    private static final String intro = "\t Hello! I'm "
            + name
            +"\n"
            + "\t What can I do for you?\n"
            + line;
    private static final String outro = line + "\t Bye. Hope to see you again soon!\n" + line;
    private static final ArrayList<Task> toDoList = new ArrayList<Task>();

    /**
     * Returns reply message
     *
     * @param msg reply from bot
     * @return formatted message with lines
     */
    static String replyFormatter(String msg) {
        return line + "\t " + msg + "\n" + line;
    }

    /**
     * add item to toDoList
     *
     * @param item item to add
     */
    static void addToDoItem(String item) {
        Task newItem = new Task(item);
        toDoList.add(newItem);
        System.out.println(replyFormatter("added: " + item));
    }

    /**
     * prints the toDoList
     */
    static void printToDoList() {
        System.out.println(line);
        for (int i = 0; i < toDoList.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + toDoList.get(i).getTaskDescription());
        }
        System.out.println(line);
    }

    /**
     * finds the corresponding task and mark as done or undone
     *
     * @param option task id provided by user
     * @param toMarkDone boolean action to mark as done or undone
     * @return message of the action compelted
     */
    static void findTaskToCheck(String option, boolean toMarkDone) {
        int taskID = -1;
        try {
            taskID = Integer.parseInt(option);
        }
        catch(NumberFormatException e) {
            System.out.println(replyFormatter("No Number detected! Please try again!"));
            return;
        }
        taskID = taskID - 1;
        if (taskID < 0 || taskID > toDoList.size() - 1) {
            System.out.println(replyFormatter("No task found! Please try again!"));
            return;
        }

        if (toMarkDone) {
            if (toDoList.get(taskID).getStatus()) {
                System.out.println(replyFormatter("Task has already been completed:  \n\t\t\t"
                        + toDoList.get(taskID).getTaskDescription()));
            } else {
                toDoList.get(taskID).markAsDone();
                System.out.println(replyFormatter("Nice! I've marked this task as done: \n\t\t\t"
                        + toDoList.get(taskID).getTaskDescription()));
            }
        } else {
            if (!toDoList.get(taskID).getStatus()) {
                System.out.println(replyFormatter("Task has not been done yet:  \n\t\t\t"
                        + toDoList.get(taskID).getTaskDescription()));
            } else {
                toDoList.get(taskID).markAsUndone();
                System.out.println(replyFormatter("OK, I've marked this task as not done yet: \n\t\t\t"
                        + toDoList.get(taskID).getTaskDescription()));
            }
        }

    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println(intro);

        String response = input.nextLine();

        while (!response.equals("bye")) {
            String[] responseLst = response.split(" ");
            switch (responseLst[0]) {
                case "mark":
                    findTaskToCheck(responseLst[1], true);
                    break;
                case "unmark":
                    findTaskToCheck(responseLst[1], false);
                    break;
                case "list":
                    printToDoList();
                    break;
                default:
                    addToDoItem(response);
                    break;
            }
            response = input.nextLine();
        }
        System.out.println(outro);


    }
}
