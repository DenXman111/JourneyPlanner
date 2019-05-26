import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endingDate;
    private int price;

    private List<EdgesInOut> additionalVisits;
    private Edge edgeOmittingEndCity;

    Edge(Integer busID, City startCity, City endCity, int price, LocalDate startDate, LocalDate endingDate){
        this.busID = busID;
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startDate = startDate;
        this.endingDate = endingDate;
    }

    @SuppressWarnings("unused")
    private Edge(Edge edge){
        this.busID = edge.busID;
        this.startCity = edge.startCity;
        this.endCity = edge.endCity;
        this.startDate = edge.startDate;
        this.endingDate = edge.endingDate;
        this.price = edge.price;
        this.additionalVisits = edge.additionalVisits;
        this.edgeOmittingEndCity = edge.edgeOmittingEndCity;
    }

    @SuppressWarnings("WeakerAccess")
    public void findOmittingEdge(Edge nextEdge){
        edgeOmittingEndCity = mergeEdges(this, nextEdge);
    }

    @SuppressWarnings("WeakerAccess")
    public void findAdditionalVisits(LocalDate begin, LocalDate end){
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
    public LocalDate getStartDate() {
        return startDate;
    }

    @SuppressWarnings("WeakerAccess")
    public LocalDate getEndingDate() {
        return endingDate;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public int getBusId() {return busID; }

    @Override
    public Pane display() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        //draws line with description
        Line line = new Line(-50, 0, 50, 0);
        line.getStyleClass().add("line");
        Label label = new Label("Bus " + busID);
        label.getStyleClass().add("description");

        box.getChildren().addAll(line, label);
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

    private boolean isBetween(LocalDate begin, LocalDate end){
        return !begin.isAfter(startDate) && !end.isBefore(endingDate);
    }


    /*
     * Function requires improvement
     *      |
     *      V
     */
    @SuppressWarnings("WeakerAccess")
    public static Edge mergeEdges(Edge first, Edge second){
        if (first == null || second == null) return null;
        Integer startId = first.getStartCity().getID(), endId = second.getEndCity().getID();
        List<Edge> options = DbAdapter.getNeighbours(startId)
                .stream()
                .filter( edge -> edge.getEndCity().getID().equals(endId) && edge.isBetween(first.startDate, second.endingDate))
                .collect(Collectors.toList());
        return !options.isEmpty() ? options.get(0) : null;
    }
}
