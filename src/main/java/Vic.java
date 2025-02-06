package main.java;

import main.java.enums.FileCodes;
import main.java.exceptions.*;
import main.java.tasks.Deadline;
import main.java.tasks.Event;
import main.java.tasks.Task;
import main.java.tasks.ToDo;
import main.java.enums.Command;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    private static final String fileName = "/vic.txt";
    private static final String folderPath = "./data";

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
            Command command = Command.convertText(responseLst[0]);
            switch (command) {
            case TODO:
                description = response.split(" ", 2)[1];
                if (description.length() < 1) throw new EmptyContentException();

                newItem = new ToDo(description);
                break;
            case DEADLINE:
                description = response.split(" ", 2)[1].split("/")[0];
                if (description.length() < 1) throw new EmptyContentException();

                String by = response.split("/by ").length > 1 ? response.split("/by ")[1] : "";
                if (by.length() < 1) throw new EmptyContentException();

                newItem = new Deadline(description, by);
                break;
            case EVENT:
                description = response.split(" ", 2)[1].split("/")[0];
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
            saveNewTaskToFile(newItem);
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
        loadTasksFromFile();
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
                    saveEditedTaskAtIndex(taskID, toDoList.get(taskID));
                    System.out.println(replyFormatter("Nice! I've marked this task as done:\n\t\t\t"
                            + toDoList.get(taskID).toString()));
                } else {
                    throw new ActionCompletedException();
                }
            } else {
                if (toDoList.get(taskID).getStatus()) {
                    toDoList.get(taskID).markAsUndone();
                    saveEditedTaskAtIndex(taskID, toDoList.get(taskID));
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
        deleteTaskAtIndex(taskID, toDoList.get(taskID));
        System.out.println(replyFormatter(removeMsg
                        + "\n\t Now you have "
                        + toDoList.size()
                        + " tasks in the list."
        ));
    }


    /**
     * Checks if a file for storage exists
     */
    static boolean checkFileExists() {
        try {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
                File file = new File(folderPath + fileName);
                boolean createdFile = file.createNewFile();
                if (createdFile) {
                    System.out.println("Unable to find data. A new storage is created at: " + folderPath + fileName);
                } else {
                    System.out.println("Failed to create the new storage area. Please try again!");
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error retrieving historical data! Please try again! (-̀╯⌓╰-́)");
        }
        return true;
    }

    /**
     * Load tasks from a file at start
     */
    static void loadTasksFromFile() {
        try {
            FileReader in = new FileReader(folderPath+fileName);
            BufferedReader br = new BufferedReader(in);

            toDoList.clear();
            String line = br.readLine();
            while (line != null) {
                Task newItem = null;
                String[] contents = line.split(" \\| ");
                FileCodes command = FileCodes.convertText(contents[0]);
                switch (command) {
                    case T:
                        String description = contents.length > 2 ? contents[2] : "";
                        newItem = new ToDo(description);
                        break;
                    case D:
                        String deadlineDescription = contents.length > 2 ? contents[2] : "";
                        String by = contents.length > 3 ? contents[3] : "";
                        newItem = new Deadline(deadlineDescription, by);
                        break;
                    case E:
                        String eventDescription = contents.length > 2 ? contents[2] : "";
                        String from = contents.length > 3 ? contents[3] : "";
                        String to = contents.length > 4 ? contents[4] : "";
                        newItem = new Event(eventDescription, from, to);
                        break;
                    default:
                        break;
                }
                if (contents.length > 1 && contents[1].equals("1")) {
                    newItem.markAsDone();
                }
                toDoList.add(newItem);
                line = br.readLine();
            }
            br.close();
            in.close();
        } catch (IOException e) {
            System.out.println("Error retrieving historical data! Please try again! (-̀╯⌓╰-́)");
        }
    }


    /**
     * Save new task to file
     */
    static void saveNewTaskToFile(Task task) {
        try {
            FileWriter out = new FileWriter(folderPath + fileName, true);
            BufferedWriter bw = new BufferedWriter(out);

            FileCodes taskType;
            String isDone = "0";
            String line = "";

            if (task instanceof ToDo) {
                taskType = FileCodes.T;
                line = String.format("%s | %s | %s", taskType, isDone, task.getDescription());
            } else if (task instanceof Deadline) {
                taskType = FileCodes.D;
                Deadline deadline = (Deadline) task;
                line = String.format("%s | %s | %s | %s", taskType, isDone, deadline.getDescription(), deadline.getBy());
            } else if (task instanceof Event) {
                taskType = FileCodes.E;
                Event event = (Event) task;
                line = String.format("%s | %s | %s | %s | %s", taskType, isDone, event.getDescription(), event.getFrom(), event.getTo());
            }

            bw.write(line);
            bw.newLine();

            bw.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error saving task to file! (-̀╯⌓╰-́)");
        }
    }


    /**
     * Checks if the taskexists at line (index) of the file
     *
     * returns boolean if file exists
     */
    static boolean taskExistsAtIndex(int index, Task task) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));
            if (index >= 0 && index < lines.size()) {
                String[] contents = line.split(" \\| ");
                String description = contents.length > 2 ? contents[2] : "";
                if (description.equals(task.getDescription())) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.out.println("Error reading tasks from file!");
            return false;
        }
    }

    /**
     * Deletes task from file
     */
    static void deleteTaskAtIndex(int index, Task task) {
        try {
            if (taskExistsAtIndex(index, task)) throw new FileContentCorruptedException();
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));
            lines.remove(index);
            Files.write(Paths.get(folderPath + fileName), lines);
        } catch (FileContentCorruptedException e) {
            System.out.println(replyFormatter(e.getMessage()));
        } catch (IOException e) {
            System.out.println("Error deleting task from file!");
        }
    }

    /**
     * Save edited content of file
     */
    static void saveEditedTaskAtIndex(int index, Task task) {
        try {
            if (taskExistsAtIndex(index, task)) throw new FileContentCorruptedException();
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));

            String isDone = task.getStatus() ? "1" : "0";
            String line = "";
            FileCodes taskType;

            if (task instanceof ToDo) {
                taskType = FileCodes.T;
                line = String.format("%s | %s | %s", taskType, isDone, task.getDescription());
            } else if (task instanceof Deadline) {
                taskType = FileCodes.D;
                Deadline deadline = (Deadline) task;
                line = String.format("%s | %s | %s | %s", taskType, isDone, deadline.getDescription(), deadline.getBy());
            } else if (task instanceof Event) {
                taskType = FileCodes.E;
                Event event = (Event) task;
                line = String.format("%s | %s | %s | %s | %s", taskType, isDone, event.getDescription(), event.getFrom(), event.getTo());
            }
            lines.set(index, line);
            Files.write(Paths.get(folderPath + fileName), lines);
        } catch (FileContentCorruptedException e) {
            System.out.println(replyFormatter(e.getMessage()));
        } catch (IOException e) {
            System.out.println("Error editing task in file!");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("\n\n\n\tRetrieving records...");
        if (!checkFileExists()) {
            System.out.println(outro);
            return;
        }
        loadTasksFromFile();
        System.out.println("\n\n\n\tRetrieval done");


        System.out.println(intro);

        String response = "";

        boolean stop = false;
        while (!stop) {
            response = input.nextLine();
            try {
                String[] responseLst = response.split(" ");
                Command command = Command.convertText(responseLst[0]);
                switch (command) {
                case BYE:
                    System.out.println(outro);
                    stop = true;
                    break;
                case MARK:
                    findTaskToCheck(responseLst[1], true);
                    break;
                case UNMARK:
                    findTaskToCheck(responseLst[1], false);
                    break;
                case LIST:
                    printToDoList();
                    break;
                case DELETE:
                    deleteListItem(response);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    addListItem(response);
                    break;
                default:
                    throw new UnknownCommandException();
                }
            } catch (UnknownCommandException e) {
                System.out.println(replyFormatter(e.getMessage()));
            }
        }


    }
}
