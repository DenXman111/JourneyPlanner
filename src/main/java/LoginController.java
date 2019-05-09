import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private Stage prevStage;

    @FXML
    private TextField UsernameField;

    @FXML
    private TextField PasswordField;

    @FXML
    private Button LoginButton;

    @SuppressWarnings("WeakerAccess")
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @FXML
    void findButtonPressed(ActionEvent event) {
    }

}