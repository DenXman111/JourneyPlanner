import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.HOURS;

public class Planner extends Task<Integer> {

    private String startPoint;
    private int funds;
    private int seats;
    private LocalDate startDate;
    private LocalDate endDate;

    private VBox box;
    private ProgressBar progressBar;

    private Map<Integer, List<Edge>> map;

    public Planner(String startPoint, int funds, int seats, LocalDate startDate, LocalDate endDate){
        this.startPoint = startPoint;
        this.funds = funds;
        this.seats = seats;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @SuppressWarnings("WeakerAccess")
    public void setDisplayData(VBox box, ProgressBar progressBar){
        this.box = box;
        this.progressBar = progressBar;
    }

    private void displayTrips(List<Trip> propositions){
        Platform.runLater(()-> {

            if (propositions.isEmpty()){
                Label messageLabel = new Label("No trips found");
                messageLabel.getStyleClass().add("red-message-label");
                VBox.setMargin(messageLabel, new Insets(0, 0, 0, 250));
                box.getChildren().add(messageLabel);
            }

            propositions.stream().
                map(Trip::display).
                forEach(node -> {
                    box.getChildren().add(node);
                    VBox.setMargin(node, new Insets(20, 10, 10, 20));
                });
            progressBar.setVisible(false);
        });
    }

    @Override
    protected Integer call() {
        TripPlans trips = new TripPlans();

        Integer startID;
        try {
            startID = DbAdapter.getCityID(startPoint);

            //System.out.println("Start downloading map");
            map = DbAdapter.getAllAvailableTransits(Date.valueOf(startDate), Date.valueOf(endDate), seats);
            System.out.println("Downloaded map");
            System.out.println("Cities: " + map.size());

            //System.out.println("Start looking for trips");
            trips.findBest(startID, funds / seats, startDate, endDate);
            List < Trip > propositions = new ArrayList<>(trips.getSet());
            Collections.reverse(propositions);

            propositions.forEach(trip -> System.out.println("Rating of trip: " + trip.getRating()));

            if(box != null && progressBar != null)
                displayTrips(propositions);

            return propositions.size();
        } catch (SQLException | DatabaseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressWarnings("FieldCanBeLocal")
    public class TripPlans {
        private TreeSet < City > inCurrent;
        private TreeSet < Trip > tripsList;
        private Trip current;
        private final int  maxTripsNumber = 25;
        private final int maxTripLength = 50;

        private Integer start; //used in dfs to remember startID

        @SuppressWarnings("WeakerAccess")
        public TripPlans(){
            inCurrent = new TreeSet<>(Comparator.comparingInt(City::getID));
            tripsList = new TreeSet<>(Comparator.comparingDouble(Trip::getRating));
            current = new Trip(seats);
        }

        @SuppressWarnings("WeakerAccess")
        public Set< Trip > getSet(){
            return tripsList;
        }

        private void dfs(City currentCity, int fund, Timestamp currentDate, Timestamp tripEndingDate) {

            //if(tripsList.size() >= maxTripsNumber) return; //BAD - don't find best ways

            inCurrent.add(currentCity);
            if (currentCity.getID().equals(start) && !current.isEmpty()){
               System.out.println("Found trip " + currentCity.getID() + " " + current.getRating() + " " + current.getPlan());
                tripsList.add(new Trip(current));
                if(tripsList.size() > maxTripsNumber) tripsList.remove(tripsList.first());

                //used for progress bar
                updateProgress(tripsList.size(), maxTripsNumber);

                inCurrent.remove(currentCity);
                return;
            }

            //System.out.println("dfs in " + currentCity.getID() + " " + fund + " " + currentDate + " " + current.getRating());

            if (current.getPlan().size() > maxTripLength){
                inCurrent.remove(currentCity);
                return;
            }

            if (!map.containsKey(currentCity.getID())){
                //System.out.println("No neighbours");
                inCurrent.remove(currentCity);
                return;
            }
            List < Edge > neighbours = map.get(currentCity.getID());
            //System.out.println("list size: :" + neighbours.size());
            for (Edge e : neighbours){
                if (e.getEndTime().after(tripEndingDate)) continue;
                if (!e.getStartTime().after(currentDate)) continue;
                if (!current.isEmpty() && (int)HOURS.between(currentDate.toLocalDateTime(), e.getStartTime().toLocalDateTime()) < 2) continue; //Time to change bus -- min 1 hour
                if (e.getPrice() > fund) continue;
                if (inCurrent.contains(e.getEndCity()) && !e.getEndCity().getID().equals(start)) continue;
                int livingPrice = current.getLivingPrice(e);
                if (fund < e.getPrice() + livingPrice) continue;
                current.pushEdge(e);
                dfs(e.getEndCity(), fund - e.getPrice() - livingPrice, e.getEndTime(), tripEndingDate);
                current.removeLastEdge();
            }

            inCurrent.remove(currentCity);
        }
        @SuppressWarnings("WeakerAccess")
        public void findBest(Integer startID, int fund, LocalDate startDate, LocalDate endingDate) throws SQLException, DatabaseException {
            start = startID;
            dfs(DbAdapter.getCityFromID(startID), fund, Timestamp.valueOf(startDate.atStartOfDay()), Timestamp.valueOf(endingDate.atStartOfDay()));
        }
    }
}