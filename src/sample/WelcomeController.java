package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    @FXML
    ComboBox<String> UniversityComboBox;
    @FXML
    CheckComboBox<Obszar> LearnCheckComboBox;
    @FXML
    TextField UsernameTextField;
    Client client = new Client();
    ArrayList<String> universities;
    ArrayList<Obszar> learnAreas;
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        universities=client.getUczelnie();
        learnAreas=client.getObszary();
        UniversityComboBox.getItems().setAll(universities);
        LearnCheckComboBox.getItems().setAll(learnAreas);
    }
    public void LogInButtonClicked()
    {
        ArrayList<Obszar> selectedLearnAreas = new ArrayList<>();
        for(int i=0 ;i<LearnCheckComboBox.getCheckModel().getCheckedIndices().size(); i++)
        {
            selectedLearnAreas.add(LearnCheckComboBox.getItems().get(LearnCheckComboBox.getCheckModel().getCheckedIndices().get(i)));
        }
        String username = UsernameTextField.getText();
        String university = UniversityComboBox.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            MainWindowController mainWindowController = fxmlLoader.getController();
            mainWindowController.setup(client, username, university, selectedLearnAreas,universities,  learnAreas);
            Stage closeStage = (Stage)UsernameTextField.getScene().getWindow();
            closeStage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
