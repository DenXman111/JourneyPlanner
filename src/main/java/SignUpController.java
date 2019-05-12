import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    private Stage prevStage;

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

    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

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
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

            Pane myPane = myLoader.load();
            WelcomeController controller = myLoader.getController();
            controller.setPrevStage(prevStage);

            Scene myScene = new Scene(myPane);
            prevStage.setScene(myScene);

        } catch (SQLException e){
            new ErrorWindow("User with this username or email already exists");
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(prevStage);

        Scene myScene = new Scene(myPane);
        prevStage.setScene(myScene);
    }
}
