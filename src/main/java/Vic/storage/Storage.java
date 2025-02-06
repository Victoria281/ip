package main.java.Vic.storage;
import main.java.Vic.parser.Parser;
import main.java.Vic.tasks.TaskList;
import main.java.Vic.ui.Ui;
import main.java.Vic.enums.FileCodes;
import main.java.Vic.exceptions.*;
import main.java.Vic.tasks.Deadline;
import main.java.Vic.tasks.Event;
import main.java.Vic.tasks.Task;
import main.java.Vic.tasks.ToDo;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Storage {

    private static String fileName;
    private static String folderPath;
    private static Ui Ui = new Ui();

    public Storage(String fileName, String folderPath) {
        this.fileName = fileName;
        this.folderPath = folderPath;
    }


    /**
     * On startup load
     *
     * @return the TaskList created
     */
    public static TaskList load() {
        TaskList tasks = new TaskList();
        loadTasksFromFile(tasks);
        return tasks;
    }

    /**
     * Load the file data into storage
     *
     * @return the TaskList filled up
     */
    public static TaskList loadTasksFromFile(TaskList tasks) {
        try {
            FileReader in = new FileReader(folderPath+fileName);
            BufferedReader br = new BufferedReader(in);

            Map<String, String> errorMap = new HashMap<>();
            tasks.clearTasks();
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
                        String description = contents[2];
                        newItem = new ToDo(description);
                        break;
                    case D:
                        String deadlineDescription = contents[2];
                        LocalDateTime by = Parser.parseDate(contents[3]);
                        newItem = new Deadline(deadlineDescription, by);
                        break;
                    case E:
                        String eventDescription = contents[2];
                        LocalDateTime from = Parser.parseDate(contents[3]);
                        LocalDateTime to = Parser.parseDate(contents[4]);
                        newItem = new Event(eventDescription, from, to);
                        break;
                    default:
                        break;
                }
                if (contents.length > 1 && contents[1].equals("1")) {
                    newItem.markAsDone();
                }
                tasks.addTask(newItem);
                line = br.readLine();
                lineNumber++;
            }
            br.close();
            in.close();

            fixErrorLines(errorMap);
            return tasks;
        } catch (IOException e) {
            Ui.out("Error retrieving historical data! Please try again! (-̀╯⌓╰-́)");
        } catch (DateTimeParseException e) {
            Ui.out("Error parsing date in historical data! Please try again! (-̀╯⌓╰-́)");
        }
        return null;
    }

    /**
     * Checks if a file for storage exists
     *
     * @return true if file exists and false otherwise
     */
    public static boolean checkFileExists() {
        try {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
                File file = new File(folderPath + fileName);
                boolean createdFile = file.createNewFile();
                if (createdFile) {
                    Ui.out("Unable to find data. A new storage is created at: " + folderPath + fileName);
                } else {
                    Ui.out("Failed to create the new storage area. Please try again!");
                    return false;
                }
            }
        } catch (IOException e) {
            Ui.out("Error retrieving historical data! Please try again! (-̀╯⌓╰-́)");
        }
        return true;
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
            Ui.out("Error updating file with corrected lines!");
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
                Parser.parseDate(fixedContents.get(3));
            } catch (DateTimeParseException e) {
                fixedContents.set(3, Parser.formatDefaultDate());
            }
            hasChanged = true;
        }

        if (command == FileCodes.E && !fixedContents.get(4).isEmpty()) {
            try {
                Parser.parseDate(fixedContents.get(4));
            } catch (DateTimeParseException e) {
                fixedContents.set(4, Parser.formatDefaultDate());
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
    public static void saveNewTaskToFile(Task task) {
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
            Ui.out("Error saving task to file! (-̀╯⌓╰-́)");
        }
    }

    /**
     * Checks if the task exists at line (index) of the file
     *
     * @param index index of task to check against file
     * @param task task to check against file
     * @return boolean if file exists
     */
    public static boolean taskExistsAtIndex(int index, Task task) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));
            if (index >= 0 && index < lines.size()) {
                String[] contents = lines.get(index).split(" \\| ");
                String description = contents.length > 2 ? contents[2] : "";
                if (description.equals(task.getDescription())) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            Ui.out("Error reading tasks from file!");
            return false;
        }
    }

    /**
     * Deletes task from file
     *
     * @param index index of task to delete
     * @param task task to delete
     */
    public static void deleteTaskAtIndex(int index, Task task) {
        try {
            if (!taskExistsAtIndex(index, task)) throw new FileContentCorruptedException();
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));
            lines.remove(index);
            Files.write(Paths.get(folderPath + fileName), lines);
        } catch (FileContentCorruptedException e) {
            Ui.out(e.getMessage());
        } catch (IOException e) {
            Ui.out("Error deleting task from file!");
        }
    }

    /**
     * Save edited content of file
     *
     * @param index index of task to edit
     * @param task task to edit
     */
    public static void saveEditedTaskAtIndex(int index, Task task) {
        try {
            if (!taskExistsAtIndex(index, task)) throw new FileContentCorruptedException();
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
            Ui.out(e.getMessage());
        } catch (IOException e) {
            Ui.out("Error editing task in file!");
        }
    }

}
