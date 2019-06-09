import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LinesController implements Initializable {
    public TableView<Line> lineTable;
    public TableColumn<Line, Integer> lineIdColumn;
    public TableColumn<Line, String> lineStartStopColumn;
    public TableColumn<Line, String> lineEndStopColumn;

    private static List<Line> lines;

    public static void setLines(List<Line> lines){
        LinesController.lines = lines;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lineIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lineStartStopColumn.setCellValueFactory(new PropertyValueFactory<>("startStopName"));
        lineEndStopColumn.setCellValueFactory(new PropertyValueFactory<>("endStopName"));

        if (lines != null){
            lineTable.getItems().addAll(lines);
        }
    }
}
