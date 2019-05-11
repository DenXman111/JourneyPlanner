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

    @FXML
    private Button SignUp;

    @FXML
    private Button Return;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @FXML
    void signUpPressed(ActionEvent event) {
        try{
            DbAdapter.addNewUser(UsernameField.getText(), PasswordField.hashCode(), EmailField.getText(), NameField.getText(), SurnameField.getText());
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

            Pane myPane = myLoader.load();
            WelcomeController controller = myLoader.getController();
            controller.setPrevStage(prevStage);

            Scene myScene = new Scene(myPane);
            prevStage.setScene(myScene);

        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            //Show window with error
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void returnPressed(ActionEvent event) {
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

            Pane myPane = myLoader.load();
            WelcomeController controller = myLoader.getController();
            controller.setPrevStage(prevStage);

            Scene myScene = new Scene(myPane);
            prevStage.setScene(myScene);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
