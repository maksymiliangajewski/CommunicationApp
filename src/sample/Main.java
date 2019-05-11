package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        AnchorPane loader = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(loader));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
