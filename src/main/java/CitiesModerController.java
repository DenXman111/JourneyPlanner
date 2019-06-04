import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CitiesModerController implements Initializable {
    public TableView cityTable;
    public TableView stopsTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.MODER);
    }
}
