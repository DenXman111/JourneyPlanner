import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModerController implements Initializable{
    private Stage prevStage;
    @FXML
    private Button ReturnButton;

    @FXML
    private ChoiceBox<String> CityAChoiceBox;

    @FXML
    private ChoiceBox<String> CityBChoiceBox;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> observableCitiesList = FXCollections.observableArrayList(DbAdapter.getCityList());
        CityAChoiceBox.setItems(observableCitiesList);
        CityBChoiceBox.setItems(observableCitiesList);
    }


        @FXML
    void addNewBusPressed(ActionEvent event) {
        try{
            if (CityAChoiceBox.getValue() == null) throw new FieldsDataException("Set departure city");
            if (CityBChoiceBox.getValue() == null) throw new FieldsDataException("Set arrival city");
            if (SeatPlacesField.getText().length() == 0) throw new FieldsDataException("Seat places field is empty");
            if (PriceField.getText().length() == 0) throw new FieldsDataException("Price field is empty");
            if (DepartureDate.getValue() == null) throw new FieldsDataException("Departure is empty");
            if (ArrivalDate.getValue() == null) throw new FieldsDataException("Arrival date is empty");

            if (CityAChoiceBox.getValue().equals(CityBChoiceBox.getValue())) throw new FieldsDataException("Set different departure and arrival cites");
            if (!SeatPlacesField.getText().matches("\\d+")) throw new FieldsDataException("Write number to seat places field");
            if (!PriceField.getText().matches("\\d+")) throw new FieldsDataException("Write number to price field");
            if (DepartureDate.getValue().isBefore(LocalDate.now())) throw new FieldsDataException("Departure date shouldn't be in past");
            if (ArrivalDate.getValue().isBefore(DepartureDate.getValue())) throw new FieldsDataException("Arrival date shouldn't be before departure date");

            DbAdapter.addNewBus(CityAChoiceBox.getValue(), CityBChoiceBox.getValue(), Integer.valueOf(PriceField.getText()), DepartureDate.getValue(), ArrivalDate.getValue(), Integer.valueOf(SeatPlacesField.getText()));
            new ErrorWindow("Added!");
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            new ErrorWindow("Can't add this bus");
        }
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
