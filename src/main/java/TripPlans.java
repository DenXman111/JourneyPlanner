import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;


/**
 * Class have list of best trips and fill it.
 */
public class TripPlans {
    Set < Integer > inCurrent;
    private Set< ? extends Trip > TripsList;
    private Trip current;

    @SuppressWarnings("WeakerAccess")
    public TripPlans(){
        inCurrent = new TreeSet<>();
        TripsList = new TreeSet<>(Comparator.comparingDouble(Trip::getRating));
        current = new Trip();
    }

    @SuppressWarnings("WeakerAccess")
    public Set < ? extends Trip > getSet(){
        return TripsList;
    }

    private void dfs(Integer nowID, int fund, LocalDate currentDate, LocalDate endingDate){
        inCurrent.add(nowID);

        List < ? extends Edge > neighbours = DbAdapter.getNeighbours(nowID);
        for (Edge e : neighbours){
            if (!e.getEndingDate().isBefore(endingDate)) continue;
            if (e.getPrice() > fund) continue;
            if (inCurrent.contains(e.getEndCity().getID())) continue;
            dfs(e.getEndCity().getID(), fund - e.getPrice(), e.getEndingDate(), endingDate);
        }

        inCurrent.remove(nowID);
    }
    @SuppressWarnings("WeakerAccess")
    public void findBest(Integer startID, int fund, LocalDate startDate, LocalDate endingDate){
        dfs(startID, fund, startDate, endingDate);
    }
}
