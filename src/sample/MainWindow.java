package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow {

    DoubleProperty x;

    public double getX()
    {
        return x.get();
    }

    public void setX(double value)
    {
        x.set(value);
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public MainWindow(){
        x = new SimpleDoubleProperty(10.0);
    }
}
