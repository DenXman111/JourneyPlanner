import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class Trip implements Displayable{
    private List<? extends Edge> plan;
    private double rating;

    @SuppressWarnings("WeakerAccess")
    public Trip() {
        plan = new ArrayList<>();
        rating = 0;
    }

    public Trip(List<? extends Edge> plan) {
        this.plan = plan;
        rating = Rating.count(plan);
    }

    public double getRating(){
        return rating;
    }

    public void setRating(double rating){
        this.rating = rating;
    }

    @Override
    public Node display() {
        // display all information about trip in HBox
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setMaxWidth(Region.USE_PREF_SIZE);
        hbox.setPrefHeight(60);
        hbox.getStyleClass().add("boxes");
        hbox.getChildren().add(plan.get(0).getStartCity().display());
        plan.forEach(edge -> hbox.getChildren().addAll(edge.display(), edge.getEndCity().display()) );
        return hbox;
    }
}