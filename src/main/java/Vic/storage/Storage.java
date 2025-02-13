package Vic.storage;

import Vic.parser.Parser;
import Vic.tasks.TaskList;
import Vic.ui.Ui;
import Vic.enums.FileCodes;
import Vic.exceptions.FileContentCorruptedException;
import Vic.tasks.Deadline;
import Vic.tasks.Event;
import Vic.tasks.Task;
import Vic.tasks.ToDo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * The Storage class handles the saving, loading, editing, and deleting of tasks in a file.
 */
public class Storage {

    private static String fileName;
    private static String folderPath;

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
     * Processes and adds a task to the TaskList based on the fixed line data.
     *
     * @param line  the fixed line containing task data
     * @param tasks the TaskList to add the task to
     */
    private static void processTaskLine(String line, TaskList tasks) {
        String[] contents = line.split(" \\| ");
        FileCodes command = FileCodes.convertText(contents[0]);
        Task newItem = null;

        switch (command) {
        case T:
            newItem = new ToDo(contents[2]);
            break;
        case D:
            LocalDateTime by = Parser.parseDate(contents[3]);
            newItem = new Deadline(contents[2], by);
            break;
        case E:
            LocalDateTime from = Parser.parseDate(contents[3]);
            LocalDateTime to = Parser.parseDate(contents[4]);
            newItem = new Event(contents[2], from, to);
            break;
        default:
            return;
        }
        if (contents.length > 1 && contents[1].equals("1")) {
            newItem.markAsDone();
        }
        tasks.addTask(newItem);
    }

    /**
     * Loads tasks from the file into the provided TaskList.
     *
     * @param tasks the TaskList to populate with tasks from the file
     */
    public static TaskList loadTasksFromFile(TaskList tasks) {
        try {
            FileReader in = new FileReader(folderPath+fileName);
            BufferedReader br = new BufferedReader(in);
            Map<Integer, String> errorMap = new HashMap<>();
            tasks.clearTasks();
            String line = br.readLine();
            int lineNumber = 0;

            while (line != null) {
                String checkedLine = checkOrFixTaskFormat(line);
                if (checkedLine.equals("delete")) {
                    errorMap.put(lineNumber, "delete");
                } else {
                    if (!checkedLine.equals("-1")) {
                        line = checkedLine;
                        errorMap.put(lineNumber, checkedLine);
                    }
                    processTaskLine(line, tasks);
                }

                line = br.readLine();
                lineNumber++;
            }
            br.close();
            in.close();

            fixErrorLines(errorMap);
            return tasks;
        } catch (IOException | DateTimeParseException e) {
            Ui.out("Error retrieving historical data! Please try again! (-̀╯⌓╰-́)");
        }
        return null;
    }

    /**
     * Checks if the file exists, and creates it if it does not.
     *
     * @return true if the file or folder was created successfully, false otherwise
     */
    public static boolean checkFileExists() {
        try {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folderPath, fileName);
            if (!file.exists()) {
                boolean createdFile = file.createNewFile();
                if (createdFile) {
                    Ui.out("Unable to find data. A new storage is created at: " + folder.getAbsolutePath() + "/" + fileName);
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
    static void fixErrorLines(Map<Integer, String> errorMap) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(folderPath + fileName));
            List<Integer> deleteIndices = new ArrayList<>();

            for (Map.Entry<Integer, String> entry : errorMap.entrySet()) {
                if (entry.getValue().equals("delete")) {
                    deleteIndices.add(entry.getKey());
                } else {
                    lines.set(entry.getKey(), entry.getValue());
                }
            }

            Collections.sort(deleteIndices, Collections.reverseOrder());
            for (Integer index : deleteIndices) {
                lines.remove((int) index);
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

        for (String content : contents) {
            if (content.contains("|")) {
                return "delete";
            }
        }

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

        if (contents.length > 2) {
            if (!contents[1].equals("1") && !contents[1].equals("0")) {
                contents[1] = "0";
                hasChanged = true;
            }
        }

        List<String> fixedContents = new ArrayList<>(Arrays.asList(contents));

        if (fixedContents.size() > requiredLength) {
            fixedContents = fixedContents.subList(0, requiredLength);
            hasChanged = true;
        } else if (fixedContents.size() < requiredLength) {
            while (fixedContents.size() < requiredLength) {
                fixedContents.add("");
                hasChanged = true;
            }
        }

        for (int i = 0; i < fixedContents.size(); i++) {
            if (fixedContents.get(i).isEmpty()) {
                fixedContents.set(i, getDefaultForField(i, command));
                hasChanged = true;
            }
        }

        if ((command == FileCodes.D || command == FileCodes.E)) {
            try {
                fixedContents.set(3, Parser.formatDate(Parser.parseDate(fixedContents.get(3))));
            } catch (DateTimeParseException e) {
                fixedContents.set(3, Parser.formatDefaultDate());
            }
            hasChanged = true;
        }

        if (command == FileCodes.E) {
            try {
                fixedContents.set(4, Parser.formatDate(Parser.parseDate(fixedContents.get(3))));
            } catch (DateTimeParseException e) {
                fixedContents.set(4, Parser.formatDefaultDate());
            }
            hasChanged = true;
        }

        return (!hasChanged ? "-1" : String.join(" | ", fixedContents));
    }

    /**
     * Returns the default value for a specific field based on the task type and field index.
     *
     * @param index The index of the field that is being checked.
     * @param command The type of task by the `FileCodes` enum
     * @return The default value to use for the given field index, based on the task type.
     */
    private static String getDefaultForField(int index, FileCodes command) {
        switch (command) {
        case T:
            if (index == 1) return "0";
            if (index == 2) return "No description";
            break;
        case D:
            if (index == 1) return "0";
            if (index == 2) return "No task description";
            if (index == 3) return Parser.formatDefaultDate();
            break;
        case E:
            if (index == 1) return "0";
            if (index == 2) return "No event description";
            if (index == 3) return Parser.formatDefaultDate();
            if (index == 4) return Parser.formatDefaultDate();
            break;
        default:
            return "";
        }
        return "";
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
