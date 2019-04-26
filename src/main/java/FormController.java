import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

class CheckException extends Exception{
    private String massage;

    CheckException(String massage){
        this.massage = massage;
    }

    String getMassage() {
        return massage;
    }
}

public class FormController {
    final static boolean debugMode = true; //debugMode flag

    @FXML
    private TextField MainFieldName;

    @FXML
    private TextField MainFieldSurname;

    @FXML
    private TextField MainFieldCity;

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

    private void check() throws CheckException{
        if (MainFieldName.getText().isEmpty()) throw new CheckException("Write your name");
        if (MainFieldSurname.getText().isEmpty()) throw new CheckException("Write your surname");
        if (MainFieldCity.getText().isEmpty()) throw new CheckException("Write your city");
        if (MainFieldFunds.getText().isEmpty()) throw new CheckException("Write your funds");
        if (MainFieldStartDate.getValue() == null) throw new CheckException("Fill start date");
        if (MainFieldEndingDate.getValue() == null) throw new CheckException("Fill ending date");
//        System.out.println(MainFieldName.getCharacters());
//        System.out.println(MainFieldSurname.getCharacters());
//        System.out.println(MainFieldCity.getCharacters());
        try{
            Integer.valueOf(MainFieldFunds.getText());
//            System.out.println(Integer.valueOf(MainFieldFunds.getText()));
        } catch (RuntimeException e){
            e.getStackTrace();
            throw new CheckException("Should be number in Funds");
        }
//        System.out.println(MainFieldStartDate.getValue());
//        System.out.println(MainFieldEndingDate.getValue());

        if (LocalDate.now().isAfter(MainFieldStartDate.getValue())) throw new CheckException("Start date should be in future");
        if (MainFieldStartDate.getValue().isAfter(MainFieldEndingDate.getValue())) throw new CheckException("Ending date before start date");
    }
    @FXML
    void findButtonPressed(ActionEvent event) {
        try {
            if (!debugMode) check(); //without checking for debug

            List<? extends Trip> propositions = Planner.plan("Krakow", 100, MainFieldStartDate.getValue(), MainFieldEndingDate.getValue());
            assert propositions != null;
            answersVBox.getChildren().clear();
            propositions.stream().
                    map(Trip::display).
                    forEach(node -> {
                        answersVBox.getChildren().add(node);
                        VBox.setMargin(node, new Insets(20, 10, 10, 20));
                    });

        } catch (CheckException e){
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
