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
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(Year.now().getValue(), Month.JANUARY, 1, 23, 59);


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

                LocalDateTime byDate = parseDateTime(by, dateTimeFormatter);
                newItem = new Deadline(description, byDate);
                break;
            case EVENT:
                description = response.split(" ", 2)[1].split("/")[0];
                if (description.length() < 1) throw new EmptyContentException();

                String[] part2 = response.split("/from", 2);
                if (part2.length < 2) throw new EmptyContentException();

                String[] fromSplit = part2[1].split("/to", 2);
                String from = fromSplit[0].trim();
                if (from.isEmpty()) throw new EmptyContentException();

                String to = fromSplit.length > 1 ? fromSplit[1].trim() : "";
                if (to.isEmpty()) throw new EmptyContentException();

                LocalDateTime fromDate = parseDateTime(from, dateTimeFormatter);
                LocalDateTime toDate = parseDateTime(to, dateTimeFormatter);

                newItem = new Event(description, fromDate, toDate);
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
        } catch (DateTimeParseException e) {
            System.out.println(replyFormatter("Date given is in wrong format, Please try again! ಥ‿ಥ"));
        }
    }

    /**
     * formats the provided string as a date time object
     *
     * @param dateTimeStr date string
     * @param formatter format type
     * @return date object according to format type
     */
    static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTimeStr, formatter);
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
     * @return message of the action completed
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
     *
     * @return true if file exists and false otherwise
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

            Map<String, String> errorMap = new HashMap<>();
            toDoList.clear();
            String line = br.readLine();
            int lineNumber = 0;
            while (line != null) {
                String checkedLine = checkOrFixTaskFormat(line);
                if (checkedLine.equals("delete")) {
                    errorMap.put("delete", lineNumber+"");
                } else if (!checkedLine.equals("-1")) {
                    line = checkedLine;
                    errorMap.put(lineNumber+"", checkedLine);
                }

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
                        LocalDateTime by = contents.length > 3 ? LocalDateTime.parse(contents[3], dateTimeFormatter) : DEFAULT_DATE;
                        newItem = new Deadline(deadlineDescription, by);
                        break;
                    case E:
                        String eventDescription = contents.length > 2 ? contents[2] : "";
                        LocalDateTime from = contents.length > 3 ? LocalDateTime.parse(contents[3], dateTimeFormatter) : DEFAULT_DATE;
                        LocalDateTime to = contents.length > 4 ? LocalDateTime.parse(contents[4], dateTimeFormatter) : DEFAULT_DATE;
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
                lineNumber++;
            }
            br.close();
            in.close();

            fixErrorLines(errorMap);
        } catch (IOException e) {
            System.out.println("Error retrieving historical data! Please try again! (-̀╯⌓╰-́)");
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date in historical data! Please try again! (-̀╯⌓╰-́)");
        }
    }

    /**
     * Checks the given line and fixes line format if necessary
     *
     * @param errorMap stores lines with error or wrong format to update
     */
    static void fixErrorLines(Map<String, String> errorMap) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                if (entry.getKey().equals("delete")) {
                    lines.remove(Integer.parseInt(entry.getValue()));
                } else {
                    lines.set(Integer.parseInt(entry.getKey()), entry.getValue());
                }
            }
            Files.write(Paths.get(folderPath + fileName), lines);

        } catch (IOException e) {
            System.out.println("Error updating file with corrected lines!");
        }
    }

    /**
     * Checks the given line and fixes line format if necessary
     *
     * @param line provides line read from file to check against
     * @return "delete" if line is not readable,
     *          line number and fixed line if line is corrupted,
     *          "-1" if line has no issues
     */
    static String checkOrFixTaskFormat(String line) {
        String[] contents = line.split(" \\| ");
        boolean hasChanged = false;
        if (contents.length < 1) return "delete";

        FileCodes command;
        try {
            command = FileCodes.convertText(contents[0]);
            if (command.equals(FileCodes.N)) {
                return "delete";
            }
        } catch (Exception e) {
            return "delete";
        }

        int requiredLength;
        switch (command) {
            case T:
                requiredLength = 3;
                break;
            case D:
                requiredLength = 4;
                break;
            case E:
                requiredLength = 5;
                break;
            default:
                return "delete";
        }

        List<String> fixedContents = new ArrayList<>(Arrays.asList(contents));
        while (fixedContents.size() < requiredLength) {
            fixedContents.add("");
            hasChanged = true;
        }

        if ((command == FileCodes.D || command == FileCodes.E) && !fixedContents.get(3).isEmpty()) {
            try {
                LocalDateTime.parse(fixedContents.get(3), dateTimeFormatter);
            } catch (DateTimeParseException e) {
                fixedContents.set(3, DEFAULT_DATE.format(dateTimeFormatter));
            }
            hasChanged = true;
        }

        if (command == FileCodes.E && !fixedContents.get(4).isEmpty()) {
            try {
                LocalDateTime.parse(fixedContents.get(4), dateTimeFormatter);
            } catch (DateTimeParseException e) {
                fixedContents.set(4, DEFAULT_DATE.format(dateTimeFormatter));
            }
            hasChanged = true;
        }

        return !hasChanged ? "-1" : String.join(" | ", fixedContents);
    }

    /**
     * Save new task to file
     *
     * @param task task to save to file
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
     * Checks if the task exists at line (index) of the file
     *
     * @param index index of task to check against file
     * @param task task to check against file
     * @return boolean if file exists
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
     *
     * @param index index of task to delete
     * @param task task to delete
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
     * 
     * @param index index of task to edit
     * @param task task to edit
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
