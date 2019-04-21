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
     * @param to Time when trip ha
     * @return Time before which the trip has to end.
     */
    @SuppressWarnings("unused")
    public static List<Edge> plan(String startPoint, int founds, double from, double to){
        String[] exemplaryCities = new String[] {"Brno", "Prague", "Bratislava", "Lviv", "Warsaw", "Krakow"};
        return Arrays.stream(exemplaryCities).
                map( cityName -> new Edge(null, new City(cityName), 100, 100)).
                collect(Collectors.toList());
    }
}
