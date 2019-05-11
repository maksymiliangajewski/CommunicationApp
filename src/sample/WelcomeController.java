package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    @FXML
    ComboBox<String> UniversityComboBox;
    @FXML
    CheckComboBox<String> LearnCheckComboBox;
    @FXML
    TextField UsernameTextField;
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        UniversityComboBox.getItems().setAll("PJATK", "PW");
        LearnCheckComboBox.getItems().setAll("Hello", "Hello2");
    }
    public void LogInButtonClicked()
    {
        //System.out.println(UsernameTextField.getText());
        //System.out.println(UniversityComboBox.getSelectionModel().getSelectedItem());
        //System.out.println(LearnCheckComboBox.getItems().get(LearnCheckComboBox.getCheckModel().getCheckedIndices().get(0)));
        ArrayList<String> selectedLearnAreas = new ArrayList<>();
        for(int i=0 ;i<LearnCheckComboBox.getCheckModel().getCheckedIndices().size(); i++)
        {
            selectedLearnAreas.add(LearnCheckComboBox.getItems().get(LearnCheckComboBox.getCheckModel().getCheckedIndices().get(i)));
        }
        String username = UsernameTextField.getText();
        String university = UniversityComboBox.getSelectionModel().getSelectedItem();
    }
}
