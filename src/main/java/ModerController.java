import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ModerController {
    private Stage prevStage;
    @FXML
    private Button ReturnButton;

    @FXML
    private ChoiceBox<?> CityAChoiceBox;

    @FXML
    private ChoiceBox<?> CityBChoiceBox;

    @FXML
    private TextField SeatPlacesField;

    @FXML
    private TextField PriceField;

    @FXML
    private DatePicker DepartureDate;

    @FXML
    private DatePicker ArrivalDate;

    @FXML
    private Button AddNewBusButton;

    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @FXML
    void addNewBusPressed(ActionEvent event) {

    }

    @FXML
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
