import javafx.scene.control.Button;
import javafx.scene.control.TextField;

@SuppressWarnings("unused")
public class CityRow {

    private City city;
    private TextField nameField;
    private Integer cityID;
    private TextField ratingField;
    private TextField nightPriceField;
    private TextField countyField;
    private Button deleteButton;

    private Runnable update;

    CityRow (City city, Runnable update){
        this.city = city;
        nameField = new TextField(city.getName());
        nameField.setOnAction(actionEvent -> chaneCityInDataBase());
        cityID = city.getCityID();
        ratingField = new TextField(Double.toString(city.getRating()));
        ratingField.setOnAction(actionEvent -> chaneCityInDataBase());
        nightPriceField = new TextField(Double.toString(city.getNightPrice()));
        nightPriceField.setOnAction(actionEvent -> chaneCityInDataBase());
        countyField = new TextField(city.getCountry());
        countyField.setOnAction(actionEvent -> chaneCityInDataBase());
        this.update = update;

        deleteButton = new Button(" x ");
        deleteButton.getStyleClass().add("ratting-button");
        deleteButton.setOnMouseClicked( clicked -> deleteCityFromDataBase());
        if (city.hasStops())
            deleteButton.setDisable(true);
    }

    public TextField getNameField() {
        return nameField;
    }

    public Integer getCityID() {
        return cityID;
    }

    public TextField getRatingField() {
        return ratingField;
    }

    public TextField getNightPriceField() {
        return nightPriceField;
    }

    public TextField getCountyField() {
        return countyField;
    }

    public Button getDeleteButton(){
        return deleteButton;
    }

    private boolean noChanges(){
        return  city.getName().equals(nameField.getText()) &&
                city.getCountry().equals(countyField.getText()) &&
                Double.toString(city.getRating()).equals(ratingField.getText()) &&
                Double.toString(city.getNightPrice()).equals(nightPriceField.getText());
    }

    private boolean rightData(){
        return  TextChecker.containsSpecialSigns(nameField.getText()) ||
                TextChecker.containsSpecialSigns(countyField.getText()) ||
                !TextChecker.isDouble(ratingField.getText()) ||
                !TextChecker.isDouble(nightPriceField.getText());
    }

    private void chaneCityInDataBase(){
        if (noChanges()) return;
        if (rightData()){
            new ErrorWindow("Wrong input, you used special characters \n" +
                    "or you haven't written proper number in the rating or the night price fields");
            nameField.setText(city.getName());
            return;
        }

        DbUpdate.execute(
                () -> {
                    DbAdapter.uppdateCity(
                            cityID,
                            nameField.getText(),
                            countyField.getText(),
                            Double.parseDouble(ratingField.getText()),
                            Double.parseDouble(nightPriceField.getText())
                    );
                    return null;
                },
                () -> CityRow.this.update.run()
        );
    }

    private void deleteCityFromDataBase(){
        DbUpdate.execute(
                () -> {
                    DbAdapter.deleteCity(CityRow.this.cityID);
                    return null;
                },
                () -> CityRow.this.update.run()
        );
    }
}
