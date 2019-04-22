package main.java.Controllers.PathControllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Planner {

    /**
     *
     * @param startPoint city where user starts his journey
     * @param founds Founds that user can spend on his trip
     * @param from Time where trip starts
     * @param to Time before which the trip has to end.
     * @return List of proposed trips
     */
    @SuppressWarnings("unused")
    public static List<Trip> plan(String startPoint, int founds, double from, double to){

        String[] exemplaryCities = new String[] {"Brno", "Prague", "Bratislava", "Lviv", "Warsaw", "Krakow"};
        List<Edge> plan = Arrays.stream(exemplaryCities).
                map( cityName -> new Edge(new City(startPoint), new City(cityName), 100, 100)).
                collect(Collectors.toList());
        try {
            Trip trip = new Trip(plan);
            return Arrays.asList(trip, trip, trip);
        } catch (EmptyPlan emptyPlan) {
            emptyPlan.printStackTrace();
            return null;
        }
    }
}
