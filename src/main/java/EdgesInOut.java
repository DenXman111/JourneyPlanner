import java.util.Arrays;
import java.util.List;

/**
 * Wrapper over pair of edges, inEdge should lead to the city where outEdge starts.
 * Class used during modification of user's trips
 */
@SuppressWarnings("WeakerAccess")
public class EdgesInOut {
    private final Edge inEdge, outEdge;

    @SuppressWarnings("WeakerAccess")
    public EdgesInOut(Edge inEdge, Edge outEdge){
        if (!inEdge.getEndCity().getID().equals(outEdge.getStartCity().getID()))
            throw new RuntimeException("Wrong cities");
        this.inEdge = inEdge;
        this.outEdge = outEdge;
    }

    @SuppressWarnings("WeakerAccess")
    public Edge getInEdge() {
        return inEdge;
    }

    @SuppressWarnings("WeakerAccess")
    public Edge getOutEdge() {
        return outEdge;
    }

    @SuppressWarnings("WeakerAccess")
    public City getMiddleCity(){
        return inEdge.getEndCity();
    }

    @SuppressWarnings("WeakerAccess")
    public static List<EdgesInOut> possibleInserts(Edge edge){
        // Only for example, should be replaced with searching possible city to insert in database
        return Arrays.asList(
                new EdgesInOut(
                    new Edge(0, edge.getStartCity() ,new City(21, "Krakow", 37, 420), 20, edge.getStartDate(), edge.getEndingDate()),
                    new Edge(0, new City(21, "Krakow", 37, 420), edge.getEndCity(), 20, edge.getStartDate(), edge.getEndingDate())
                ),
                new EdgesInOut(
                        new Edge(0, edge.getStartCity() ,new City(21, "Warszawa", 37, 420), 20, edge.getStartDate(), edge.getEndingDate()),
                        new Edge(0, new City(21, "Warszawa", 37, 420), edge.getEndCity(), 20, edge.getStartDate(), edge.getEndingDate())
                )
        );
    }
}
