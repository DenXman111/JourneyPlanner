import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jnr.ffi.annotations.In;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    public Button ReturnButton;
    public Button ModerLoginButton;
    public Button ShowHistoryButton;
    public Button LoginButton;
    private Stage prevStage;
    static String username = null;

    @FXML
    private TextField UsernameField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private VBox loginBox;

    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @FXML
    void returnButtonPressed() throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(prevStage);

        Scene myScene = new Scene(myPane);
        prevStage.setScene(myScene);
    }

    @FXML
    @SuppressWarnings("Duplicates")
    void findButtonPressed(){

        loginBox.setVisible(false);
        Task<Integer> loginTask = new Task<Integer>() {
            @Override
            protected Integer call() {

                Platform.runLater(() -> {
                    try {
                        if (!DbAdapter.userExists(UsernameField.getText(), PasswordField.getText())) throw new FieldsDataException("User data wrong");
                        Stage stage = new Stage();
                        stage.setTitle("Main");
                        username = UsernameField.getText();

                        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/form.fxml"));
                        Pane myPane = myLoader.load();
                        FormController controller = myLoader.getController();
                        controller.setPrevStage(stage);

                        Scene scene = new Scene(myPane);
                        stage.setScene(scene);
                        prevStage.close();
                        stage.show();

                    } catch (FieldsDataException e){
                        new ErrorWindow(e.getMessage());
                    } catch (SQLException e) {
                        new ErrorWindow("Connection error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loginBox.setVisible(true);
                });
                return 100;
            }
        };
        progressIndicator.progressProperty().bind(loginTask.progressProperty());
        Main.executors.submit(loginTask);

    }

    @FXML
    void loginAsModeratorPressed() throws IOException{
        try{
            if (!DbAdapter.loginModerator(UsernameField.getText(), PasswordField.getText())) throw new FieldsDataException("User data wrong");

            Stage stage = new Stage();
            stage.setTitle("AppSettings");

            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/moder.fxml"));
            Pane myPane = myLoader.load();
            ModerController controller = myLoader.getController();
            controller.setPrevStage(stage);

            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.setResizable(false);
            prevStage.close();
            stage.show();
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        } catch (SQLException e) {
            new ErrorWindow("Connection error");
        }
    }

    @FXML
    void showHistoryButtonPressed() throws IOException{
        try{
            if (!DbAdapter.userExists(UsernameField.getText(), PasswordField.getText())) throw new FieldsDataException("User data wrong");
            username=UsernameField.getText();
            Stage stage = new Stage();
            stage.setTitle("Main");

            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/history.fxml"));
            Pane myPane = myLoader.load();
            HistoryController controller = myLoader.getController();
            controller.setPrevStage(stage);

            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.setResizable(false);
            prevStage.close();
            stage.show();
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        } catch (SQLException e) {
            new ErrorWindow("Connection error");
        }
    }
}