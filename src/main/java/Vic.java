import main.java.tasks.Deadline;
import main.java.tasks.Event;
import main.java.tasks.Task;
import main.java.tasks.ToDo;

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
     * @param response item response to verify and add
     */
    static void addListItem(String response) {
        String[] responseLst = response.split(" ");
        Task newItem = null;
        String description = "";
        switch (responseLst[0]) {
        case "todo":
            description = response.split("todo ")[1];
            newItem = new ToDo(description);
            break;
        case "deadline":
            description = response.split("deadline ")[1].split("/")[0];
            String by = response.split("/by ")[1];
            newItem = new Deadline(description, by);
            break;
        case "event":
            description = response.split("event ")[1].split("/")[0];
            String[] part2 = response.split("/");
            String from = part2[1].split("from ")[1];
            String to = part2[2].split("to ")[1];
            newItem = new Event(description, from, to);
            break;
        default:
            break;
        }
        toDoList.add(newItem);
        System.out.println(replyFormatter(
                "Got it. I've added this task:\n"
                + "\t\t\t" + newItem.toString()
                + "\n\t Now you have "
                + toDoList.size()
                + " tasks in the list."
        ));
    }

    /**
     * prints the toDoList
     */
    static void printToDoList() {
        System.out.println(line);
        System.out.println("\tHere are the tasks in your list:\n");
        for (int i = 0; i < toDoList.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + toDoList.get(i).toString());
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
                        + toDoList.get(taskID).toString()));
            } else {
                toDoList.get(taskID).markAsDone();
                System.out.println(replyFormatter("Nice! I've marked this task as done: \n\t\t\t"
                        + toDoList.get(taskID).toString()));
            }
        } else {
            if (!toDoList.get(taskID).getStatus()) {
                System.out.println(replyFormatter("Task has not been done yet:  \n\t\t\t"
                        + toDoList.get(taskID).toString()));
            } else {
                toDoList.get(taskID).markAsUndone();
                System.out.println(replyFormatter("OK, I've marked this task as not done yet: \n\t\t\t"
                        + toDoList.get(taskID).toString()));
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
                addListItem(response);
                break;
            }
            response = input.nextLine();
        }
        System.out.println(outro);


    }
}
