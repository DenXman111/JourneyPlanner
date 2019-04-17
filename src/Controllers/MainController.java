package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

class CheckException extends Exception{
    private String massage;

    public CheckException(String massage){
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }
}

public class MainController {
    @FXML
    private TextField MainFieldName;

    @FXML
    private TextField MainFieldSurname;

    @FXML
    private TextField MainFieldCity;

    @FXML
    private TextField MainFieldFunds;

    @FXML
    private DatePicker MainFieldFirstDate;

    @FXML
    private DatePicker MainFieldSecondDate;

    @FXML
    private Button MainButtonFind;

    private void check() throws CheckException{
        System.out.println(MainFieldName.getCharacters());
        System.out.println(MainFieldSurname.getCharacters());
        System.out.println(MainFieldCity.getCharacters());
        try{
            System.out.println(Integer.valueOf(MainFieldFunds.getText()));
        } catch (RuntimeException e){
            e.getStackTrace();
            throw new CheckException("Write number to Funds");
        }
        System.out.println(MainFieldFirstDate.getValue());
        System.out.println(MainFieldSecondDate.getValue());

        if (LocalDate.now().isAfter(MainFieldFirstDate.getValue())) throw new CheckException("Start date should be in future");
        if (MainFieldFirstDate.getValue().isAfter(MainFieldSecondDate.getValue())) throw new CheckException("Ending date before start date");
    }
    @FXML
    void findButtonPressed(ActionEvent event) {
        try{
            check();

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
