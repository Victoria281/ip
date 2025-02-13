package vic;

import vic.actions.Action;
import vic.enums.Command;
import vic.exceptions.UnknownCommandException;
import vic.exceptions.VicException;
import vic.parser.Parser;
import vic.storage.Storage;
import vic.tasks.TaskList;
import vic.ui.Ui;

/**
 * The main class for the Vic chatbot.
 */
public class Vic {
    private static final String FILE_NAME = "/vic.txt";
    private static final String FOLDER_PATH = "./data";

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    /**
     * Constructs a Vic instance for task management.
     *
     * @param fileName   The name of the file where tasks are stored
     * @param folderPath The path to the folder where the file is stored
     */
    public Vic(String fileName, String folderPath) {
        this.ui = new Ui();
        this.storage = new Storage(fileName, folderPath);
        initializeTaskList();
    }

    /**
     * Initializes the task list by loading tasks from the storage.
     * If the file does not exist, it will handle the exception gracefully.
     */
    private void initializeTaskList() {
        try {
            if (!storage.checkFileExists()) {
                throw new VicException();
            }
            this.taskList = storage.load();
        } catch (VicException e) {
            ui.out(e.getMessage());
        }
    }

    /**
     * Starts and runs the Vic chatbot application.
     * It continuously accepts user input and performs actions.
     */
    public void run() {
        ui.showIntro();
        boolean isExit = false;

        while (!isExit) {
            String action = ui.readCommand();
            String[] responseLst = action.split(" ");
            Command command = Command.convertText(responseLst[0]);

            try {
                Action actionObject = Parser.parseCommand(command, action, storage, taskList);
                isExit = actionObject.execute();
            } catch (UnknownCommandException e) {
                ui.out(e.getMessage());
            }
        }
        ui.showOutro();
    }

    /**
     * The entry point of the application.
     */
    public static void main(String[] args) {
        new Vic(FILE_NAME, FOLDER_PATH).run();
    }
}
