package vic.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import vic.Vic;
import vic.response.IntroResponse;
import vic.response.Response;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Vic vic;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/vic.png"));
    private Image vicImage = new Image(this.getClass().getResourceAsStream("/images/vic.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().addAll(DialogBox.getVicDialog(
                new IntroResponse(),
                vicImage));
    }

    /**
     * Injects the BPlusChatter instance.
     */
    public void setVic(Vic vic) {
        this.vic = vic;
    }


    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        Response response = vic.handleRun(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getVicDialog(response, vicImage)
        );
        userInput.clear();
    }
}
