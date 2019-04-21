package main.java.Controllers.PathControllers;

/**
 * City class stores all data about cites read from database.
 * For now it oly stores city name.
 * @author Łukasz Selwa
 */
public class City {
    private String name;

    City(String name){
        this.name = name;
    }

    public String getName() { return name; }
}
