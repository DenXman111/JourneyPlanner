import ch.qos.logback.classic.db.DBAppender;
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
    private ChoiceBox<String> CityCChoiceBox;

    @FXML
    private ChoiceBox<String> CityDChoiceBox;

    @FXML
    private TextField SeatPlacesField;

    @FXML
    private TextField PriceField;

    @FXML
    private DatePicker DepartureDate;

    @FXML
    private DatePicker ArrivalDate;

    @FXML
    private DatePicker DepartureDate2;

    @FXML
    private DatePicker ArrivalDate2;

    @FXML
    private Button AddNewBusButton;

    @FXML
    private TextField BusNumberField;

    @FXML
    private Button DeleteByIDButton;

    @FXML
    private Button DeleteByParametersButton;
    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> observableCitiesList = null;
        try {
            observableCitiesList = FXCollections.observableArrayList(DbAdapter.getCityList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CityAChoiceBox.setItems(observableCitiesList);
        CityBChoiceBox.setItems(observableCitiesList);
        CityCChoiceBox.setItems(observableCitiesList);
        CityDChoiceBox.setItems(observableCitiesList);
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
    void deleteBusByIDPressed(ActionEvent event) {
        try{
            if (BusNumberField.getText().length() == 0) throw new FieldsDataException("Bus number field is empty");
            if (!BusNumberField.getText().matches("\\d+")) throw new FieldsDataException("Write number to bus number field");
            DbAdapter.removeBusByID(Integer.valueOf(BusNumberField.getText()));
            new ErrorWindow("Deleted!");
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            try {
                DbAdapter.removeReservationsByID(Integer.valueOf(BusNumberField.getText()));
                DbAdapter.removeBusByID(Integer.valueOf(BusNumberField.getText()));
            }
            catch(Exception f){
                new ErrorWindow("Bus in Reservation");
            }

        }
        catch (Exception e)
        {
            new ErrorWindow("Wrong bus ID");
        }
    }
    @FXML
    void deleteBusByParametersPressed(ActionEvent event) {
        try{
            if (CityCChoiceBox.getValue() == null) throw new FieldsDataException("Set departure city");
            if (CityDChoiceBox.getValue() == null) throw new FieldsDataException("Set arrival city");
            if (DepartureDate2.getValue() == null) throw new FieldsDataException("Departure is empty");
            if (ArrivalDate2.getValue() == null) throw new FieldsDataException("Arrival date is empty");
            DbAdapter.removeBusByParameters(DbAdapter.getCityID(CityCChoiceBox.getValue()), DbAdapter.getCityID(CityDChoiceBox.getValue()),DepartureDate2.getValue(), ArrivalDate2.getValue());
            new ErrorWindow("Deleted!");
        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e) {
            try{
            int idd=DbAdapter.getIDFromParameters(DbAdapter.getCityID(CityCChoiceBox.getValue()), DbAdapter.getCityID(CityDChoiceBox.getValue()),DepartureDate2.getValue(), ArrivalDate2.getValue());
            DbAdapter.removeReservationsByID(idd);
            DbAdapter.removeBusByID(idd);
            } catch (Exception f){
                new ErrorWindow("Bus in Reservation");
            }
        }
        catch (Exception e)
        {
            new ErrorWindow("Wrong bus parameters");
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
