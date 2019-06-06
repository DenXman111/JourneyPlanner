import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
    public Button returnButton;
    public Button moderLoginButton;
    public Button showHistoryButton;
    public Button loginButton;
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
    void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }

    @FXML
    void loginButtonPressed(ActionEvent actionEvent ){

        loginBox.setVisible(false);
        Task<Integer> loginTask = new Task<Integer>() {
            @Override
            protected Integer call() throws SQLException {
                boolean loginResult = DbAdapter.userExists(UsernameField.getText(), PasswordField.getText());
                Platform.runLater(() -> {
                    try {
                        if (!loginResult) throw new FieldsDataException("User data wrong");
                        Stage stage = new Stage();
                        stage.setTitle("Main");
                        username = UsernameField.getText();

                        if (actionEvent.getSource().equals(loginButton))
                            StageChanger.changeStage(StageChanger.ApplicationStage.FORM);
                        else
                            StageChanger.changeStage(StageChanger.ApplicationStage.HISTORY);

                    } catch (FieldsDataException e){
                        new ErrorWindow(e.getMessage());
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
    void loginAsModeratorPressed() {
        loginBox.setVisible(false);
        Task<Integer> loginTask = new Task<Integer>() {
            @Override
            protected Integer call() throws SQLException {
                boolean loginResult = DbAdapter.loginModerator(UsernameField.getText(), PasswordField.getText());
                Platform.runLater(() -> {
                    try {
                        if (!loginResult) throw new FieldsDataException("User data wrong");
                        Stage stage = new Stage();
                        stage.setTitle("Main");
                        username = UsernameField.getText();

                        StageChanger.changeStage(StageChanger.ApplicationStage.MODER);

                    } catch (FieldsDataException e){
                        new ErrorWindow(e.getMessage());
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
}