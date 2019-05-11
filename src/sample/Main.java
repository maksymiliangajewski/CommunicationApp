package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Hello World");
        Pane mainWindowPane = new Pane(root);
        Scene mainWindowScene = new Scene(mainWindowPane);
        mainWindowPane.prefHeightProperty().bind(mainWindowScene.heightProperty());
        mainWindowPane.prefWidthProperty().bind(mainWindowScene.widthProperty());
        primaryStage.setScene(mainWindowScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
