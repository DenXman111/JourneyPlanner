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
        //wrap HBox in ScrollPane
        /*ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(hbox);
        //Wrap in grid
        GridPane grid = new GridPane();
        grid.setMaxWidth(Control.USE_PREF_SIZE);
        grid.add(scrollPane, 1,1);
        grid.getStyleClass().add("boxes");*/
        return hbox;
    }
}