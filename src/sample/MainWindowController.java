package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.spreadsheet.Grid;

import java.util.ArrayList;
import java.util.Optional;

public class MainWindowController {
    @FXML
    VBox ResponseVbox;
    @FXML
    VBox InboxVbox;
    @FXML
    Label UsernameLabel;
    Client client;
    ArrayList<String> universities;
    ArrayList<Obszar> areas;
    public void setup(Client client, String nazwa, String uczelnia, ArrayList<Obszar> obszary, ArrayList<String> universities, ArrayList<Obszar> areas)
    {
        this.client=client;
        this.universities=universities;
        this.areas=areas;
        client.connect(nazwa, uczelnia, obszary, this);
        UsernameLabel.setText(client.nazwa.substring(0,1));


    }
    public void giveHelpRequest(String description, String username, int id)
    {
        Platform.runLater(()->{
            InboxRequest inboxRequest = new InboxRequest(description, id, username, InboxVbox, client);
            InboxVbox.getChildren().add(inboxRequest);
        });

    }
    public void giveResponse(String description, String username)
    {
        Platform.runLater(()->{
            InboxRequest inboxRequest = new InboxRequest(description, username, ResponseVbox, client);
            ResponseVbox.getChildren().add(inboxRequest);
        });

    }
    public void displayDialogPane()
    {
        Dialog dialog = new Dialog();
        dialog.setTitle("Hello");
        dialog.setHeaderText("Create New Help Request");
        dialog.setResizable(true);
        Label description = new Label("Description: ");
        Label university = new Label("University: ");
        Label learnArea = new Label("Area: ");
        ComboBox<String> universityComboBox = new ComboBox<>();
        universityComboBox.getItems().setAll(universities);
        ComboBox<Obszar> learnAreaComboBox = new ComboBox<>();
        learnAreaComboBox.getItems().setAll(areas);
        TextField descriptionTextBox = new TextField();
        GridPane gridPane = new GridPane();
        gridPane.add(university,1,1);
        gridPane.add(universityComboBox, 2, 1);
        gridPane.add(description, 1,2);
        gridPane.add(descriptionTextBox, 2,2);
        gridPane.add(learnArea, 1,3);
        gridPane.add(learnAreaComboBox, 2,3);
        dialog.getDialogPane().setContent(gridPane);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, HelpRequest>() {
            @Override
            public HelpRequest call(ButtonType param) {
                if (param.equals(buttonTypeOk)) {

                    return new HelpRequest(universityComboBox.getSelectionModel().getSelectedItem(), descriptionTextBox.getText(), learnAreaComboBox.getSelectionModel().getSelectedItem());
                }
                return null;
            }
        });
        Optional <HelpRequest>result = dialog.showAndWait();
        if(result.isPresent())
        {
            System.out.println("Result: " + result.get().university);
            client.getHelp(result.get().learnArea, result.get().description, "  ", result.get().university);
        }



    }
}
