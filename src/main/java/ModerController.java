import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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

    @FXML
    private TextField BusNumberField;

    @FXML
    private Button DeleteByIDButton;

    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> observableCitiesList = null;
        try {
            observableCitiesList = FXCollections.observableArrayList(DbAdapter.getCityNamesList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CityAChoiceBox.setItems(observableCitiesList);
        CityBChoiceBox.setItems(observableCitiesList);
    }


    @FXML
    void addNewBusPressed() {
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
    void deleteBusByIDPressed() {
        try{
            if (BusNumberField.getText().length() == 0) throw new FieldsDataException("Bus number field is empty");
            if (!BusNumberField.getText().matches("\\d+")) throw new FieldsDataException("Write number to bus number field");

            DbAdapter.removeBusByID(Integer.valueOf(BusNumberField.getText()));
            new ErrorWindow("Deleted!");
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            new ErrorWindow("Wrong bus ID");
        }
    }

    @FXML
    void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }

    public void ManageCitiesClicked() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.CITIES_MODER);
    }
}
