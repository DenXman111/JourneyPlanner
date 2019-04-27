import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.time.temporal.ChronoUnit.DAYS;

public class Trip implements Displayable{
    private List<Edge> plan;
    private double rating;
    private int daysInTrip;
    private HBox createdTripBox;
    private Pane mainPane;

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
        this.createdTripBox = null;
        this.mainPane = null;
    }

    /*
    // Used for generating random trips during testing trip.display
    public Trip(List<Edge> plan){
        this.plan = plan;
        rating = new Random().nextInt(500) / 100.0;
    }
    */

    @SuppressWarnings("WeakerAccess")
    public void pushEdge(Edge edge){
        if (!plan.isEmpty()){
            Edge last = plan.get(plan.size() - 1);
            int days = (int)DAYS.between(last.getEndingDate(), edge.getStartDate()) - 1;
            this.rating = this.rating * this.daysInTrip + edge.getStartCity().getRating() * days;
            this.daysInTrip += days;
            if (this.daysInTrip > 0) this.rating /= this.daysInTrip; else this.rating = 0;
            //this.rating = Math.round(this.rating * 100) / 100;
        }

        plan.add(edge);
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
            if (this.daysInTrip > 0) this.rating /= this.daysInTrip; else this.rating = 0;
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

    @SuppressWarnings("WeakerAccess")
    public int getLivingPrice(Edge edge){
        if (plan.isEmpty()) return 0;
        Edge last = plan.get(plan.size() - 1);
        int days = (int)DAYS.between(last.getEndingDate(), edge.getStartDate());
        return edge.getStartCity().getNightPrice() * days;
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
        if (createdTripBox != null) fillHBox(createdTripBox);
    }

    void insertEdges(int index, Edge first, Edge second){
        if (index < 0 ||  plan.size() <= index || first == null || second == null) return;

        plan.set(index, first);
        plan.add(index + 1, second);
        if (createdTripBox != null) fillHBox(createdTripBox);
    }

    @Override
    public Node display() {
        return mainPane != null ? mainPane : createNode();
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
        //box wraps rating and drawn trip plan
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(Region.USE_PREF_SIZE);
        box.getStyleClass().add("boxes");

        HBox informationBox = new HBox();
        informationBox.setAlignment(Pos.BOTTOM_LEFT);

        Label ratingLabel = new Label("Rating: ");
        ratingLabel.getStyleClass().add("description");
        HBox.setMargin(ratingLabel, new Insets(0, 5, 0, 10));

        ImageView stars = new ImageView("stars.png");
        stars.setFitHeight(10);
        stars.setPreserveRatio(true);

        Rectangle2D crop = new Rectangle2D(0, 0 , 1280 * rating / 5 + 1, 256);
        stars.setViewport(crop);

        HBox.setMargin(stars, new Insets(0, 5, 3, 0));

        Label numberLabel = new Label(String.valueOf(rating));
        numberLabel.getStyleClass().add("grey-text");

        informationBox.getChildren().addAll( ratingLabel, stars, numberLabel);

        // display all information about trip in HBox
        HBox tripBox = new HBox();
        tripBox.setAlignment(Pos.CENTER_LEFT);
        tripBox.setMaxWidth(Region.USE_PREF_SIZE);
        tripBox.setPrefHeight(60);
        fillHBox(tripBox);
        createdTripBox = tripBox;

        box.getChildren().addAll(informationBox, tripBox);
        return box;
    }
}