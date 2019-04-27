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

    Edge(Integer busID, City startCity, City endCity, int price, LocalDate startDate, LocalDate endingDate){
        this.busID = busID;
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startDate = startDate;
        this.endingDate = endingDate;
    }

    @SuppressWarnings("WeakerAccess")
    public City getStartCity() {
        return startCity;
    }

    @SuppressWarnings("WeakerAccess")
    public City getEndCity() {
        return endCity;
    }

    @SuppressWarnings("WeakerAccess")
    public int getPrice() { return price; }

    @SuppressWarnings("WeakerAccess")
    public LocalDate getStartDate() {
        return startDate;
    }

    @SuppressWarnings("WeakerAccess")
    public LocalDate getEndingDate() {
        return endingDate;
    }

    @SuppressWarnings("WeakerAccess")
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
    public Pane display(Trip trip, int index, List<EdgesInOut> edges){
        Pane box = display();
        if (trip == null ||  edges == null || edges.isEmpty()) return box;

        ImageView addIcon = new ImageView("plus-sign.png");
        addIcon.setFitHeight(12);
        addIcon.setFitWidth(12);
        addIcon.getStyleClass().add("icon");
        VBox.setMargin(addIcon, new Insets(5, 0, 0, 0));
        ContextMenu citiesMenu = new ContextMenu();

        Stream<MenuItem> itemsStream = edges.stream().map(inOut -> {
            MenuItem item = new MenuItem(inOut.getMiddleCity().getName());
            item.setOnAction(actionEvent -> trip.insertEdges(index, inOut.getInEdge(), inOut.getOutEdge()));
            return item;
        });

        citiesMenu.getItems().addAll( itemsStream.collect(Collectors.toList()));

        addIcon.setOnMouseClicked(mouseEvent -> citiesMenu.show(box, mouseEvent.getScreenX(), mouseEvent.getScreenY()));

        box.getChildren().add(addIcon);
        return box;
    }

    @SuppressWarnings("WeakerAccess")
    public static Edge mergeEdges(Edge first, Edge second){
        return new Edge(first.busID, first.startCity, second.endCity,
                first.price + second.price, first.startDate, second.endingDate);
    }
}
