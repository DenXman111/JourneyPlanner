import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.*;

public class Planner extends Task<Integer> {

    private String startPoint;
    private int funds;
    private LocalDate startDate;
    private LocalDate endDate;

    private VBox box;
    private ProgressBar progressBar;

    public Planner(String startPoint, int funds, LocalDate startDate, LocalDate endDate){
        this.startPoint = startPoint;
        this.funds = funds;
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

        Integer startID = DbAdapter.getCityID(startPoint);

        trips.findBest(startID, funds, startDate, endDate);
        List < Trip > propositions = new ArrayList<>(trips.getSet());
        Collections.reverse(propositions);

        propositions.forEach(trip -> System.out.println("Rating of trip: " + trip.getRating()));

        if(box != null && progressBar != null)
            displayTrips(propositions);

        return propositions.size();
    }

    public class TripPlans {
        private Set < Integer > inCurrent;
        private Set< Trip > TripsList;
        private Trip current;

        private Integer start; //used in dfs to remember startID

        @SuppressWarnings("WeakerAccess")
        public TripPlans(){
            inCurrent = new TreeSet<>();
            TripsList = new TreeSet<>(Comparator.comparingDouble(Trip::getRating));
            current = new Trip();
        }

        @SuppressWarnings("WeakerAccess")
        public Set< Trip > getSet(){
            return TripsList;
        }

        private void dfs(Integer nowID, int fund, LocalDate currentDate, LocalDate tripEndingDate){

            int maxTripsNumber = 25;
            if(TripsList.size() >= maxTripsNumber)
                return;

            inCurrent.add(nowID);
            if (nowID.equals(start) && !current.isEmpty()){
//            System.out.println("Found trip " + nowID + " " + current.getRating() + " " + current.getPlan());
                TripsList.add(new Trip(current));

                //used for progress bar
                updateProgress(TripsList.size(), maxTripsNumber);

                inCurrent.remove(nowID);
                return;
            }

//        System.out.println("dfs in " + nowID + " " + fund + " " + currentDate + " " + current.getRating());

            List < Edge > neighbours = DbAdapter.getNeighbours(nowID);
            for (Edge e : neighbours){
                if (e.getEndingDate().isAfter(tripEndingDate)) continue;
                if (!e.getStartDate().isAfter(currentDate)) continue;
                if (e.getPrice() > fund) continue;
                if (inCurrent.contains(e.getEndCity().getID()) && !e.getEndCity().getID().equals(start)) continue;
                int livingPrice = current.getLivingPrice(e);
                if (fund < e.getPrice() + livingPrice) continue;
                current.pushEdge(e);
                dfs(e.getEndCity().getID(), fund - e.getPrice() - livingPrice, e.getEndingDate(), tripEndingDate);
                current.removeLastEdge();
            }

            inCurrent.remove(nowID);
        }
        @SuppressWarnings("WeakerAccess")
        public void findBest(Integer startID, int fund, LocalDate startDate, LocalDate endingDate){
            start = startID;
            dfs(startID, fund, startDate, endingDate);
        }
    }
}