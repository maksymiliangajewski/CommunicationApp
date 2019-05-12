package sample;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Optional;

public class InboxRequest extends AnchorPane {
    String description;
    int id;
    String username;
    Client client;
    VBox InboxVbox;
    public InboxRequest(String description, int id, String username, VBox InboxVbox, Client client)
    {
        this.client=client;
        this.description = description;
        this.id = id;
        this.username = username;
        this.InboxVbox=InboxVbox;
        this.setPrefSize(200, 100);
        GridPane gridPane = new GridPane();
        Label label = new Label(this.username);
        Label descriptionLabel = new Label(this.description);
      //  Label abbr = new Label(this.username.substring(0,1));
        Button okButton = new Button("OK");
        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction((e)->{
            InboxVbox.getChildren().remove(this);
        });
        okButton.setOnAction((e)->{
            displayDialogPane();
            InboxVbox.getChildren().remove(this);
        });
        okButton.setPrefSize(60,18);
        rejectButton.setPrefSize(60,18);
        okButton.setStyle("-fx-background-color: rgba(255, 255, 255, 1)");
        rejectButton.setStyle("-fx-background-color: rgba(255, 255, 255, 1)");
        gridPane.setPrefSize(this.getPrefWidth(),this.getPrefHeight());
        gridPane.add(label, 2,1,2,1);
        gridPane.add(descriptionLabel,2,2,2,1);
        gridPane.add(okButton, 1,1);
        gridPane.add(rejectButton, 1,2);
        gridPane.setStyle("-fx-background-color: white; -fx-background-insets: 10px; -fx-padding: 10px;\n" +
                "    -fx-border-insets: 10px;");
        this.setStyle("-fx-background-color: white;");
        this.setHeight(30);
        this.getChildren().add(gridPane);
    }
    public InboxRequest(String description, String username, VBox InboxVbox, Client client)
    {
        this.client=client;
        this.description = description;
        this.username = username;
        this.InboxVbox=InboxVbox;
        this.setPrefSize(200, 100);
        GridPane gridPane = new GridPane();
        Label label = new Label(this.username);
        Label descriptionLabel = new Label(this.description);
        Label abbr = new Label(this.username.substring(0,1));
        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction((e)->{
            InboxVbox.getChildren().remove(this);
        });
        gridPane.add(label, 1,1);
        gridPane.add(descriptionLabel,1,2);
        gridPane.add(rejectButton, 2,3);
        this.getChildren().add(gridPane);
    }

    public void displayDialogPane()
    {
        Dialog dialog = new Dialog();
        dialog.setTitle("Hello");
        dialog.setHeaderText("Create New Help Request");
        dialog.setResizable(true);
        Label description = new Label("Description: ");
        TextField descriptionTextBox = new TextField();
        GridPane gridPane = new GridPane();
        gridPane.add(description, 1,0);
        gridPane.add(descriptionTextBox, 2,0);
        dialog.getDialogPane().setContent(gridPane);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, String>() {
            @Override
            public String call(ButtonType param) {
                if (param.equals(buttonTypeOk)) {

                    return descriptionTextBox.getText();
                }
                return null;
            }
        });
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent())
        {
            System.out.println("Result: " + result.get());
            client.respond(client.nazwa, id, result.get());

        }



    }
}
