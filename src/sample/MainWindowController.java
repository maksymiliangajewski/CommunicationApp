package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
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
        WelcomeController.st.setResizable(false);
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
            InboxVbox.setSpacing(10);
            InboxVbox.getChildren().add(inboxRequest);
        });

    }
    public void giveResponse(String description, String username)
    {
        Platform.runLater(()->{
            InboxRequest inboxRequest = new InboxRequest(description, username, ResponseVbox, client);
            InboxVbox.setSpacing(10);
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
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(10);
        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().addAll(col1,col2, col3);
        gridPane.add(university,0,0);
        gridPane.add(universityComboBox, 1, 0,2,1);
        gridPane.add(description, 0,1);
        gridPane.add(descriptionTextBox, 1,1,2,1);
        gridPane.add(learnArea, 0,2);
        gridPane.add(learnAreaComboBox, 1,2,2,1);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(new Callback<ButtonType, HelpRequest>() {
            @Override
            public HelpRequest call(ButtonType param) {
                if (param!=null && param.equals(buttonTypeOk)) {

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
