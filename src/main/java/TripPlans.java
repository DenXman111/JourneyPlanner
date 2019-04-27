import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;


/**
 * Class have list of best trips and fill it.
 */
@SuppressWarnings("WeakerAccess")
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
    public Set < Trip > getSet(){
        return TripsList;
    }

    private void dfs(Integer nowID, int fund, LocalDate currentDate, LocalDate tripEndingDate){
        inCurrent.add(nowID);
        if (nowID.equals(start) && !current.isEmpty()){
            System.out.println("Found trip " + nowID + " " + current.getRating() + " " + current.getPlan());
            TripsList.add(new Trip(current));
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
//        System.out.println("TRIPSLIST " + TripsList.toArray().length);
    }
}
