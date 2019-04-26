import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.time.LocalDate;
import java.util.Date;

/**
 * Edge class stores information about single transit between cities.
 */
public class Edge implements Displayable{

    private Integer BusID;
    private City startCity;
    private City endCity;
    private Date startDate;
    private Date endingDate;
    private int price;

    Edge(Integer BusID, City startCity, City endCity, int price, Date startDate, Date endingDate){
        this.BusID = BusID;
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startDate = startDate;
        this.endingDate = endingDate;
    }

    @SuppressWarnings("WeakerAccess")
    public City getStartCity() {
        return startCity;
    }

    @SuppressWarnings("WeakerAccess")
    public City getEndCity() {
        return endCity;
    }

    public Date getStartTime() {
        return startDate;
    }

    public Date getEndDate() {
        return endingDate;
    }

    @Override
    public Node display() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        //draws line with description
        Line line = new Line(-50, 0, 50, 0);
        line.getStyleClass().add("line");
        Label label = new Label("Bus");
        label.getStyleClass().add("description");

        box.getChildren().addAll(line, label);
        return box;
    }
}
