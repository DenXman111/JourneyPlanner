import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CitiesModerController implements Initializable {
    public TableView<City> cityTable;
    public TableView<BusStop> stopsTable;
    public TableColumn<City, Integer> cityIdColumn;
    public TableColumn<City, String> cityNameColumn;
    public TableColumn<City, String> cityCountryColumn;
    public TableColumn<City, Double> cityRatingColumn;
    public TableColumn<City, Integer> cityNightPriceColumn;

    public TableColumn<BusStop, Integer> stopIdColumn;
    public TableColumn<BusStop, String> stopNameColumn;
    public TableColumn<BusStop, String> stopCityColumn;
    public TableColumn<BusStop, Integer> stopCityIdColumn;
    public VBox vBox;
    public ProgressBar indicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cityIdColumn.setCellValueFactory(new PropertyValueFactory<>("cityID"));
        cityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        cityRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        cityNightPriceColumn.setCellValueFactory(new PropertyValueFactory<>("nightPrice"));

        stopIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stopNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stopCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        stopCityIdColumn.setCellValueFactory(new PropertyValueFactory<>("cityId"));

        Task<Integer> downloadTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                try {
                    List<City> cityList = DbAdapter.getCitiesList();
                    List<BusStop> stopList = DbAdapter.getBusStopsList();
                    Platform.runLater(() -> {
                        displayCitiesTable(cityList);
                        displayStopsTable(stopList);
                        indicator.setVisible(false);
                    });
                } catch (Exception e){
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        vBox.getChildren().clear();
                        Label failLabel = new Label("fail to connect");
                        failLabel.getStyleClass().add("red-message-label");
                        vBox.getChildren().add(failLabel);
                    });
                }
                return 100;
            }
        };

        indicator.progressProperty().bind(downloadTask.progressProperty());
        Main.executors.submit(downloadTask);
    }

    private void displayCitiesTable(List<City> cityList){
        cityList.forEach(city -> cityTable.getItems().add(city));
    }

    private void displayStopsTable(List<BusStop> stopsList){
        stopsList.forEach(stop -> stopsTable.getItems().add(stop));
    }

    public void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.MODER);
    }
}
