import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Edge class stores information about single transit between cities.
 */
public class Edge implements Displayable{

    private Integer busID;
    private City startCity;
    private City endCity;
    private Timestamp startTime;
    private Timestamp endTime;
    private int price;

    private List<EdgesInOut> additionalVisits;
    private Edge edgeOmittingEndCity;

    Edge(Integer busID, City startCity, City endCity, int price, Timestamp startDate, Timestamp endingDate){
        this.busID = busID;
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startTime = startDate;
        this.endTime = endingDate;
    }

    @SuppressWarnings("unused")
    private Edge(Edge edge){
        this.busID = edge.busID;
        this.startCity = edge.startCity;
        this.endCity = edge.endCity;
        this.startTime = edge.startTime;
        this.endTime = edge.endTime;
        this.price = edge.price;
        this.additionalVisits = edge.additionalVisits;
        this.edgeOmittingEndCity = edge.edgeOmittingEndCity;
    }

    @SuppressWarnings("WeakerAccess")
    public void findOmittingEdge(Edge nextEdge) throws SQLException {
        edgeOmittingEndCity = mergeEdges(this, nextEdge);
    }

    @SuppressWarnings("WeakerAccess")
    public void findAdditionalVisits(Timestamp begin, Timestamp end) throws SQLException {
        additionalVisits = EdgesInOut.possibleInserts(this, begin, end);
    }

    @SuppressWarnings("WeakerAccess")
    public Edge getEdgeOmittingEndCity(){
        return edgeOmittingEndCity;
    }

    @SuppressWarnings("WeakerAccess")
    public City getStartCity() {
        return startCity;
    }

    @SuppressWarnings("WeakerAccess")
    public City getEndCity() {
        return endCity;
    }

    public int getPrice() { return price; }

    @SuppressWarnings("WeakerAccess")
    public Timestamp getStartTime() {
        return startTime;
    }

    @SuppressWarnings("WeakerAccess")
    public Timestamp getEndTime() {
        return endTime;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public int getBusId() {return busID; }

    private Label timeInformation(){
        Label dateLabel = new Label(Formatter.dateFormat(startTime) + ":     " +
                        Formatter.timeFormat(startTime) + " - " + Formatter.timeFormat(endTime));
        dateLabel.getStyleClass().addAll("grey-description");
        return dateLabel;
    }

    @Override
    public Pane display() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        //draws line with description
        Line line = new Line(-100, 0, 100, 0);
        VBox.setMargin(line, new Insets(5, 0, 0, 0));
        line.getStyleClass().add("line");
        Label label = new Label("Bus " + busID);
        label.getStyleClass().add("description");

        Label informationLabel = new Label(startTime.getTime() + "    " + endTime.getTime());
        informationLabel.getStyleClass().add("grey-description");
        box.getChildren().addAll(line, label, timeInformation());
        return box;
    }

    @SuppressWarnings("WeakerAccess")
    public Pane display(Trip trip, int index){
        Pane box = display();

        ImageView addIcon = new ImageView("plus-sign.png");
        addIcon.setFitHeight(12);
        addIcon.setFitWidth(12);
        addIcon.getStyleClass().add("icon");
        VBox.setMargin(addIcon, new Insets(7, 0, 0, 0));
        addIcon.setVisible(false);
        if (trip != null && additionalVisits != null && !additionalVisits.isEmpty() && Trip.displayBookButton) {
            addIcon.setVisible(true);
            ContextMenu citiesMenu = new ContextMenu();

            Stream<MenuItem> itemsStream = additionalVisits.stream().map(inOut -> {
                MenuItem item = new MenuItem(inOut.getMiddleCity().getName());
                item.setOnAction(actionEvent -> trip.insertEdges(index, inOut.getInEdge(), inOut.getOutEdge()));
                return item;
            });

            citiesMenu.getItems().addAll(itemsStream.collect(Collectors.toList()));

            addIcon.setOnMouseClicked(mouseEvent -> citiesMenu.show(box, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        }
        box.getChildren().add(addIcon);
        return box;
    }

    private boolean isBetween(Timestamp begin, Timestamp end){
        return !begin.after(startTime) && !end.before(endTime);
    }


    /*
     * Function requires improvement
     *      |
     *      V
     */
    @SuppressWarnings("WeakerAccess")
    public static Edge mergeEdges(Edge first, Edge second) throws SQLException {
        if (first == null || second == null) return null;
        Integer endId = second.getEndCity().getID();
        List<Edge> options = DbAdapter.getNeighbours(first.getStartCity(), first.getStartTime(), second.getEndTime())
                .stream()
                .filter( edge -> edge.getEndCity().getID().equals(endId) && edge.isBetween(first.startTime, second.endTime))
                .collect(Collectors.toList());
        return !options.isEmpty() ? options.get(0) : null;
    }
}
