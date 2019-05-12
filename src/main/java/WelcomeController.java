import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    public Button SignUp;
    public Button Login;
    public Button WelcomeSceneStartButton;
    private Stage prevStage;

    @SuppressWarnings("WeakerAccess")
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @SuppressWarnings({"Duplicates", "unused"})
    @FXML
    public void startButtonPressed(ActionEvent event) throws IOException {
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
    @SuppressWarnings("unused")
    @FXML
    public void loginPressed(ActionEvent event) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/login.fxml"));

        Pane myPane = myLoader.load();
        LoginController controller = myLoader.getController();
        controller.setPrevStage(prevStage);

        Scene myScene = new Scene(myPane);
        prevStage.setScene(myScene);
    }

    @SuppressWarnings("unused")
    @FXML
    public void signUpPressed(ActionEvent event) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/signUp.fxml"));

        Pane myPane = myLoader.load();
        SignUpController controller = myLoader.getController();
        controller.setPrevStage(prevStage);

        Scene myScene = new Scene(myPane);
        prevStage.setScene(myScene);
    }
}
