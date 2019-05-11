package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class MainWindowController {
    @FXML
    Label UsernameLabel;
    public void setup(String username)
    {
        System.out.println(username);
        UsernameLabel.setText(username.substring(0,1));
    }
    public void displayDialogPane()
    {
        Dialog dialog = new Dialog();
        dialog.setTitle("Hello");
        dialog.setHeaderText("Create New Help Request");
        dialog.setResizable(true);
        Label subject = new Label("Subject: ");
        Label description = new Label("Description: ");
        TextField subjectTextField = new TextField();
        TextField descriptionTextBox = new TextField();
        GridPane gridPane = new GridPane();
        gridPane.add(subject, 1,1);
        gridPane.add(description, 1,2);
        gridPane.add(subjectTextField, 2,1);
        gridPane.add(descriptionTextBox, 2,2);
        dialog.getDialogPane().setContent(gridPane);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        Optional result = dialog.showAndWait();


    }
}
