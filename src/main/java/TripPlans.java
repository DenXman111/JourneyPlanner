import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Class have list of best trips and fill it.
 */
public class TripPlans {
    private List< ? extends Trip > TripsList;
    Trip current;

    @SuppressWarnings("WeakerAccess")
    public TripPlans(){
        TripsList = new ArrayList<>();
        current = new Trip();
    }

    @SuppressWarnings("WeakerAccess")
    public List < ? extends Trip > getList(){
        return TripsList;
    }

    private void rec(Integer nowID, int fund, LocalDate currentDate, LocalDate endingDate){
        List < ? extends Edge > neighbours = DbAdapter.getNeighbours(nowID);
        //...
    }
    @SuppressWarnings("WeakerAccess")
    public void findBest(Integer startID, int fund, LocalDate startDate, LocalDate endingDate){
        rec(startID, fund, startDate, endingDate);
    }
}
