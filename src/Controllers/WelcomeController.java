package Controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    public VBox mainBox;

    /**
     * Checks if inserted data is correct and changes the scene to tripIdeas
     */
    public void ShowTripIdeas(ActionEvent actionEvent) throws IOException {
        SceneChanger.changeScene( (Stage)mainBox.getScene().getWindow(), "/xmlFiles/tripIdeas.fxml");
    }

}