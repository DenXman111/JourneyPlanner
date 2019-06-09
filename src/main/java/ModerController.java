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
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class ModerController implements Initializable{
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
    private DatePicker DBeginDate;

    @FXML
    private DatePicker DEndDate;

    @FXML
    private DatePicker ExceptionDate;

    @FXML
    private DatePicker DExceptionDate;

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
    private TextField DTransitField;

    @FXML
    private TextField DDTransitField;

    @FXML
    private TextField PriceField;

    @FXML
    private TextField DepartureTime;

    @FXML
    private TextField DDepartureTime;

    @FXML
    private TextField Duration;

    @FXML
    private TextField DDuration;

    @FXML
    private TextField DepartureSpan;

    @FXML
    private TextField DDepartureSpan;

    @FXML
    private TextField Weekday;

    @FXML
    private TextField DWeekday;

    @FXML
    private TextField ExceptionSpan;

    @FXML
    private TextField DExceptionSpan;

    @FXML
    private Button AddNewLineButton;

    @FXML
    private Button AssignSpanToLineButton;

    @FXML
    private Button AssignDepartureTimeButton;

    @FXML
    private Button AddBreakButton;

    @FXML
    private Button DeleteBreakButton;

    @FXML
    private Button DeleteDepartureTimeButton;

    @FXML
    private Button DeleteSpanFromLineButton;

    @FXML
    private Button DeleteLineButton;

    @FXML
    private Button ShowSpansFromLineButton;

    void setPrevStage(Stage stage){
    }

    @SuppressWarnings("UnusedAssignment")
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
            if (BusType.getText().matches(".*'.*")||BusType.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (ArrivalStop.getText().matches(".*'.*")||ArrivalStop.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (PriceField.getText().matches(".*'.*")||PriceField.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (DepartureStop.getText().matches(".*'.*")||DepartureStop.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (Integer.valueOf(PriceField.getText())<=0) throw new FieldsDataException("Price must be greater than 0");
            if (Integer.valueOf(ArrivalStop.getText())<=0) throw new FieldsDataException("Arrival stop id must be greater than 0");
            if (Integer.valueOf(DepartureStop.getText())<=0) throw new FieldsDataException("Departure stop id must be greater than 0");
            if (Integer.valueOf(BusType.getText())<=0) throw new FieldsDataException("Departure stop id must be greater than 0");
            DbAdapter.addNewLine(Integer.valueOf(DepartureStop.getText()),Integer.valueOf(ArrivalStop.getText()),Integer.valueOf(PriceField.getText()),Integer.valueOf(BusType.getText()));
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
            if (TransitField.getText().matches(".*'.*")||TransitField.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            DbAdapter.assignSpanToLine(Integer.valueOf(TransitField.getText()),BeginDate.getValue(),EndDate.getValue());
            new ErrorWindow("Added!");
        }
            catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            new ErrorWindow("Wrong input data!");
        }
    }

    @FXML
    void addDepartureTime(){
        try{
            if (DepartureTime.getText().length() == 0) throw new FieldsDataException("No departure time");
            if (Duration.getText() == null) throw new FieldsDataException("No duration");
            if (DepartureSpan.getText() == null) throw new FieldsDataException("No transit chosen");
            if (Weekday.getText() == null) throw new FieldsDataException("No day chosen");
            if (DepartureTime.getText().matches(".*'.*")||DepartureTime.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (Duration.getText().matches(".*'.*")||Duration.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (DepartureSpan.getText().matches(".*'.*")||DepartureSpan.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (Weekday.getText().matches(".*'.*")||Weekday.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (!DepartureSpan.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            DbAdapter.addDepartureTime(DepartureTime.getText(),Duration.getText(),Integer.valueOf(DepartureSpan.getText()),"\'"+Weekday.getText()+"\'");
            new ErrorWindow("Added!");
        }
        catch (FieldsDataException | SQLException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void addBreak(){
        try{
            if (ExceptionSpan.getText().length() == 0) throw new FieldsDataException("No line chosen");
            if (!ExceptionSpan.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            if (ExceptionSpan.getText().matches(".*'.*")||ExceptionSpan.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (ExceptionDate.getValue() == null) throw new FieldsDataException("Pick a date");
            DbAdapter.addBreak(Integer.valueOf(ExceptionSpan.getText()),ExceptionDate.getValue());
            new ErrorWindow("Added!");
        }
        catch(FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
        catch (SQLException e){
            new ErrorWindow("Wrong input data!");
        }
    }

    @FXML
    void deleteBreak(){
        try{
            if (DExceptionSpan.getText().length() == 0) throw new FieldsDataException("No line chosen");
            if (!DExceptionSpan.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            if (DExceptionSpan.getText().matches(".*'.*")||DExceptionSpan.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (DExceptionDate.getValue() == null) throw new FieldsDataException("Pick a date");
            if(DbAdapter.deleteBreak(Integer.valueOf(DExceptionSpan.getText()),DExceptionDate.getValue()))
            new ErrorWindow("Deleted!");
            else throw new FieldsDataException("No such break!");

        }
        catch(FieldsDataException | SQLException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void deleteDepartureTime(){
        try{
            if (DDepartureTime.getText().length() == 0) throw new FieldsDataException("No departure time");
            if (DDuration.getText() == null) throw new FieldsDataException("No duration");
            if (DDepartureSpan.getText() == null) throw new FieldsDataException("No transit chosen");
            if (DWeekday.getText() == null) throw new FieldsDataException("No day chosen");
            if (DDepartureTime.getText().matches(".*'.*")||DDepartureTime.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (DDuration.getText().matches(".*'.*")||DDuration.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (DDepartureSpan.getText().matches(".*'.*")||DDepartureSpan.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (DWeekday.getText().matches(".*'.*")||DWeekday.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (!DDepartureSpan.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            if(DbAdapter.deleteDepartureTime(DDepartureTime.getText(),DDuration.getText(),Integer.valueOf(DDepartureSpan.getText()),"\'"+DWeekday.getText()+"\'"))
            new ErrorWindow("Deleted!");
            else throw new FieldsDataException("No such departure time!");
        }
        catch (FieldsDataException | SQLException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void deleteSpanFromLine(){
        try{
            if (DTransitField.getText().length() == 0) throw new FieldsDataException("No line chosen");
            if (DBeginDate.getValue() == null) throw new FieldsDataException("Begin date is empty");
            if (DEndDate.getValue() == null) throw new FieldsDataException("End date is empty");
            if (DTransitField.getText().matches(".*'.*")||DTransitField.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (!DTransitField.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            if(DbAdapter.deleteSpanFromLine(Integer.valueOf(DTransitField.getText()),DBeginDate.getValue(),DEndDate.getValue()))
            new ErrorWindow("Deleted!");
            else throw new FieldsDataException("No such span!");
        }
        catch (FieldsDataException | SQLException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void deleteLine(){
        try {
            if (DDTransitField.getText().length() == 0) throw new FieldsDataException("No line chosen");
            if (DDTransitField.getText().matches(".*'.*")||DDTransitField.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (!DDTransitField.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            DbAdapter.removeTransitByID(Integer.valueOf(DDTransitField.getText()));
            new ErrorWindow("Deleted!");
        }
        catch (FieldsDataException | SQLException e) {
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void ShowLines(){
        try{
            List<Line> lines = DbAdapter.getLines();
            StageChanger.displayLinesWindow(lines);
        }
        catch(Exception e) {
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void ShowSpansFromLine(){
        try {
            if (DTransitField.getText().length() == 0) throw new FieldsDataException("No line chosen");
            if (DTransitField.getText().matches(".*'.*")|| DTransitField.getText().matches(".*;.*") ) throw new FieldsDataException("Don't use special characters");
            if (!DTransitField.getText().matches("\\d+")) throw new FieldsDataException("Write a number to line field");
            String spans=DbAdapter.getSpans(Integer.valueOf(DTransitField.getText()));
            new LinesWindow(spans);
        }
        catch (FieldsDataException | SQLException e){
            new ErrorWindow(e.getMessage());
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
