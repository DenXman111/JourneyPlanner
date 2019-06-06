import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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
    public TableColumn<BusStop, TextField> stopNameColumn;
    public TableColumn<BusStop, ChoiceBox<City> > stopCityColumn;
    public TableColumn<BusStop, BusStop> stopDeleteColumn;

    public VBox vBox;
    public ProgressBar indicator;
    public Button addStopButton;
    public Button addCityButton;

    private void downloadAndDisplay(){
        indicator.setVisible(true);
        cityTable.setVisible(false);
        stopsTable.setVisible(false);
        addCityButton.setVisible(false);
        addStopButton.setVisible(false);
        Task<Integer> downloadTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                try {
                    List<City> cityList = DbAdapter.getCitiesList();

                    List<BusStop> stopList = DbAdapter.getBusStopsList();
                    ObservableList<City> observableList = FXCollections.observableArrayList(cityList);
                    stopList.forEach(stop -> {
                        stop.setAvailableCities(observableList);
                        stop.setUpdate(CitiesModerController.this::downloadAndDisplay);
                    });

                    Platform.runLater(() -> {
                        displayCitiesTable(cityList);
                        displayStopsTable(stopList);
                        indicator.setVisible(false);
                        cityTable.setVisible(true);
                        stopsTable.setVisible(true);
                        addCityButton.setVisible(true);
                        addStopButton.setVisible(true);
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
        citiesDeleteColumn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

        stopIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stopNameColumn.setCellValueFactory(new PropertyValueFactory<>("nameField"));
        stopCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityChoice"));
        stopDeleteColumn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));


        downloadAndDisplay();
    }

    private void displayCitiesTable(List<City> cityList){
        cityTable.getItems().clear();
        cityList.forEach(city ->
                cityTable.getItems().add(new CityRow(city, CitiesModerController.this::downloadAndDisplay))
        );
    }

    private void displayStopsTable(List<BusStop> stopsList){
        stopsTable.getItems().clear();
        stopsList.forEach(stop -> stopsTable.getItems().add(stop));
    }

    public void returnButtonPressed() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.MODER, false);
    }

    public void addCityClicked() {
        DbUpdate.execute(
                () -> {
                    DbAdapter.addNewCity();
                    return null;
                },
                CitiesModerController.this::downloadAndDisplay
        );
    }

    public void  addStopClicked() {
        DbUpdate.execute(
                () -> {
                    DbAdapter.addNewStop();
                    return null;
                },
                CitiesModerController.this::downloadAndDisplay
        );
    }
}
