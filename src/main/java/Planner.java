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
    @SuppressWarnings("unused")
    public static List< ? extends Trip > plan(String startPoint, int fund, LocalDate startDate, LocalDate endDate){
        TripPlans trips = new TripPlans();

        Integer startID = DbAdapter.getCityID(startPoint);

        trips.findBest(startID, fund, startDate, endDate);
        return trips.getList();
        /*

        String[] exemplaryCities = new String[] {"Brno", "Prague", "Bratislava", "Lviv", "Warsaw", "Krakow"};
        List<Trip> result = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(12) + 3;
        return Stream.generate(() -> new Trip(
                Stream.generate(() -> exemplaryCities[random.nextInt(exemplaryCities.length)]).
                        limit(random.nextInt(10) + 3).
                        map(cityName -> new Edge(new City(startPoint), new City(cityName), 100, 100)).
                        collect(Collectors.toList())
        )).limit(size).collect(Collectors.toList());


         */
    }
}