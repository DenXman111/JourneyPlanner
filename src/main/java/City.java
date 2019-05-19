import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * City class stores all data about cites read from database.
 */
public class City implements Displayable {

    private String name;
    private Integer cityID;
    private double rating;
    private int nightPrice;
    private long days;

    @SuppressWarnings("WeakerAccess")
    public City(Integer id, String name, double rating, int price){
        if (name == null)
            throw new NullPointerException();
        this.name = name;
        this.cityID = id;
        this.rating = rating;
        this.nightPrice = price;
        this.days = 0;
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getID() { return cityID; }

    @SuppressWarnings({"unused"})
    public double getRating() { return rating; }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public int getNightPrice() { return nightPrice; }

    @SuppressWarnings({"unused"})
    public String getName() { return name; }

    /**
     * Wraps small circle and city name in VBox.
     * Displays additional information about the city when cursor is over the circle
     * @return Pane with dot and city name
     */
    @Override
    public Pane display() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);

        // draws a circle above label containing city name
        Circle circle = new Circle();
        circle.setRadius(5);
        circle.getStyleClass().add("point");

        // displays additional information when user moves cursor above circle
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Rating: " + rating + "\nNight price: " + nightPrice);
        tooltip.setShowDelay(Duration.ZERO);
        Tooltip.install(circle, tooltip);

        // Displays label with city name
        Label label = new Label();
        label.setText(name + "\n");
        label.getStyleClass().add("description");

        box.getChildren().addAll(circle, label);
        return box;
    }

    /**
     *
     * @param indexInTrip index of this city in trip.plan
     * @param trip trip in which the city is contained
     * @return result of display() with additional button used to remove the city from trip
     */
    @SuppressWarnings("WeakerAccess")
    public Node display(int indexInTrip, Trip trip, Edge replaceEdge){
        Pane box = display();

        // displays delete icon
        ImageView deleteIcon = new ImageView("delete.png");
        deleteIcon.setFitHeight(10);
        deleteIcon.setFitWidth(10);
        deleteIcon.getStyleClass().add("icon");
        deleteIcon.setVisible(false);
        if (trip != null && replaceEdge != null && Trip.displayBookButton) {
            deleteIcon.setVisible(true);
            deleteIcon.setOnMouseClicked(mouseEvent -> trip.removeCityWithIndex(indexInTrip, replaceEdge));
        }
        VBox.setMargin(deleteIcon, new Insets(5, 0, 0, 0));
        box.getChildren().add(deleteIcon);
        return box;
    }

    @SuppressWarnings("WeakerAccess")
    public void setDays(long days) {
        this.days = days;
    }
}
