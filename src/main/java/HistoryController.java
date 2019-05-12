import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    private Stage prevStage;

    @FXML
    private Button ReturnButton;
    @FXML
    private VBox answersVBox;
    @SuppressWarnings("WeakerAccess")
    protected void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Trip.displayBookButton = false;
        List<Trip> history = DbAdapter.getHistory(LoginController.username);
        assert history != null;
        answersVBox.getChildren().clear();
        history.stream().
                map(Trip::display).
                forEach(node -> {
                    answersVBox.getChildren().add(node);
                    VBox.setMargin(node, new Insets(20, 10, 10, 20));
                });
    }

    @FXML
    @SuppressWarnings("Duplicates")
    void returnButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));
        Pane myPane = myLoader.load();

        Stage stage = new Stage();
        stage.setTitle("JourneyPlanner");

        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(stage);
        Scene myScene = new Scene(myPane);

        stage.setScene(myScene);
        stage.setResizable(false);
        prevStage.close();
        stage.show();

    }
}
