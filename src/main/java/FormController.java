import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
    private VBox answersVBox;

    void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(LoginController.username);
        ObservableList<String> observableCitiesList = FXCollections.observableArrayList(DbAdapter.getCityList());
        MainCityChoiceBox.setItems(observableCitiesList);

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

            List<Trip> propositions = Planner.
                    plan(   MainCityChoiceBox.getValue(),
                            Integer.valueOf(MainFieldFunds.getText()),
                            MainFieldStartDate.getValue(),
                            MainFieldEndingDate.getValue() );
            assert propositions != null;
            answersVBox.getChildren().clear();
            propositions.stream().
                    map(Trip::display).
                    forEach(node -> {
                        answersVBox.getChildren().add(node);
                        VBox.setMargin(node, new Insets(20, 10, 10, 20));
                    });

            MainButtonFind.setPrefWidth(170);

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
}
