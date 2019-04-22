package main.java.Controllers.PathControllers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class Trip implements Displayable{
    private List<? extends Edge> plan;
    @SuppressWarnings("WeakerAccess")
    public Trip(List<? extends Edge> plan) throws EmptyPlan {
        if (plan == null || plan.size() == 0)
            throw new EmptyPlan();
        this.plan = plan;
    }

    @Override
    public Node display() {
        // display all information about trip in HBox
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefHeight(60);
        hbox.getChildren().add(plan.get(0).getStartCity().display());
        plan.forEach(edge -> hbox.getChildren().addAll(edge.display(), edge.getEndCity().display()) );
        //wrap HBox in ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(hbox);
        //Wrap in grid
        GridPane grid = new GridPane();
        grid.setMaxWidth(Control.USE_PREF_SIZE);
        grid.add(scrollPane, 1,1);
        grid.getStyleClass().add("boxes");
        return grid;
    }
}