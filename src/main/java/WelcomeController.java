import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    public Button SignUp;
    public Button Login;
    public Button WelcomeSceneStartButton;
    public VBox ButtonsBox;
    public ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        if (DbAdapter.connected()) {
            progressIndicator.setVisible(false);
            return;
        }
        Login.setVisible(false);
        SignUp.setVisible(false);
        WelcomeSceneStartButton.setDisable(true);
        Task<Integer> connectTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                updateMessage("Connecting to database...");
                try {
                    DbAdapter.connect();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to connect to database");
                    updateMessage("Failed to connect to database");
                    Platform.runLater(() -> progressIndicator.setVisible(false));
                    return 0;
                }


                Platform.runLater(() -> {
                    Login.setVisible(true);
                    SignUp.setVisible(true);
                    progressIndicator.setVisible(false);
                    WelcomeSceneStartButton.setDisable(false);
                });
                updateMessage("Plan my trip");
                return 100;
            }
        };

        WelcomeSceneStartButton.textProperty().bind(connectTask.messageProperty());
        progressIndicator.progressProperty().bind(connectTask.progressProperty());
        new Thread(connectTask).start();

    }

    @FXML
    public void startButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.FORM);
    }

    @FXML
    public void loginPressed() throws IOException{
        StageChanger.changeStage(StageChanger.ApplicationStage.LOGIN);
    }

    @FXML
    public void signUpPressed() throws IOException{
        StageChanger.changeStage(StageChanger.ApplicationStage.SIGN_UP);
    }
}
