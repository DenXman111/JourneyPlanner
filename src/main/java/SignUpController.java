import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField UsernameField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField NameField;

    @FXML
    private TextField SurnameField;

    @SuppressWarnings("unused")
    @FXML
    private Button SignUpButton;

    @SuppressWarnings("unused")
    @FXML
    private Button ReturnButton;

    @SuppressWarnings("unused")
    @FXML
    void signUpButtonPressed(ActionEvent event) throws IOException{
        try{
            if (UsernameField.getText().length() == 0) throw new FieldsDataException("Username field is empty");
            if (PasswordField.getText().length() == 0) throw new FieldsDataException("Password field is empty");
            if (EmailField.getText().length() == 0) throw new FieldsDataException("Email field is empty");
            if (NameField.getText().length() == 0) throw new FieldsDataException("Name field is empty");
            if (SurnameField.getText().length() == 0) throw new FieldsDataException("Surname field is empty");

            DbAdapter.addNewUser(UsernameField.getText(), PasswordField.getText(), EmailField.getText(), NameField.getText(), SurnameField.getText());

            StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);

        } catch (SQLException e){
            e.printStackTrace();
            new ErrorWindow("User with this username or email already exists");
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
