import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormController implements Initializable {
    private Stage prevStage;

    @FXML
    private Label loginLabel;

    @FXML
    private ChoiceBox<String> MainCityChoiceBox;

    @FXML
    private TextField MainFieldFunds;

    @FXML
    private DatePicker MainFieldStartDate;

    @FXML
    private DatePicker MainFieldEndingDate;

    @FXML
    private Button MainButtonFind;

    @SuppressWarnings("unused")
    @FXML
    private Button ReturnButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private VBox answersVBox;

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
        MainCityChoiceBox.setItems(observableCitiesList);

        progressBar.setVisible(false);

        if (LoginController.username != null)
            loginLabel.setText("logged as " + LoginController.username);

    }

    private void check() throws FieldsDataException{
        if (MainCityChoiceBox.getSelectionModel().isEmpty()) throw new FieldsDataException("Choose any city");
        if (MainFieldFunds.getText().isEmpty()) throw new FieldsDataException("Write your funds");
        if (MainFieldStartDate.getValue() == null) throw new FieldsDataException("Fill start date");
        if (MainFieldEndingDate.getValue() == null) throw new FieldsDataException("Fill ending date");
        try{
            Integer.valueOf(MainFieldFunds.getText());
        } catch (RuntimeException e){
            e.getStackTrace();
            throw new FieldsDataException("Should be number in Funds");
        }

        if (LocalDate.now().isAfter(MainFieldStartDate.getValue()))
            throw new FieldsDataException("Start date should be in future");
        if (MainFieldStartDate.getValue().isAfter(MainFieldEndingDate.getValue()))
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
            Trip.setFormController(this);
            MainButtonFind.setPrefWidth(170);

            Planner planing = new Planner(MainCityChoiceBox.getValue(),
                    Integer.valueOf(MainFieldFunds.getText()),
                    MainFieldStartDate.getValue(),
                    MainFieldEndingDate.getValue());

            planing.setDisplayData(answersVBox, progressBar);

            progressBar.progressProperty().bind(planing.progressProperty());
            new Thread(planing).start();

        } catch (FieldsDataException e){
            new ErrorWindow(e.getMessage());
        }
    }

    @SuppressWarnings({"Duplicates", "unused"})
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

    @SuppressWarnings("WeakerAccess")
    protected static Trip tripToShowing;
    void showButtonPressed(Trip trip) throws IOException {
        tripToShowing = trip;

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/map.fxml"));
        Pane myPane = myLoader.load();

        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Map");
        stage.setScene(scene);
        stage.show();
    }
}
