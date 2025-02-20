package vic.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import vic.response.Response;

/**
 * Represents a dialog box in the user interface.
 * It displays a message along with a display picture and an optional name label.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    @FXML
    private Label nameLabel;

    /**
     * Creates a new DialogBox with the specified text, image, and name.
     * Loads the FXML file for the DialogBox layout and initializes the components.
     *
     * @param text The text to be displayed in the dialog.
     * @param img The image to be displayed as the user's display picture (may be null).
     * @param name The name to be displayed beside the dialog (may be null).
     */
    private DialogBox(String text, Image img, String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        if (img != null) {
            displayPicture.setImage(img);
            displayPicture.setVisible(true);
            displayPicture.setManaged(true);
        } else {
            displayPicture.setVisible(false);
            displayPicture.setManaged(false);
        }
        if (name != null) {
            nameLabel.setText(name);
        } else {
            nameLabel.setVisible(false);
        }
    }

    /**
     * Flips the dialog box by reversing its components, changing the alignment,
     * and adjusting the style class of the dialog label to indicate a reply.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box for user input, with the given text and image.
     * Applies the style class "user-message" to the dialog.
     *
     * @param text The text to be displayed in the dialog.
     * @param img The image to be displayed as the user's display picture.
     * @return A DialogBox instance representing the user input.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, null, null);
        db.dialog.getStyleClass().add("user-message");
        return db;
    }

    /**
     * Creates a dialog box for the system (Vic) response, with the given response message
     * and image. Applies error styling if the response indicates an error, and flips
     * the dialog box for the system's reply.
     *
     * @param response The system's response object containing the message and error state.
     * @param img The image to be displayed as Vic's display picture.
     * @return A DialogBox instance representing Vic's response.
     */
    public static DialogBox getVicDialog(Response response, Image img) {
        DialogBox db = new DialogBox(response.getMessage(), img, "Vic");
        if (response.isError()) {
            db.dialog.getStyleClass().add("error-message");
        }
        db.flip();
        db.dialog.getStyleClass().add("vic-message");
        return db;
    }
}
