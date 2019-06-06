import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    public ProgressIndicator indicator;

    public VBox signUpBox;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private Button signUpButton;

    @SuppressWarnings("unused")
    @FXML
    void signUpButtonPressed(ActionEvent event) {
        try{
            if (usernameField.getText().length() == 0) throw new FieldsDataException("Username field is empty");
            if (passwordField.getText().length() == 0) throw new FieldsDataException("Password field is empty");
            if (emailField.getText().length() == 0) throw new FieldsDataException("Email field is empty");
            if (nameField.getText().length() == 0) throw new FieldsDataException("Name field is empty");
            if (surnameField.getText().length() == 0) throw new FieldsDataException("Surname field is empty");

            TextField [] fields = {usernameField, passwordField, emailField, nameField, surnameField};
            for (TextField field : fields){
                if (!TextChecker.noWhiteCharactersOrQuotes(field.getText()))
                    throw new FieldsDataException("Do not use white characters or quotes\n" + field.getText());
            }

            signUpButton.setDisable(true);
            signUpBox.setVisible(false);

            Task<Integer> singUpTask = new Task<Integer>() {
                @Override
                protected Integer call() {
                    try {
                        DbAdapter.addNewUser(
                                usernameField.getText(),
                                passwordField.getText(),
                                emailField.getText(),
                                nameField.getText(),
                                surnameField.getText()
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> {
                            signUpBox.setVisible(true);
                            signUpButton.setDisable(false);
                            new ErrorWindow("User with this username or email already exists");
                        });
                        return 0;
                    }
                    Platform.runLater( () -> {
                        try {
                            StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    return 100;
                }
            };

            indicator.progressProperty().bind(singUpTask.progressProperty());
            Main.executors.submit(singUpTask);

        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException{
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }
}
