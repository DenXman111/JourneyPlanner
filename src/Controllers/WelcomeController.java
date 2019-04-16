package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {

    @FXML
    private Button WelcomeSceneStartButton;

    @FXML
    void startButtonPressed(ActionEvent event) {
        System.out.println("I should change scene : )");
    }
}
