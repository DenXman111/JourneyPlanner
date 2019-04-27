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

    @SuppressWarnings("WeakerAccess")
    public City(Integer id, String name, double rating, int price){
        if (name == null)
            throw new NullPointerException();
        this.name = name;
        this.cityID = id;
        this.rating = rating;
        this.nightPrice = price;
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getID() { return cityID; }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public double getRating() { return rating; }

    @SuppressWarnings("unused")
    public int getNightPrice() { return nightPrice; }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public String getName() { return name; }

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


        Label label = new Label();
        label.setText(name);
        label.getStyleClass().add("description");

        box.getChildren().addAll(circle, label);
        return box;
    }

    @SuppressWarnings("WeakerAccess")
    public Node display(int indexInTrip, Trip trip){
        Pane box = display();

        ImageView deleteIcon = new ImageView("delete.png");
        deleteIcon.setFitHeight(10);
        deleteIcon.setFitWidth(10);
        deleteIcon.getStyleClass().add("icon");
        deleteIcon.setVisible(false);
        if (trip.allowToMergeEdges(indexInTrip)) {
            deleteIcon.setVisible(true);
            deleteIcon.setOnMouseClicked(mouseEvent -> trip.removeCityWithIndex(indexInTrip));
        }
        VBox.setMargin(deleteIcon, new Insets(5, 0, 0, 0));
        box.getChildren().add(deleteIcon);
        return box;
    }
}
