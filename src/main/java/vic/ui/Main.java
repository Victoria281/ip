package vic.ui;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vic.Vic;

public class Main extends Application {

    private static final String FILE_NAME = "/vic.txt";
    private static final String FOLDER_PATH = "./data";

    private Vic vic = new Vic(FILE_NAME, FOLDER_PATH);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Vic");
            fxmlLoader.<MainWindow>getController().setVic(vic);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}