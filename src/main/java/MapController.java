import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.service.geocoding.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class MapController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {
    private DirectionsService directionsService;
    private DirectionsPane directionsPane;

    protected String from;
    protected String to;
    @SuppressWarnings("WeakerAccess")
    protected DirectionsRenderer directionsRenderer = null;
    private GoogleMap map;

    @FXML
    private GoogleMapView mapView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView.setKey(Main.APIkey);
        mapView.addMapInializedListener(this);
    }

    private void addMarker(City city, String title){
        String address = city.getName() + ", " + city.getCountry();
        GeocodingService geocodingService = new GeocodingService();
        geocodingService.geocode(address,
                (GeocodingResult[] geocodingResults, GeocoderStatus geocoderStatus) -> {
//                    System.out.println("Step 1");
                    GeocoderGeometry geometry = geocodingResults[0].getGeometry();
//                    System.out.println("Step 2");
                    double latitude = geometry.getLocation().getLatitude();
                    double longitude = geometry.getLocation().getLongitude();
//                    System.out.println("Step 3");
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLong(latitude, longitude));
                    markerOptions.title(city.getName());
                    //markerOptions.icon("");
                    Marker marker = new Marker(markerOptions);
                    map.addMarker(marker);

                    InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                    if (title.equals("Start")) infoWindowOptions.content("<h2>" + title + "</h2>");
                    else {
                        infoWindowOptions.content("<h2>" + title + "</h2>"
                                + "Rating: " + city.getRating() + "<br>"
                                //+ "Days here: " + ((int) DAYS.between(startDates.get(city.getName()), endingDates.get(city.getName())) - 1) + "<br>"
                                + "Arrival date: " + startDates.get(city.getName()) + "<br>"
                                + "Departure date: " + endingDates.get(city.getName()) + "<br>"
                                + "Apartment price: " + city.getNightPrice() + "€");
                    }

                    InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
                    infoWindow.open(map, marker);
                    System.out.println("Marker " + title + " added");
                }
        );
    }

    private Map<String, Timestamp> startDates = new HashMap<>();
    private Map<String, Timestamp> endingDates = new HashMap<>();

    private void showCurrentTrip(){
        City startCity = FormController.tripToShowing.getStartCity();

        int number = 1;
        //List<DirectionsWaypoint> points = new ArrayList<>();
        addMarker(startCity, "Start");

        for (Edge edge : FormController.tripToShowing.getPlan()){
            startDates.put(edge.getEndCity().getName(), edge.getEndTime());
            endingDates.put(edge.getStartCity().getName(), edge.getStartTime());

            if (!startCity.getName().equals(edge.getEndCity().getName())) {
                addMarker(edge.getEndCity(), NumberConventer.convertNumber(number));
                number++;
            }
//            points.add(new DirectionsWaypoint(edge.getEndCity().getName() + ", " + edge.getEndCity().getCountry()));
//            System.out.println(edge.getEndCity().getName() + ", " + edge.getEndCity().getCountry() + "  :::  " + points.get(points.size() - 1).getVariableName());

            City A = edge.getStartCity();
            City B = edge.getEndCity();

            DirectionsRequest request =
                    new DirectionsRequest(A.getName() + ", " + A.getCountry(),
                            B.getName() + ", " + B.getCountry(), TravelModes.DRIVING);

            directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
            directionsService.getRoute(request, this, directionsRenderer);

        }
        //Collections.reverse(points);

        //Direction adding

        //BUILD ROAD IN BAD ORDER
/*
        Platform.runLater(()->{
            DirectionsRequest request =
                    new DirectionsRequest(startCity.getName() + ", " + startCity.getCountry(),
                            startCity.getName() + ", " + startCity.getCountry(), TravelModes.DRIVING, points.toArray(new DirectionsWaypoint[points.size()]));
            directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
            directionsService.getRoute(request, this, directionsRenderer);
            addMarker(startCity, "Start");
        });
 */

    }
    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        options.center(new LatLong(52.047472,29.243783))
                .zoomControl(true)
                .zoom(5)
                .overviewMapControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);
        map = mapView.createMap(options);

        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();

        showCurrentTrip();

/*
        //Markers and info in that  --SAMPLE
        LatLong latLong = new LatLong(62.047472,29.243783);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLong);
        markerOptions.title("ABC");
        Marker marker = new Marker(markerOptions);
        map.addMarker(marker);
        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("<h2>My sample marker</h2>"
                + "Current Location: Safeway<br>"
                + "ETA: 45 minutes" );
        InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
        infoWindow.open(map, marker);
        LatLong latLong2 = new LatLong(52.047472,29.243783);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(latLong2);
        markerOptions2.title("ABC");
        Marker marker2 = new Marker(markerOptions2);
        map.addMarker(marker2);
        InfoWindowOptions infoWindowOptions2 = new InfoWindowOptions();
        infoWindowOptions2.content("<h2>My sample marker 2</h2>"
                + "Current Location: Safeway<br>"
                + "ETA: 45 minutes" );
        InfoWindow infoWindow2 = new InfoWindow(infoWindowOptions2);
        infoWindow2.open(map, marker2);
        //Directions    --SAMPLE
        DirectionsWaypoint[] directionsWaypoints = {new DirectionsWaypoint("Vienna, Austria"),
                new DirectionsWaypoint("Berlin, Germany"),
                new DirectionsWaypoint("Warszaw, Poland"),
                new DirectionsWaypoint("Mink, Belarus"),
                new DirectionsWaypoint("Moskow, Russia"),
                new DirectionsWaypoint("Mozyr, Belarus")};
        DirectionsRequest request =
        new DirectionsRequest(latLong.getLatitude() + ", " + latLong.getLongitude(),
                latLong.getLatitude() + ", " + latLong.getLongitude(), TravelModes.DRIVING, directionsWaypoints);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
 */
/*
        from = "Krakow";
        to = "Krakow, Poland";
        DirectionsRequest request = new DirectionsRequest(from, to, TravelModes.DRIVING, directionsWaypoints);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
 */


        /*
        to = "Warszaw, Poland";
        from = "Krakow";
        DirectionsRequest request = new DirectionsRequest(from, to, TravelModes.DRIVING);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
        from = to;
        to = "Mozyr";
        request = new DirectionsRequest(from, to, TravelModes.DRIVING);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
        from = to;
        to = "Kiev";
        request = new DirectionsRequest(from, to, TravelModes.DRIVING);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsRenderer.setOptions("name: C  ");
        directionsService.getRoute(request, this, directionsRenderer);
        from = to;
        to = "Kraków";
        request = new DirectionsRequest(from, to, TravelModes.DRIVING);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
 */
        System.out.println("map initialized");
    }

    @Override
    public void directionsReceived(DirectionsResult directionsResult, DirectionStatus directionStatus) {

    }

}