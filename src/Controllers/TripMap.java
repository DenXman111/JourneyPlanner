package Controllers;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TripMap {
    public VBox mainBox;

    public void Back(ActionEvent actionEvent) throws IOException {
        SceneChanger.changeScene((Stage)mainBox.getScene().getWindow(), "/xmlFiles/TripIdeas.fxml");
    }
}
