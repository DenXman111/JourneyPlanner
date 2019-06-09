@SuppressWarnings("unused")
public class Line {
    private Integer id;
    private String startStopName;
    private String endStopName;

    public Line (Integer id, String startStopName, String endStopName){
        this.id = id;
        this.startStopName = startStopName;
        this.endStopName = endStopName;
    }

    public Integer getId() {
        return id;
    }

    public String getStartStopName() {
        return startStopName;
    }

    public String getEndStopName() {
        return endStopName;
    }
}
