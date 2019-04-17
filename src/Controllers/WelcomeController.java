package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController {

    Stage prevStage;
    @FXML
    private Button WelcomeSceneStartButton;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public void startButtonPressed(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Main");
        Pane myPane = FXMLLoader.load(getClass().getResource("../xmlFiles/main.fxml"));
        Scene scene = new Scene(myPane);
        stage.setScene(scene);

        prevStage.close();
        stage.show();
    }
}
