import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BusStop {

    private Integer id;
    private String name;
    private City city;

    private TextField nameField;
    private Button deleteButton;
    private ChoiceBox<City> cityChoice;

    private Runnable update;

    BusStop (Integer id, String name, City city){
        this.id = id;
        this.name = name;
        this.city = city;

        this.nameField = new TextField(name);
        nameField.setOnAction(event -> updateStopInDataBase());

        this.deleteButton = new Button(" x ");
        deleteButton.getStyleClass().add("ratting-button");
        deleteButton.setOnMouseClicked(event -> this.deleteStopFromDataBase());
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setDisable(false);

        this.cityChoice = new ChoiceBox<>();
        cityChoice.setOnAction(event -> updateStopInDataBase());
        cityChoice.setDisable(false);
    }

    public void setAvailableCities(ObservableList<City> observableCitiesList){
        cityChoice.setItems(observableCitiesList);
        cityChoice.setValue(city);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public TextField getNameField() { return nameField; }

    public Button getDeleteButton() { return deleteButton; }

    public ChoiceBox<City> getCityChoice() { return cityChoice; }

    public void setModifiable(boolean modifiable) {
        cityChoice.setDisable(modifiable);
        deleteButton.setDisable(modifiable);
    }

    public void setUpdate(Runnable update) { this.update = update; }

    private boolean noChanges(){
        return name.equals(nameField.getText()) && city.equals(cityChoice.getValue());
    }

    private void updateStopInDataBase(){
        if (noChanges()) return;
        if (TextChecker.containsSpecialSigns(nameField.getText())){
            new ErrorWindow("Please do not use special characters");
            return;
        }
        DbUpdate.execute(
                () ->{
                    DbAdapter.updateStop(id, nameField.getText(), cityChoice.getValue().getCityID());
                    return null;
                },
                BusStop.this.update
        );
    }

    private void deleteStopFromDataBase(){
        DbUpdate.execute(
                () ->{
                    DbAdapter.deleteStop(id);
                    return null;
                },
                BusStop.this.update
        );
    }
}
