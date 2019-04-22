package main.java.Controllers.PathControllers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/**
 * Edge class stores information about single transit between cities.
 */
public class Edge implements Displayable{
    private City startCity, endCity;
    private double startTime, duration;

    Edge(City startCity, City endCity, double startTime, double duration){
        this.startCity = startCity;
        this.endCity = endCity;
        this.startTime = startTime;
        this.duration = duration;
    }

    @SuppressWarnings("WeakerAccess")
    public City getStartCity() {
        return startCity;
    }

    @SuppressWarnings("WeakerAccess")
    public City getEndCity() {
        return endCity;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return duration;
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
