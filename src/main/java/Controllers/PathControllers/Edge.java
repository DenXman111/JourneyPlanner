package main.java.Controllers.PathControllers;

/**
 * Edge class stores information about single transit between cities.
 */
public class Edge {
    private City startCity, endCity;
    private double startTime, duration;

    Edge(City startCity, City endCity, double startTime, double duration){
        this.startCity = startCity;
        this.endCity = endCity;
        this.startTime = startTime;
        this.duration = duration;
    }

    public City getStartCity() {
        return startCity;
    }

    public City getEndCity() {
        return endCity;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return duration;
    }
}
