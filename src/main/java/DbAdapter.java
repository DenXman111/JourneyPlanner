import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class DbAdapter {
    private static String user = "application_user";

    private static String password = "qjNds5HVPTMTuCy";

    private static String socketFactory = "com.google.cloud.sql.postgres.SocketFactory";

    private static String jdbcUrl = "jdbc:postgresql://google/postgres?cloudSqlInstance=ferrous-phoenix-241520:europe-north1:journey-planer";

    private static Connection connection = null;


    public static synchronized void connect() throws SQLException, IOException {
        connection = DriverManager.getConnection(jdbcUrl + "&socketFactory=" + socketFactory + "&user=" + user + "&password=" + password);
    }

    public static synchronized void disconnect(){
        try {
            if (connection != null) connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean connected(){
        return connection != null;
    }

    public static Integer getCityID(String name) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="Select id from cities where name=\'"+name+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){return result.getInt("id");}
            return -1;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        finally {
            if (statement != null) statement.close();
        }
    }

    public static City getCityFromID(Integer ID) throws DatabaseException, SQLException {
        if (City.downloadedCities.containsKey(ID))
            return new City(City.downloadedCities.get(ID));
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="Select * from cities where id=\'"+ID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){
                City city = new City(
                        ID,
                        result.getString("name"),
                        result.getDouble("rating"),
                        result.getInt("average_price"),
                        result.getString("country")
                );
                City.downloadedCities.putIfAbsent(ID, city);
                return city;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) statement.close();
        }
        return null;
    }

    public static List<City> getCitiesList() throws SQLException {
        List<City> cityList = new ArrayList<>();

        String query = "select * from cities";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                City city = new City(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getDouble("rating"),
                        result.getInt("average_price"),
                        result.getString("country")
                );
                cityList.add(city);
            }
        }finally {
            if (statement != null) statement.close();
        }

        return cityList;
    }

    public static List<BusStop> getBusStopsList() throws SQLException {
        List<BusStop> stopsList = new ArrayList<>();

        String query =
                "select bs.id, bs.stop_name, c.name, bs.city from bus_stops bs join cities c on bs.city = c.id";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                BusStop stop = new BusStop(
                        result.getInt("id"),
                        result.getString("stop_name"),
                        result.getString("name"),
                        result.getInt("city")
                );
                stopsList.add(stop);
            }
        }finally {
            if (statement != null) statement.close();
        }

        return stopsList;
    }


    public static void addNewUser(String username, String password, String email, String name, String surname) throws SQLException{
        Statement statement = connection.createStatement();
        String query="INSERT INTO new_users(username, password, email_address, name, surname) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);
        pst.setString(2, password);
        pst.setString(3, email);
        pst.setString(4, name);
        pst.setString(5, surname);
        pst.executeUpdate();
        statement.close();
    }
    public static void addNewBus(String cityA, String cityB, int price, LocalDate departure, LocalDate arrival, int places) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "INSERT INTO buses VALUES(nextval(\'buses_seq\'), ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, getCityID(cityA));
        pst.setInt(2, getCityID(cityB));
        pst.setInt(3, price);
        pst.setDate(4, Date.valueOf(departure));
        pst.setDate(5, Date.valueOf(arrival));
        pst.setInt(6, places);
        pst.executeUpdate();
        statement.close();
    }

    public static boolean haveBusWithID(int id) throws SQLException{

        Statement statement = connection.createStatement();
        String query = "Select count(*) from transits where id = \'"+id+"\'";
        ResultSet result=statement.executeQuery(query);
        statement.close();
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static void removeBusByID(int id) throws SQLException{
        if (!haveBusWithID(id)) throw new SQLException();
        Statement statement = connection.createStatement();
        String query = "DELETE FROM transits WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
        statement.close();
    }

    public static boolean userExists(String username, String password) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="Select * from loginUser('"+ username + "', '" + password  + "')";
            ResultSet result=statement.executeQuery(query);
            if (result.next()){
                 return result.getBoolean(1);
            } else return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        finally {
            if (statement != null) statement.close();
        }
    }

    public static boolean loginModerator(String username, String password) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="Select * from loginModerator('"+ username + "', '" + password  + "')";
            ResultSet result=statement.executeQuery(query);
            if (result.next()){
                return result.getBoolean(1);
            } else return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        finally {
            if (statement != null) statement.close();
        }
    }

    //geting list of cityID's neighbours
    public static List< Edge > getNeighbours(City stratCity, Timestamp begin, Timestamp end) throws SQLException {
        ArrayList<Edge> a=new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="Select * from buses_from_city(" + stratCity.getID() + ", '"
                    + begin.toLocalDateTime() + "'::date, '" + end.toLocalDateTime() + "'::date)";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                City r2= getCityFromID(result.getInt("end_city"));
                Edge b=new Edge(result.getInt("id_transit"), stratCity, r2,
                        result.getInt("price"),
                        result.getTimestamp("departure"),
                        result.getTimestamp("arrival"));
                a.add(b);
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) statement.close();
        }
        return a;
    }



    public static Map<Integer, List<Edge>> getAllAvailableTransits(Date begin, Date end, int seats) throws SQLException {
        Map<Integer, List<Edge>> map = new ConcurrentHashMap<>();

        Statement statement = null;
        try{
            statement = connection.createStatement();

            String query = "select * from get_buses_with_seats_left('"
                    + begin + "'::date, '" + end + "'::date, " + seats + " )";

            ResultSet result = statement.executeQuery(query);

            while (result.next()){
                City startCity = getCityFromID(result.getInt("start_city"));
                City endCity = getCityFromID(result.getInt("end_city"));
                Edge edge = new Edge(
                        result.getInt("id_transit"),
                        startCity,
                        endCity,
                        result.getInt("price"),
                        result.getTimestamp("departure"),
                        result.getTimestamp("arrival") );

                map.putIfAbsent(startCity.getID(), new ArrayList<>());
                map.get(startCity.getID()).add(edge);
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) statement.close();
        }

        return map;
    }



    public static ArrayList<String> getCityNamesList() throws SQLException {
        ArrayList<String> a =new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="Select name from cities";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){a.add(result.getString("name"));}
            Collections.sort(a);
            return a;
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) statement.close();
        }
        Collections.sort(a);
        return a;
    }

    public static List< EdgesInOut > getCitiesBetween(Integer prev_cityID,Integer next_cityID, Timestamp begin, Timestamp end) throws SQLException {
        Statement statement = null;
        ArrayList<EdgesInOut> a=new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query="select * from optional_visits(" + prev_cityID + ", '" + begin + "'::timestamp ,"
                    + next_cityID +", '" + end + "'::timestamp )";
            ResultSet result=statement.executeQuery(query);
            while (result.next()) {
                City r1=getCityFromID(prev_cityID);
                City r2=getCityFromID(result.getInt("tr1_end_city"));
                Edge b=new Edge(result.getInt("id_tr1"),r1,r2,
                        result.getInt("tr1_price"),
                        result.getTimestamp("tr1_departure"),
                        result.getTimestamp("tr1_arrival"));
                City r3=getCityFromID(next_cityID);
                Edge c=new Edge(result.getInt("id_tr2"),r2,r3,
                        result.getInt("tr2_price"),
                        result.getTimestamp("tr2_departure"),
                        result.getTimestamp("tr2_arrival"));
                EdgesInOut d=new EdgesInOut(b,c);
                a.add(d);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) statement.close();
        }
        return a;
    }
    public static ArrayList <PastTrip> getHistory(String user) throws SQLException {
        ArrayList <PastTrip> tripList = new ArrayList();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query="select * from user_reservations('" + user + "')";
            ResultSet result=statement.executeQuery(query);

            PastTrip pastTrip = null;
            while (result.next()){
                int reservationId = result.getInt("reservation_id");

                if (pastTrip == null || pastTrip.getId() != reservationId){
                    if (pastTrip != null) tripList.add(pastTrip);
                    int people = ((Integer[])result.getArray("reserved_seats").getArray()).length;
                    pastTrip = new PastTrip(reservationId, people);
                }

                pastTrip.reservation.addTransitReservation(pastTrip.reservation.new TransitReservation(
                        result.getInt("transit_id"),
                        result.getTimestamp("departure"),
                        result.getString("departure_stop"),
                        result.getString("arrival_stop"),
                        (Integer[])result.getArray("reserved_seats").getArray()
                ));

                City departureCity = getCityFromID(result.getInt("departure_city"));
                City arrivalCity = getCityFromID(result.getInt("arrival_city"));
                pastTrip.trip.pushEdge(new Edge(
                        result.getInt("transit_id"),
                        departureCity,
                        arrivalCity,
                        result.getInt("transit_price"),
                        result.getTimestamp("departure"),
                        result.getTimestamp("arrival")
                ));
            }
            if (pastTrip != null) tripList.add(pastTrip);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) statement.close();
        }
        return tripList;
    }



    public static Reservation reserve(Trip trip, String user, int seats) throws SQLException {
        Statement statement = null;

        if (trip == null || user == null) return null;

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("select * from reserve('" + user + "', " + seats);
        trip.getPlan().forEach( edge -> {
            queryBuilder.append(", row(" + edge.getBusId() + ", '" + edge.getStartTime() + "'::timestamp )");
        });
        queryBuilder.append(" )");
        Reservation reservation = new Reservation();

        try {
            statement = connection.createStatement();

            ResultSet result =  statement.executeQuery(queryBuilder.toString());

            while (result.next())
                reservation.addTransitReservation(
                        reservation.new TransitReservation(
                                result.getInt("transit_id"),
                                result.getTimestamp("departure_time"),
                                result.getString("departure_stop"),
                                result.getString("arrival_stop"),
                                (Integer[])result.getArray("reserved_seats").getArray()
                        ));

        }
        finally {
            if (statement != null) statement.close();
        }

        return reservation;
    }


    public static void cancelReservation(int reservation_id) throws SQLException {
        String deleteStatement = "delete from reservations where id = " + reservation_id;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
        preparedStatement.executeUpdate();
    }
}
