import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
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
    private ChoiceBox<String> CityCChoiceBox;

    @FXML
    private ChoiceBox<String> CityDChoiceBox;

    @FXML
    private DatePicker BeginDate;

    @FXML
    private DatePicker EndDate;

    @FXML
    private TextField SeatPlacesField;

    @FXML
    private TextField DepartureStop;

    @FXML
    private TextField ArrivalStop;

    @FXML
    private TextField BusType;

    @FXML
    private TextField TransitField;

    @FXML
    private TextField PriceField;

    @FXML
    private Button AddNewLineButton;

    @FXML
    private Button AssignSpanToLineButton;

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
    }


    @FXML
    void addNewLinePressed(){
        try{
            if (ArrivalStop.getText().length() == 0) throw new FieldsDataException("Arrival stop field is empty");
            if (DepartureStop.getText().length() == 0) throw new FieldsDataException("Departure stop field is empty");
            if (PriceField.getText().length() == 0) throw new FieldsDataException("Price field is empty");
            if (BusType.getText().length() == 0) throw new FieldsDataException("Bus type field is empty");
            if (ArrivalStop.getText().equals(DepartureStop.getText())) throw new FieldsDataException("Set different departure and arrival stops");
            if (!ArrivalStop.getText().matches("\\d+")) throw new FieldsDataException("Write number to arrival stop field");
            if (!DepartureStop.getText().matches("\\d+")) throw new FieldsDataException("Write number to departure stop field");
            if (!PriceField.getText().matches("\\d+")) throw new FieldsDataException("Write number to price field");
            if (!BusType.getText().matches("\\d+")) throw new FieldsDataException("Write number to bus type field");
            if (Integer.valueOf(PriceField.getText())<=0) throw new FieldsDataException("Price must be greater than 0");
            if (Integer.valueOf(ArrivalStop.getText())<=0) throw new FieldsDataException("Arrival stop id must be greater than 0");
            if (Integer.valueOf(DepartureStop.getText())<=0) throw new FieldsDataException("Departure stop id must be greater than 0");
            if (Integer.valueOf(BusType.getText())<=0) throw new FieldsDataException("Departure stop id must be greater than 0");
            DbAdapter.addNewLine(Integer.valueOf(ArrivalStop.getText()),Integer.valueOf(DepartureStop.getText()),Integer.valueOf(PriceField.getText()),Integer.valueOf(BusType.getText()));
            new ErrorWindow("Added!");
        }
        catch(FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            new ErrorWindow("Can't add this bus");
        }
    }
    @FXML
    void assignSpanToLine(){
        try{
            if (TransitField.getText().length() == 0) throw new FieldsDataException("No transit chosen");
            if (BeginDate.getValue() == null) throw new FieldsDataException("Begin date is empty");
            if (EndDate.getValue() == null) throw new FieldsDataException("End date is empty");
            if (!TransitField.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            DbAdapter.assignSpanToLine(Integer.valueOf(TransitField.getText()),BeginDate.getValue(),EndDate.getValue());
        }
        catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            new ErrorWindow("Can't assign this span");
        }
    }
    @FXML
    void deleteBusByParametersPressed() {
        try{
            if (CityCChoiceBox.getValue() == null) throw new FieldsDataException("Set departure city");
            if (CityDChoiceBox.getValue() == null) throw new FieldsDataException("Set arrival city");
            new ErrorWindow("Deleted!");
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (Exception e)
        {
            new ErrorWindow("Wrong bus parameters");
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
