import ch.qos.logback.classic.db.DBAppender;
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
    private Stage prevStage;

    @SuppressWarnings("WeakerAccess")
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

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

/*
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/map.fxml"));
        Pane myPane = myLoader.load();

        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Map");
        stage.setScene(scene);
        stage.show();

 */
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
