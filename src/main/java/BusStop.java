@SuppressWarnings("unused")
public class BusStop {

    private Integer id;
    private String name;
    private String cityName;
    private Integer cityId;

    BusStop (Integer id, String name, String cityName, Integer cityId){
        this.id = id;
        this.name = name;
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCityName() {
        return cityName;
    }

    public Integer getCityId() {
        return cityId;
    }
}
