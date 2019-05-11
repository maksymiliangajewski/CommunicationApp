package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainWindowController {
    @FXML
    Label UsernameLabel;
    public void setup(String username)
    {
        System.out.println(username);
        UsernameLabel.setText(username.substring(0,1));
    }
}
