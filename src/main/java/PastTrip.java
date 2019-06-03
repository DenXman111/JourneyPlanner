import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.sql.Timestamp;

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
        displayReservation();
        return mainBox;
    }

    boolean cancelable(){
        return reservation.earliestDeparture().after(new Timestamp(System.currentTimeMillis()));
    }

    public void displayReservation(){
        assert mainBox != null;

        if (cancelable()) {
            Button cancelButton = new Button("Cancel reservation");
            cancelButton.getStyleClass().add("cancel-button");
            VBox.setMargin(cancelButton, new Insets(10, 0, -3, 5));
            cancelButton.setOnMouseClicked(mouseEvent -> executeCancelTask());
            mainBox.getChildren().add(cancelButton);
        }

        Node tripNode = trip.display();
        VBox.setMargin(tripNode, new Insets(10, 20, 0,0));

        Node reservationNode = reservation.display();
        VBox.setMargin(reservationNode, new Insets(10, 20, 0, 0));

        mainBox.getChildren().addAll( tripNode, reservationNode);
    }

    public void displayMessage(String message, String styleClass){
        assert mainBox != null;
        mainBox.getChildren().clear();
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add(styleClass);
        mainBox.getChildren().add(messageLabel);
    }

    private class CancelTask extends Task<Integer> {
        @Override
        protected Integer call() {
            try{
                DbAdapter.cancelReservation(id);
                Platform.runLater(() -> displayMessage("Canceled", "canceled-label"));
            }catch (Exception e){
                Platform.runLater(() -> displayMessage("Failed", "red-message-label"));
                System.out.println("Error");
                e.printStackTrace();
            }
            return 100;
        }
    }

    private void executeCancelTask(){
        assert mainBox != null;
        mainBox.getChildren().clear();
        ProgressIndicator indicator = new ProgressIndicator();
        mainBox.getChildren().add(indicator);

        CancelTask task = new CancelTask();
        indicator.progressProperty().bind(task.progressProperty());

        Main.executors.submit(task);
    }
}
