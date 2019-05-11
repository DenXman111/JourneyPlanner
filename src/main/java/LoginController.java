import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage prevStage;

    @FXML
    private TextField UsernameField;

    @FXML
    private TextField PasswordField;

    @FXML
    private Button ReturnButton;

    @FXML
    private Button LoginButton;

    @FXML
    private Button ShowHistoryButton;

    @SuppressWarnings("WeakerAccess")
    protected void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(prevStage);

        Scene myScene = new Scene(myPane);
        prevStage.setScene(myScene);
    }

    @FXML
    void findButtonPressed(ActionEvent event) throws IOException{
        Stage stage = new Stage();
        stage.setTitle("Main");

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/form.fxml"));
        Pane myPane = myLoader.load();
        FormController controller = myLoader.getController();
        controller.setPrevStage(stage);

        Scene scene = new Scene(myPane);
        stage.setScene(scene);
        prevStage.close();
        stage.show();
    }

    @FXML
    void showHistoryButtonPressed(ActionEvent event) {

    }
}