import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    public Button ReturnButton;
    public Button ModerLoginButton;
    public Button ShowHistoryButton;
    public Button LoginButton;
    static String username = null;

    @FXML
    private TextField UsernameField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private VBox loginBox;

    @FXML
    void returnButtonPressed() throws IOException{
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }

    @FXML
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

                        StageChanger.changeStage(StageChanger.ApplicationStage.FORM);

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

            StageChanger.changeStage(StageChanger.ApplicationStage.MODER);
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
            StageChanger.changeStage(StageChanger.ApplicationStage.HISTORY);
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        } catch (SQLException e) {
            new ErrorWindow("Connection error");
        }
    }
}