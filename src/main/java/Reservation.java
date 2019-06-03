import com.google.common.collect.ImmutableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Reservation implements Displayable{

    public class TransitReservation implements Displayable{
        List<Integer> seats;
        int transitId;
        Timestamp departure;
        String departureStop;
        String arrivalStop;

        TransitReservation(int transitId, Timestamp departure, String departureStop, String arrivalStop, Integer [] seatsArray) {
            this.transitId = transitId;
            this.departure = departure;
            this.departureStop = departureStop;
            this.arrivalStop = arrivalStop;
            this.seats = ImmutableList.copyOf(seatsArray);
        }

        @Override
        public Node display() {
            VBox vBox = new VBox();
            vBox.getStyleClass().add("boxes");

            Label busLabel = new Label("Bus: " + transitId + "\nreserved seats:");
            busLabel.getStyleClass().add("description");
            VBox.setMargin(busLabel, new Insets(5, 0, 5, 0));

            Tooltip tooltip = new Tooltip(
                    "departure: " + Formatter.fullDateFormat(departure) + "\n" +
                    "from: '" + departureStop + "'\nto: '" + arrivalStop + "'"
            );
            tooltip.setShowDelay(Duration.ZERO);
            Tooltip.install(vBox, tooltip);

            TextArea reservedSeats = new TextArea();
            reservedSeats.setWrapText(true);
            reservedSeats.setEditable(false);
            reservedSeats.setPrefWidth(100);
            reservedSeats.setPrefHeight(40);
            reservedSeats.setMaxWidth(Region.USE_PREF_SIZE);
            reservedSeats.setMaxHeight(Region.USE_PREF_SIZE);

            StringBuilder ticketsBuilder = new StringBuilder();
            seats.forEach(number -> ticketsBuilder.append(number).append(", "));
            ticketsBuilder.setLength(ticketsBuilder.length() - 2);

            reservedSeats.setText(ticketsBuilder.toString());

            vBox.getChildren().addAll(busLabel, reservedSeats);

            return vBox;
        }
    }


    private List<TransitReservation> reservations = new ArrayList<>();


    public void addTransitReservation(TransitReservation reservation){
        reservations.add(reservation);
    }

    @Override
    public Node display() {
        HBox box = new HBox();
        VBox.setMargin(box, new Insets(15, 0, 0, 0));
        reservations.forEach(reservation -> {
            Node reservationNode = reservation.display();
            HBox.setMargin(reservationNode, new Insets(0, 20, 0, 0));
            box.getChildren().add(reservationNode);
        });
        return box;
    }

}
