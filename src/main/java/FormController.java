import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormController implements Initializable {

    @FXML
    private Label loginLabel;

    @FXML
    private ChoiceBox<String> mainCityChoiceBox;

    @FXML
    private TextField mainFieldFunds;

    @FXML
    private TextField seatsField;

    @FXML
    private DatePicker mainFieldStartDate;

    @FXML
    private DatePicker mainFieldEndingDate;

    @FXML
    private Button mainButtonFind;

    @SuppressWarnings("unused")
    @FXML
    private Button returnButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private VBox answersVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> observableCitiesList = null;
        try {
            observableCitiesList = FXCollections.observableArrayList(DbAdapter.getCityNamesList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainCityChoiceBox.setItems(observableCitiesList);
        mainCityChoiceBox.getSelectionModel().selectFirst();

        progressBar.setVisible(false);

        if (LoginController.username != null)
            loginLabel.setText("logged as " + LoginController.username);

    }

    private void check() throws FieldsDataException{
        if (mainCityChoiceBox.getSelectionModel().isEmpty()) throw new FieldsDataException("Choose any city");
        if (mainFieldFunds.getText().isEmpty()) throw new FieldsDataException("Write your funds");
        if (seatsField.getText().isEmpty()) throw new FieldsDataException("Specify number of seats");
        if (mainFieldStartDate.getValue() == null) throw new FieldsDataException("Fill start date");
        if (mainFieldEndingDate.getValue() == null) throw new FieldsDataException("Fill ending date");

        int seats;
        double founds;
        try{
            founds = Double.valueOf(mainFieldFunds.getText());
        } catch (RuntimeException e){
            throw new FieldsDataException("Should be number in Funds");
        }
        try{
            seats = Integer.valueOf(seatsField.getText());
        } catch (RuntimeException e){
            throw new FieldsDataException("Insert number in seats field");
        }
        if (founds <= 0) throw new FieldsDataException("Founds should be a positive number");
        if (seats <= 0) throw new FieldsDataException("Number of seats should be positive");

        if (LocalDate.now().isAfter(mainFieldStartDate.getValue()))
            throw new FieldsDataException("Start date should be in future");
        if (mainFieldStartDate.getValue().isAfter(mainFieldEndingDate.getValue()))
            throw new FieldsDataException("Ending date before start date");
    }
    @SuppressWarnings("unused")
    @FXML
    void findButtonPressed(ActionEvent event) {
        try {
            check(); //without checking for debug
            Trip.displayBookButton = true;
            progressBar.setVisible(true);
            answersVBox.getChildren().clear();
            mainButtonFind.setPrefWidth(170);

            Planner planing = new Planner(mainCityChoiceBox.getValue(),
                    Double.valueOf(mainFieldFunds.getText()),
                    Integer.valueOf(seatsField.getText()),
                    mainFieldStartDate.getValue(),
                    mainFieldEndingDate.getValue());

            planing.setDisplayData(answersVBox, progressBar);

            progressBar.progressProperty().bind(planing.progressProperty());
            Main.executors.submit(planing);

        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @FXML
    void returnButtonPressed() throws IOException {
        Main.restartDaemonExecutor();
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }
}
