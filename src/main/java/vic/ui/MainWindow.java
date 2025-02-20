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

    /**
     * Initializes the main window by setting up the scroll pane and displaying an intro message
     * from Vic in the dialog container.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().addAll(DialogBox.getVicDialog(
                new IntroResponse(),
                vicImage));
    }

    /**
     * Sets the Vic instance that will handle the user input and generate responses.
     *
     * @param vic The Vic instance used for handling the input and generating responses.
     */
    public void setVic(Vic vic) {
        this.vic = vic;
    }

    /**
     * Handles user input by retrieving the text from the input field, passing it to the Vic instance,
     * and updating the dialog container with the user's input and Vic's response.
     */
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
