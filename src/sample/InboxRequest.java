package sample;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        okButton.setStyle("-fx-background-color: rgba(255, 255, 255, 1); -fx-text-fill: green");
        rejectButton.setStyle("-fx-background-color: rgba(255, 255, 255, 1); -fx-text-fill: red");
        label.setStyle("-fx-font-weight: bold;");
        label.setWrapText(true);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col2.setPercentWidth(60);
        gridPane.getColumnConstraints().addAll(col1,col2);
        gridPane.setPrefSize(this.getPrefWidth(),this.getPrefHeight());
        gridPane.add(label, 1,1,2,1);
        gridPane.add(descriptionLabel,1,2,1,2);
        gridPane.add(okButton, 0,1);
        gridPane.add(rejectButton, 0,2);
        gridPane.setStyle("-fx-background-color: white; -fx-background-insets: 10px; -fx-padding: 10px;\n" +
                "    -fx-border-insets: 10px;");
        this.setStyle("-fx-background-color: white;-fx-background-radius: 10px;");
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
        Button rejectButton = new Button("Dissmis");
        rejectButton.setStyle("-fx-background-color: rgba(255, 255, 255, 1); -fx-text-fill: red");
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col2.setPercentWidth(60);
        Label label = new Label(this.username);
        label.setStyle("-fx-font-weight: bold;");
        Label descriptionLabel = new Label(this.description);
        Label abbr = new Label(this.username.substring(0,1));
        rejectButton.setOnAction((e)->{
            InboxVbox.getChildren().remove(this);
        });
        gridPane.add(label, 0,1);
        gridPane.add(descriptionLabel,0,2);
        gridPane.add(rejectButton, 2,3);
        gridPane.setStyle("-fx-background-color: white; -fx-background-insets: 10px; -fx-padding: 10px;\n" +
                "    -fx-border-insets: 10px;");
        this.setStyle("-fx-background-color: white;-fx-background-radius: 10px;");
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
        dialog.setOnCloseRequest(e->{
            dialog.close();
        });
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
