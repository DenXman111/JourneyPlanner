import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.time.temporal.ChronoUnit.DAYS;

public class Trip implements Displayable{
    private List<Edge> plan;
    private double rating;
    private int daysInTrip;
    private VBox mainPane;
    private Timestamp beginTime, endTime;

    static private FormController formController;
    static boolean displayBookButton = true;

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
        this.beginTime = obj.beginTime;
        this.endTime = obj.endTime;
        this.mainPane = null;
    }

    @SuppressWarnings("WeakerAccess")
    static public void setFormController(FormController controller){
        formController = controller;
    }


    @SuppressWarnings({"WeakerAccess", "Duplicates"})
    public void countRating(){
        this.rating = 0;
        this.daysInTrip = 0;
        Edge prev = null;
        for (Edge now : plan){
            if (prev != null){
                int days = (int)DAYS.between(prev.getEndingDate().toLocalDateTime(), now.getStartDate().toLocalDateTime()) - 1;
                this.rating = this.rating * this.daysInTrip + now.getStartCity().getRating() * days;
                this.daysInTrip += days;
                if (this.daysInTrip > 0) this.rating /= this.daysInTrip; else this.rating = 0;
            }
            prev = now;
        }
    }

    @SuppressWarnings({"WeakerAccess", "Duplicates"})
    public void pushEdge(Edge edge){
        if (!plan.isEmpty()){
            Edge last = plan.get(plan.size() - 1);
            int days = (int)DAYS.between(last.getEndingDate().toLocalDateTime(), edge.getStartDate().toLocalDateTime()) - 1;
            this.rating = this.rating * this.daysInTrip + edge.getStartCity().getRating() * days;
            this.daysInTrip += days;
            if (this.daysInTrip > 0) this.rating /= this.daysInTrip; else this.rating = 0;
            //this.rating = Math.round(this.rating * 100) / 100;
        }
        plan.add(edge);
    }

    @SuppressWarnings("WeakerAccess")
    public void removeLastEdge(){
        if (plan.size() > 1) {
            Edge last = plan.get(plan.size() - 1);
            Edge prev = plan.get(plan.size() - 2);
            int days = (int)DAYS.between(prev.getEndingDate().toLocalDateTime(), last.getStartDate().toLocalDateTime()) - 1;
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

    @SuppressWarnings("WeakerAccess")
    public City getStartCity(){
        return plan.get(0).getStartCity();
    }

    public double getRating(){
        return rating;
    }

    @SuppressWarnings("WeakerAccess")
    public int getLivingPrice(Edge edge){
        if (plan.isEmpty()) return 0;
        Edge last = plan.get(plan.size() - 1);
        int days = (int)DAYS.between(last.getEndingDate().toLocalDateTime(), edge.getStartDate().toLocalDateTime());
        return edge.getStartCity().getNightPrice() * days;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public List<Edge> getPlan(){
        return plan;
    }

    @SuppressWarnings("unused")
    public void setRating(double rating){
        this.rating = rating;
    }


    class ModificationsFinder extends Task<Integer> {

        @Override
        protected Integer call() throws SQLException {
            if (plan == null || plan.isEmpty()) return null;
            beginTime = beginTime == null ? plan.get(0).getStartDate() : beginTime;
            endTime = endTime == null && !plan.isEmpty() ? plan.get(plan.size() - 1).getEndingDate() : endTime;
            for (int i = 0; i < plan.size(); i++) {

                updateProgress(i, plan.size());

                Edge edge = plan.get(i);
                Edge nextEdge = i < plan.size() - 1 ? plan.get(i + 1) : null;
                Timestamp begin = 0 < i ? plan.get(i - 1).getEndingDate() : beginTime;
                Timestamp end = nextEdge != null ? nextEdge.getStartDate() : endTime;
                edge.findAdditionalVisits(begin, end);
                edge.findOmittingEdge(nextEdge);
            }

            Platform.runLater(Trip.this::displayTripData);
            return 100;
        }
    }



    /**
     * Removes from plan two consecutive edges and inserts new edge bypassing their common city
     * @param index index of edge in the plan list whose endCity is to be removed
     * @param newEdge edge bypassing removed city
     */
    void removeCityWithIndex(int index, Edge newEdge){
        if (index < 0 || plan.size() - 1 <= index || newEdge == null) return;

        plan.set(index + 1, newEdge);
        ListIterator<Edge> iterator = plan.listIterator(index);
        iterator.next();
        iterator.remove();
        countRating();
        if (mainPane != null) display();
    }

    void insertEdges(int index, Edge first, Edge second){
        if (index < 0 ||  plan.size() <= index || first == null || second == null) return;

        plan.set(index, first);
        plan.add(index + 1, second);

        countRating();
        if (mainPane != null) display();
    }

    @Override
    public Node display() {
        if (mainPane == null){
            mainPane = new VBox();
            mainPane.setAlignment(Pos.CENTER_LEFT);
            mainPane.setMaxWidth(Region.USE_PREF_SIZE);
        }

        mainPane.getChildren().clear();
        ProgressIndicator indicator = new ProgressIndicator();

        ModificationsFinder finder = new ModificationsFinder();
        indicator.progressProperty().bind(finder.progressProperty());

        Main.daemonExecutor.submit(finder);

        mainPane.getChildren().add(indicator);

        return mainPane;
    }

    private void fillHBox(Pane pane) throws SQLException {
        beginTime = beginTime == null && !plan.isEmpty() ? plan.get(0).getStartDate() : beginTime;
        endTime = endTime == null && !plan.isEmpty() ? plan.get(plan.size() - 1).getEndingDate() : endTime;
        pane.getChildren().clear();
        if (!plan.isEmpty()) {
            //plan.get(0).getStartCity().setDays(DAYS.between(beginTime, plan.get(0).getStartDate()));
            pane.getChildren().add(plan.get(0).getStartCity().display(-1, this, null));
        }
        int index = 0;
        for (Edge edge : plan) {
            pane.getChildren().addAll(
                    edge.display(this, index),
                    edge.getEndCity().display(index++, this, Edge.mergeEdges(edge, edge.getEdgeOmittingEndCity()))
            );
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void fillInformationPane(Pane ratingPane){
        ratingPane.getChildren().clear();

        Label ratingLabel = new Label("Rating: ");
        ratingLabel.getStyleClass().add("rating-text");
        HBox.setMargin(ratingLabel, new Insets(0, 2, 0, 10));

        ImageView stars = new ImageView("stars.png");
        stars.setFitHeight(10);
        stars.setPreserveRatio(true);

        Rectangle2D crop = new Rectangle2D(0, 0 , 1280 * rating / 5 + 1, 256);
        stars.setViewport(crop);
        HBox.setMargin(stars, new Insets(0, 5, 3, 0));

        Label numberLabel = new Label(String.format("%.2f", rating));
        numberLabel.getStyleClass().add("grey-text");
        HBox.setMargin(numberLabel, new Insets(0, 10, 0, 0));

        Label travelTimeLabel = new Label( "Travel time: " + daysInTrip +  ( daysInTrip == 1 ? " days" : " day"));
        travelTimeLabel.getStyleClass().add("rating-text");
        HBox.setMargin(travelTimeLabel, new Insets(0, 5, 0, 0));

        ratingPane.getChildren().addAll(ratingLabel, stars, numberLabel, travelTimeLabel);
    }

    private void displayTripData(){
        //box wraps rating and drawn trip plan

        mainPane.getChildren().clear();

        HBox informationBox = new HBox();
        informationBox.setAlignment(Pos.BOTTOM_LEFT);
        informationBox.setMaxWidth(Region.USE_PREF_SIZE);
        informationBox.getStyleClass().add("rating-box");

        fillInformationPane(informationBox);

        // display all information about trip in HBox
        HBox tripBox = new HBox();
        tripBox.setAlignment(Pos.CENTER_LEFT);
        tripBox.setMaxWidth(Region.USE_PREF_SIZE);
        tripBox.setPrefHeight(60);
        tripBox.getStyleClass().add("boxes");
        try {
            fillHBox(tripBox);
        } catch (SQLException e) {
            tripBox.getChildren().clear();
        }

        Button showButton = new Button("map");
        showButton.getStyleClass().add("map-button");
        HBox.setMargin(showButton, new Insets(0, 10, 0, 0));

        Button bookButton = new Button("book");
        bookButton.getStyleClass().add("ratting-button");
        HBox.setMargin(bookButton, new Insets(0, 10, 0, 0));

        //box contains informationBox and bookButton
        HBox boxesAbove = new HBox();
        VBox.setMargin(boxesAbove, new Insets(0, 0, 5, 5));

        showButton.setOnAction( (event) -> {
            try {
                formController.showButtonPressed(this);
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        boxesAbove.getChildren().add(showButton);

        if (displayBookButton  && LoginController.username != null) {
            bookButton.setOnMouseClicked( mouseEvent -> {
                try {
                    DbAdapter.reserve(this, LoginController.username);
                    bookButton.getStyleClass().add("booked");
                    bookButton.setText("booked");
                    bookButton.setOnMouseClicked( mouseEvent1 -> {});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            boxesAbove.getChildren().add(bookButton);
        }
        boxesAbove.getChildren().add(informationBox);

        mainPane.getChildren().addAll(boxesAbove, tripBox);
    }
}