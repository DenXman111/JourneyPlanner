import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CitiesModerController implements Initializable {
    public TableView<CityRow> cityTable;
    public TableView<BusStop> stopsTable;
    public TableColumn<CityRow, Integer> cityIdColumn;
    public TableColumn<CityRow, TextField> cityNameColumn;
    public TableColumn<CityRow, TextField> cityCountryColumn;
    public TableColumn<CityRow, TextField> cityRatingColumn;
    public TableColumn<CityRow, TextField> cityNightPriceColumn;
    public TableColumn<CityRow, Button> citiesDeleteColumn;

    public TableColumn<BusStop, Integer> stopIdColumn;
    public TableColumn<BusStop, String> stopNameColumn;
    public TableColumn<BusStop, String> stopCityColumn;
    public TableColumn<BusStop, Integer> stopCityIdColumn;
    public VBox vBox;
    public ProgressBar indicator;

    private void downloadAndDisplay(){
        indicator.setVisible(true);
        cityTable.setVisible(false);
        stopsTable.setVisible(false);
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
                        cityTable.setVisible(true);
                        stopsTable.setVisible(true);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cityIdColumn.setCellValueFactory(new PropertyValueFactory<>("cityID"));
        cityNameColumn.setCellValueFactory(new PropertyValueFactory<>("nameField"));
        cityCountryColumn.setCellValueFactory(new PropertyValueFactory<>("countyField"));
        cityRatingColumn.setCellValueFactory(new PropertyValueFactory<>("ratingField"));
        cityNightPriceColumn.setCellValueFactory(new PropertyValueFactory<>("nightPriceField"));

        stopIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stopNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stopCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        stopCityIdColumn.setCellValueFactory(new PropertyValueFactory<>("cityId"));
        citiesDeleteColumn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

        downloadAndDisplay();
    }

    private void displayCitiesTable(List<City> cityList){
        Runnable runDisplay = this::downloadAndDisplay;
        cityTable.getItems().clear();
        cityList.forEach(city -> cityTable.getItems().add(new CityRow(city, runDisplay)));
    }

    private void displayStopsTable(List<BusStop> stopsList){
        stopsTable.getItems().clear();
        stopsList.forEach(stop -> stopsTable.getItems().add(stop));
    }

    public void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.MODER);
    }

    public void addCityClicked() {
        Main.executors.submit(new Task<Integer>() {
            @Override
            protected Integer call() {
                try {
                    DbAdapter.addNewCity();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Platform.runLater( () -> new ErrorWindow(e.getMessage()) );
                }
                Platform.runLater(CitiesModerController.this::downloadAndDisplay);
                return null;
            }
        });
    }
}
