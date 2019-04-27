import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class FormController implements Initializable {
    private final static boolean debugMode = false; //debugMode flag

    @FXML
    private TextField MainFieldName;

    @FXML
    private TextField MainFieldSurname;

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

    @FXML
    private VBox answersVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> observableCitiesList = FXCollections.observableArrayList(DbAdapter.getCityList());
        MainCityChoiceBox.setItems(observableCitiesList);
    }

    private void check() throws FormsCheckException{
        if (MainFieldName.getText().isEmpty()) throw new FormsCheckException("Write your name");
        if (MainFieldSurname.getText().isEmpty()) throw new FormsCheckException("Write your surname");
        if (MainCityChoiceBox.getSelectionModel().isEmpty()) throw new FormsCheckException("Choose any city");
        if (MainFieldFunds.getText().isEmpty()) throw new FormsCheckException("Write your funds");
        if (MainFieldStartDate.getValue() == null) throw new FormsCheckException("Fill start date");
        if (MainFieldEndingDate.getValue() == null) throw new FormsCheckException("Fill ending date");
//        System.out.println(MainFieldName.getCharacters());
//        System.out.println(MainFieldSurname.getCharacters());
//        System.out.println(MainFieldCity.getCharacters());
        try{
            Integer.valueOf(MainFieldFunds.getText());
//            System.out.println(Integer.valueOf(MainFieldFunds.getText()));
        } catch (RuntimeException e){
            e.getStackTrace();
            throw new FormsCheckException("Should be number in Funds");
        }
//        System.out.println(MainFieldStartDate.getValue());
//        System.out.println(MainFieldEndingDate.getValue());

        if (LocalDate.now().isAfter(MainFieldStartDate.getValue()))
            throw new FormsCheckException("Start date should be in future");
        if (MainFieldStartDate.getValue().isAfter(MainFieldEndingDate.getValue()))
            throw new FormsCheckException("Ending date before start date");
    }
    @FXML
    void findButtonPressed(ActionEvent event) {
        try {
            if (!debugMode) check(); //without checking for debug

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

        } catch (FormsCheckException e){
            Stage errorStage = new Stage();
            StackPane pane = new StackPane();
            Text text = new Text(e.getMassage());
            pane.getChildren().add(text);

            Scene scene = new Scene(pane, 200, 100);
            errorStage.setScene(scene);
            errorStage.setResizable(false);
            errorStage.show();
        }
    }
}
