import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Wrapper over pair of edges, inEdge should lead to the city where outEdge starts.
 * Class used during modification of user's trips
 */
@SuppressWarnings("WeakerAccess")
public class EdgesInOut implements Comparable<EdgesInOut>{
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

    private boolean isBetween(LocalDate begin, LocalDate end){
        return begin.isBefore(inEdge.getStartDate()) && end.isAfter(outEdge.getEndingDate());
    }

    private boolean dateIsProper(){
        return inEdge.getEndingDate().isBefore(outEdge.getStartDate());
    }

    /*
     * Function requires improvement
     *      |
     *      V
     */
    public static List<EdgesInOut> possibleInserts(Edge edge, LocalDate begin, LocalDate end){
        if (edge == null || begin == null || end == null) return null;
        List<EdgesInOut> listAll = DbAdapter.getCitiesBetween(edge.getStartCity().getID(), edge.getEndCity().getID())
                .stream().filter(EdgesInOut::dateIsProper).collect(Collectors.toList());
        /*
         * In future we are should add here checking repeats cites
         */
        listAll = listAll.stream().
                filter( edgesInOut ->
                        edgesInOut.isBetween(begin, end) && edgesInOut.dateIsProper()).
                collect(Collectors.toList());
        //remove duplicates
        TreeSet<EdgesInOut> set = new TreeSet<>(listAll);
        return new ArrayList<>(set);
    }

    @Override
    public int compareTo(EdgesInOut o) {
        return this.getMiddleCity().getName().compareTo(o.getMiddleCity().getName());
    }

    @Override
    public String toString(){
        return inEdge.getStartDate() + " : "
                + inEdge.getStartCity().getName() + " -- " + inEdge.getBusId() + " -> "
                + getMiddleCity().getName() + " -- " + outEdge.getBusId() + " -> "
                + outEdge.getEndCity().getName() + " : "
                + outEdge.getEndingDate();
    }
}
