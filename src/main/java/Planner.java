import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planner {

    /**
     *
     * @param startPoint city where user starts his journey
     * @param fund Founds that user can spend on his trip
     * @param startDate Time where trip starts
     * @param endDate Time before which the trip has to end.
     * @return List of proposed trips
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static List<Trip > plan(String startPoint, int fund, LocalDate startDate, LocalDate endDate){
        /*TripPlans trips = new TripPlans();

        Integer startID = DbAdapter.getCityID(startPoint);

        trips.findBest(startID, fund, startDate, endDate);
        List < Trip > result = new ArrayList<>(trips.getSet());
        Collections.reverse(result);
        while (result.size() > 25) result.remove(result.size() - 1);
        for (Trip tr : result){
            System.out.println("Rating of trip: " + tr.getRating());
        }
        return result;*/

        String[] exemplaryCities = new String[] {"Brno", "Prague", "Bratislava", "Lvov", "Warsaw", "Krakow"};
        List<Trip> result = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(12) + 3;
        return Stream.generate(() -> new Trip(
                Stream.generate( () -> exemplaryCities[random.nextInt(exemplaryCities.length)]).
                        limit(random.nextInt(2) + 3).
                        map(cityName -> new Edge(
                                0,
                                new City(0, startPoint, 0, 0),
                                new City(0,cityName, 0, 0),
                                100,
                                LocalDate.now(),
                                LocalDate.now())).
                        collect(Collectors.toList())
        )).limit(size).collect(Collectors.toList());
    }
}