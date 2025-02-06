package main.java;

import main.java.exceptions.VicException;
import main.java.enums.Command;

/**
 * The main class for the Vic chatbot.
 */
public class Vic {
    private static final String fileName = "/vic.txt";
    private static final String folderPath = "./data";

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    /**
     * Constructs a Vic instance, initializing UI, storage, and task management.
     *
     * @param fileName   The name of the file where tasks are stored
     * @param folderPath The path to the folder where the file is stored
     */
    public Vic(String fileName, String folderPath) {
        ui = new Ui();
        storage = new Storage(fileName, folderPath);
        try {
            if (!storage.checkFileExists()) {
                throw new VicException();
            }
            taskList = storage.load();
        } catch (VicException e) {
            Ui.out(e.getMessage());
        }

    }

    /**
     * Runs the chatbot application.
     */
    public void run() {
        ui.showIntro();
        boolean isExit = false;
        while (!isExit) {
            String action = ui.readCommand();
            String[] responseLst = action.split(" ");
            Command command = Command.convertText(responseLst[0]);
            BotActions.execute(storage, taskList, command, action);
            isExit = command.equals(Command.BYE);
        }
        ui.showOutro();
    }

    /**
     * The entry point of the application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        new Vic(fileName, folderPath).run();
    }
}
