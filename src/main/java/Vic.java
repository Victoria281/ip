package main.java;

import main.java.exceptions.ActionCompletedException;
import main.java.exceptions.EmptyContentException;
import main.java.exceptions.TaskOutOfBoundsException;
import main.java.exceptions.UnknownCommandException;
import main.java.tasks.Deadline;
import main.java.tasks.Event;
import main.java.tasks.Task;
import main.java.tasks.ToDo;

import java.util.Scanner;
import java.util.ArrayList;

public class Vic {
    private static final String line = "\t ______________________________________________________________________________\n";
    private static final String name = "Vic";
    private static final String intro = "\n\n\t Hello! I'm "
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
        try {
            if (responseLst.length <= 1) throw new EmptyContentException();
            switch (responseLst[0]) {
            case "todo":
                description = response.split("todo ")[1];
                if (description.length() < 1) throw new EmptyContentException();

                newItem = new ToDo(description);
                break;
            case "deadline":
                description = response.split("deadline ")[1].split("/")[0];
                if (description.length() < 1) throw new EmptyContentException();

                String by = response.split("/by ").length > 1 ? response.split("/by ")[1] : "";
                if (by.length() < 1) throw new EmptyContentException();

                newItem = new Deadline(description, by);
                break;
            case "event":
                description = response.split("event ")[1].split("/")[0];
                if (description.length() < 1) throw new EmptyContentException();

                String[] part2 = response.split("/");
                String from = part2.length > 1 ? part2[1].split("from ").length > 1 ? part2[1].split("from ")[1] : "" : "";
                if (from.length() < 1) throw new EmptyContentException();

                String to = part2.length > 2 ? part2[2].split("to ").length > 1 ? part2[2].split("to ")[1] : "" : "";
                if (to.length() < 1) throw new EmptyContentException();

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
        } catch (EmptyContentException e) {
            System.out.println(replyFormatter(e.getMessage()));
        }
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
     * parse given task number as integer
     *
     * @param option task id provided by user
     * @return task id or -1 for error
     */
    static int parseTaskId(String option) {
        int taskID = -1;
        try {
            taskID = Integer.parseInt(option);
        }
        catch(NumberFormatException e) {
            System.out.println(replyFormatter("No Number detected! Please try again! (⚆_⚆)"));
            return -1;
        }
        taskID = taskID - 1;
        try {
            if (taskID < 0 || taskID > toDoList.size() - 1) {
                throw new TaskOutOfBoundsException();
            }
            return taskID;
        }
        catch(TaskOutOfBoundsException e) {
            System.out.println(replyFormatter(e.getMessage()));
            return -1;
        }
    }

    /**
     * finds the corresponding task and mark as done or undone
     *
     * @param option task id provided by user
     * @param toMarkDone boolean action to mark as done or undone
     * @return message of the action compelted
     */
    static void findTaskToCheck(String option, boolean toMarkDone) {
        int taskID = parseTaskId(option);
        if (taskID == -1) { return; }
        try {
            if (toMarkDone) {
                if (!toDoList.get(taskID).getStatus()) {
                    toDoList.get(taskID).markAsDone();
                    System.out.println(replyFormatter("Nice! I've marked this task as done:\n\t\t\t"
                            + toDoList.get(taskID).toString()));
                } else {
                    throw new ActionCompletedException();
                }
            } else {
                if (toDoList.get(taskID).getStatus()) {
                    toDoList.get(taskID).markAsUndone();
                    System.out.println(replyFormatter("OK, I've marked this task as not done yet:\n\t\t\t"
                            + toDoList.get(taskID).toString()));
                } else {
                    throw new ActionCompletedException();
                }
            }
        } catch (ActionCompletedException e) {
            System.out.println(replyFormatter(e.getMessage()));
        }

    }

    /**
     * finds the corresponding task and delete it
     *
     * @param response user command to exceute
     */
    static void deleteListItem(String response) {
        String[] responseLst = response.split(" ");
        try {
            if (responseLst.length <= 1) throw new EmptyContentException();
        } catch (EmptyContentException e) {
            System.out.println(replyFormatter(e.getMessage()));
            return;
        }
        int taskID = parseTaskId(responseLst[1]);
        if (taskID == -1) { return; }
        String removeMsg = "Noted. I've removed this task:\n\t\t\t"
                + toDoList.get(taskID).toString();
        toDoList.remove(taskID);
        System.out.println(replyFormatter(removeMsg
                        + "\n\t Now you have "
                        + toDoList.size()
                        + " tasks in the list."
        ));
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println(intro);

        String response = input.nextLine();

        while (!response.equals("bye")) {
            try {
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
                case "delete":
                    deleteListItem(response);
                    break;
                case "todo":
                case "deadline":
                case "event":
                    addListItem(response);
                    break;
                default:
                    throw new UnknownCommandException();
                }
            } catch (UnknownCommandException e) {
                System.out.println(replyFormatter(e.getMessage()));
            }
            response = input.nextLine();
        }
        System.out.println(outro);


    }
}
