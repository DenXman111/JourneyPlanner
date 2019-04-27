import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.time.temporal.ChronoUnit.DAYS;

public class Trip implements Displayable{
    private List<Edge> plan;
    private double rating;
    private int daysInTrip;
    private HBox createdBox;

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
        this.createdBox = null;
    }

    @SuppressWarnings("WeakerAccess")
    public Trip(List<Edge> plan){
        this.plan = plan;
        rating = 0;
        daysInTrip = 0;
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

    public Trip getTrip(){
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public double getRating(){
        return rating;
    }

    public List<Edge> getPlan(){
        return plan;
    }

    public void setRating(double rating){
        this.rating = rating;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean allowToMergeEdges(int index){
        return 0 <= index && index < plan.size() - 1 && plan.get(index).getBusId() == plan.get(index + 1).getBusId();
    }

    void removeCityWithIndex(int index){
        // doesn't allow to remove the last edge
        if (!allowToMergeEdges(index)) return;
        // allows to remove only if its the same bus
        plan.set(index + 1, Edge.mergeEdges(plan.get(index), plan.get(index + 1)));
        ListIterator<Edge> iterator = plan.listIterator(index);
        iterator.next();
        iterator.remove();
        if (createdBox != null) fillHBox(createdBox);
    }

    void insertEdges(int index, Edge first, Edge second){
        if (index < 0 ||  plan.size() <= index || first == null || second == null) return;

        plan.set(index, first);
        plan.add(index + 1, second);
        if (createdBox != null) fillHBox(createdBox);
    }

    @Override
    public Node display() {
        return createdBox != null ? createdBox : createNode();
    }

    private void fillHBox(Pane pane){
        pane.getChildren().clear();
        if (!plan.isEmpty()) pane.getChildren().add(plan.get(0).getStartCity().display(-1, this));
        int index = 0;
        for (Edge edge : plan)
            pane.getChildren().addAll(
                    edge.display(this, index, EdgesInOut.possibleInserts(edge)),
                    edge.getEndCity().display(index++, this));
    }

    private Node createNode(){
        // display all information about trip in HBox
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setMaxWidth(Region.USE_PREF_SIZE);
        hbox.setPrefHeight(60);
        hbox.getStyleClass().add("boxes");
        fillHBox(hbox);
        createdBox = hbox;
        return hbox;
    }
}