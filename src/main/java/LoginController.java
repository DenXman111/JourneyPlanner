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

public class LoginController {
    private Stage prevStage;
    static String username = null;

    @FXML
    private TextField UsernameField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private Button ReturnButton;

    @FXML
    private Button ModerLoginButton;

    @FXML
    private Button LoginButton;

    @FXML
    private Button ShowHistoryButton;

    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException{
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(prevStage);

        Scene myScene = new Scene(myPane);
        prevStage.setScene(myScene);
    }

    @FXML
    @SuppressWarnings("Duplicates")
    void findButtonPressed(ActionEvent event) throws IOException{
        try{
            if (!DbAdapter.haveUser(UsernameField.getText(), PasswordField.getText())) throw new FieldsDataException("User data wrong");

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
        }
    }

    @FXML
    void loginAsModeratorPressed(ActionEvent event) throws IOException{
        try{
            if (!DbAdapter.haveModer(UsernameField.getText(), PasswordField.getText())) throw new FieldsDataException("User data wrong");

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
        }
    }

    @FXML
    void showHistoryButtonPressed(ActionEvent event) throws IOException{
        try{
            if (!DbAdapter.haveUser(UsernameField.getText(), PasswordField.getText())) throw new FieldsDataException("User data wrong");
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
        }
    }
}