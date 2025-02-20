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
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import vic.enums.Command;
import vic.response.Response;

public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }


    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }


    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }


    public static DialogBox getVicDialog(Response response, Image img) {
        var db = new DialogBox(response.getMessage(), img);
        db.flip();
//        db.changeDialogStyle(action);
        return db;
    }

    private void changeDialogStyle(Command action) {
        switch(action) {
        case TODO:
        case EVENT:
        case DEADLINE:
//            dialog.getStyleClass().add("add-label");
            break;
        case UNMARK:
        case MARK:
//            dialog.getStyleClass().add("marked-label");
            break;
        case DELETE:
//            dialog.getStyleClass().add("delete-label");
            break;
        default:
            // Do nothing
        }
    }
}