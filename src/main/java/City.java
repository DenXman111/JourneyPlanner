import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * City class stores all data about cites read from database.
 * For now it oly stores city name.
 * @author Łukasz Selwa
 */
public class City implements Displayable {

    private String name;

    @SuppressWarnings("WeakerAccess")
    public City(String name){
        if (name == null)
            throw new NullPointerException();
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public Node display() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        // draws a circle above label containing city name
        Circle circle = new Circle();
        circle.setRadius(5);
        circle.getStyleClass().add("point");

        Label label = new Label();
        label.setText(name);
        label.getStyleClass().add("description");
        box.getChildren().addAll(circle, label);
        return box;
    }
}
