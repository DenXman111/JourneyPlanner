import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

@SuppressWarnings("WeakerAccess")
public class PastTrip implements Displayable {

    private int id;
    private VBox mainBox;

    public Trip trip;
    public Reservation reservation;

    public PastTrip(int id, int people){
        this.id = id;
        trip = new Trip(people);
        reservation = new Reservation();
    }

    public int getId() {
        return id;
    }

    @Override
    public Node display() {
        if (mainBox == null) mainBox = new VBox();
        mainBox.getChildren().clear();

        Node tripNode = trip.display();
        VBox.setMargin(tripNode, new Insets(20, 20, 0,0));

        Node reservationNode = reservation.display();
        VBox.setMargin(reservationNode, new Insets(10, 20, 0, 0));

        mainBox.getChildren().addAll(tripNode, reservationNode);
        return mainBox;
    }
}
