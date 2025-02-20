package vic.ui;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vic.Vic;

/**
 * The Main class serves as the entry point to the Vic JavaFX application.
 */
public class Main extends Application {

    private static final String FILE_NAME = "/vic.txt";
    private static final String FOLDER_PATH = "./data";

    private Vic vic = new Vic(FILE_NAME, FOLDER_PATH);

    /**
     * The entry point for the JavaFX application.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/dialog-box.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Vic");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setVic(vic);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
