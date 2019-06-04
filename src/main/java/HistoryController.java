import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    private Stage prevStage;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Button ReturnButton;

    @FXML
    private VBox answersVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Task<Integer> downloadReservations = new Task<Integer>() {
            @Override
            protected Integer call() {

                try{
                    Trip.displayBookButton = false;
                    List<PastTrip> history;
                    history = DbAdapter.getHistory(LoginController.username);
                    history.forEach(pastTrip -> pastTrip.trip.countRating());

                    List<PastTrip> finalHistory = history;
                    Platform.runLater(() -> {
                        answersVBox.getChildren().clear();
                        finalHistory.stream().
                            map(PastTrip::display).
                            forEach(node -> {
                                answersVBox.getChildren().add(node);
                                VBox.setMargin(node, new Insets(20, 10, 10, 20));
                            });
                    });
                } catch (Exception e){
                    e.printStackTrace();
                    Label messageLabel = new Label("No trips found");
                    messageLabel.getStyleClass().add("message-label");
                    VBox.setMargin(messageLabel, new Insets(0, 0, 0, 100));
                    answersVBox.getChildren().add(messageLabel);
                }
                return 100;
            }
        };

        progressIndicator.progressProperty().bind(downloadReservations.progressProperty());
        Main.executors.submit(downloadReservations);
    }

    @FXML
    @SuppressWarnings("Duplicates")
    void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }
}
