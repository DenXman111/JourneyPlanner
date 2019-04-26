import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class Trip implements Displayable{
    private List<Edge> plan;
    private double rating;
    private int daysInTrip;

    @SuppressWarnings("WeakerAccess")
    public Trip() {
        plan = new ArrayList<>();
        rating = 0;
        daysInTrip = 0;
    }


    @SuppressWarnings("WeakerAccess")
    public Trip(Trip obj) {
        this.plan = new ArrayList<>(obj.plan);
        this.rating = obj.rating;
        this.daysInTrip = obj.daysInTrip;
    }

    @SuppressWarnings("WeakerAccess")
    public void pushEdge(Edge e){
        if (!plan.isEmpty()){
            Edge last = plan.get(plan.size() - 1);
            int days = (int)DAYS.between(last.getEndingDate(), e.getStartDate()) - 1;
            this.rating = this.rating * this.daysInTrip + e.getStartCity().getRating() * days;
            this.daysInTrip += days;
            this.rating /= this.daysInTrip;
        }

        plan.add(e);
        //System.out.println("----");
        //System.out.println("Wanna add edge " + e.getStartCity().getID() + " " + e.getEndCity().getID());
        //System.out.println(plan.size());
    }

    @SuppressWarnings("WeakerAccess")
    public void removeLastEdge(){
        if (plan.size() > 1) {
            Edge last = plan.get(plan.size() - 1);
            Edge prev = plan.get(plan.size() - 2);
            int days = (int)DAYS.between(prev.getEndingDate(), last.getStartDate()) - 1;
            this.rating *= this.daysInTrip;
            this.daysInTrip -= days;
            this.rating -= last.getStartCity().getRating() * days;
        }
        if (!plan.isEmpty()) plan.remove(plan.get(plan.size() - 1));
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isEmpty(){
        return plan.isEmpty();
    }

    @SuppressWarnings("WeakerAccess")
    public Trip getTrip(){
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public double getRating(){
        return rating;
    }

    @SuppressWarnings("WeakerAccess")
    public List<Edge> getPlan(){
        return plan;
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
        if (!plan.isEmpty()) hbox.getChildren().add(plan.get(0).getStartCity().display());
        plan.forEach(edge -> hbox.getChildren().addAll(edge.display(), edge.getEndCity().display()) );
        return hbox;
    }
}