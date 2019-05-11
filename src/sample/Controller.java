package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    ComboBox UniversityComboBox;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    public void setDataForUniveristy()
    {
        UniversityComboBox.getItems().clear();
        UniversityComboBox.getItems().addAll(
                "PJATK",
                "PW"
        );
    }

}
