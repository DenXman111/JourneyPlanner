import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class DbAdapter {
    public static String user = "postgres";

    public static String password = "postgres";

    public static String jdbcUrl = "jdbc:postgresql://localhost:5432/";

    private static Connection connection = null;


    public static synchronized void connect() throws SQLException, IOException {
        connection = DriverManager.getConnection(jdbcUrl, user, password);
    }

    public static void dropAll(){
        try {
            System.out.println("Beginning overwriting tables");
            Statement statement = connection.createStatement();
            Scanner scanner = new Scanner(Main.class.getResourceAsStream("DataBase/clear.sql"));
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                builder.append('\n').append(scanner.nextLine());
            }
            statement.executeUpdate(builder.toString());
            System.out.println("Tables cleared successfully...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        try {
            Statement statement = connection.createStatement();
            Scanner scanner = new Scanner(Main.class.getResourceAsStream("DataBase/create.sql"));
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                 builder.append('\n').append(scanner.nextLine());
            }
            statement.executeUpdate(builder.toString());
            System.out.println("Tables created successfully...");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        result.getDouble("average_price"),
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

        String query = "select *, city_has_stops(id) as has_stops from cities order by id";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                City city = new City(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getDouble("rating"),
                        result.getDouble("average_price"),
                        result.getString("country")
                );
                city.setHasStops(result.getBoolean("has_stops"));
                cityList.add(city);
            }
        }finally {
            if (statement != null) statement.close();
        }

        return cityList;
    }

    public static List<BusStop> getBusStopsList() throws SQLException, DatabaseException {
        List<BusStop> stopsList = new ArrayList<>();

        String query =
                "select id, stop_name, city, exists_transits_with_stop(id) as unmodifiable from bus_stops bs order by bs.id";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                BusStop stop = new BusStop(
                        result.getInt("id"),
                        result.getString("stop_name"),
                        getCityFromID(result.getInt("city"))
                );
                stop.setModifiable(result.getBoolean("unmodifiable"));
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

    public static void addNewLine(int depstop, int arrstop, int price, int bus_type ) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO transits VALUES(nextval(\'transit_id\'), ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, depstop);
        pst.setInt(2, arrstop);
        pst.setInt(3, price);
        pst.setInt(4, bus_type);
        pst.executeUpdate();
        statement.close();
    }

    public static void assignSpanToLine(int transit,LocalDate start, LocalDate end) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "INSERT INTO spans(begin_date,end_date,transit) VALUES(?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setDate(1, Date.valueOf(start));
        pst.setDate(2, Date.valueOf(end));
        pst.setInt(3, transit);
        pst.executeUpdate();
        statement.close();
    }

    public static void addDepartureTime(String departureTime, String duration,int exceptionSpan, String weekday) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "INSERT INTO departure_time VALUES(?,\'"+duration+"\', ?,"+weekday+")";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setTime(1, Time.valueOf(departureTime));
        pst.setInt(2, exceptionSpan);
        pst.executeUpdate();
        statement.close();
    }

    public static void addBreak(int span, LocalDate data) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "INSERT INTO breaks VALUES(?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setDate(1, Date.valueOf(data));
        pst.setInt(2, span);
        pst.executeUpdate();
        statement.close();
    }

    public static boolean haveBreak(int span, LocalDate data) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "Select count(*) from breaks where date=\'"+data+"\' and span_id=\'"+span+"\'";
        ResultSet result=statement.executeQuery(query);
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static boolean deleteBreak(int span, LocalDate data) throws SQLException{
        if(haveBreak(span,data)) {
            Statement statement = connection.createStatement();
            String query = "delete from breaks where date=? and span_id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setDate(1, Date.valueOf(data));
            pst.setInt(2, span);
            pst.executeUpdate();
            statement.close();
            return true;
        }
        return false;
    }

    public static boolean haveDepartureTime(String departureTime, String duration,int exceptionSpan, String weekday) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "Select count(*) from departure_time where departure=\'"+departureTime+"\' and time=\'"+duration+"\' and span=\'"+exceptionSpan+"\' and day_of_the_week="+weekday;
        ResultSet result=statement.executeQuery(query);
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static boolean deleteDepartureTime(String departureTime, String duration,int exceptionSpan, String weekday) throws SQLException{
        if(haveDepartureTime(departureTime,duration,exceptionSpan,weekday)) {
            removeReservationbyDepartureTime(departureTime,weekday,exceptionSpan);
            Statement statement = connection.createStatement();
            String query = "delete from departure_time where departure=\'"+departureTime+"\' and time=\'" + duration + "\' and span=\'"+exceptionSpan+"\' and day_of_the_week=" + weekday;
            statement.executeUpdate(query);
            statement.close();
            return true;
        }
        return false;
    }

    public static boolean haveSpanInLine(int transit,LocalDate start, LocalDate end) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "Select count(*) from spans where begin_date=\'"+start+"\' and end_date=\'"+end+"\' and transit=\'"+transit+"\'";
        ResultSet result=statement.executeQuery(query);
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static void removeSpanByID(int id) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "DELETE FROM departure_time WHERE span= ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
        statement.close();
        Statement statement2 = connection.createStatement();
        String query2 = "DELETE FROM spans WHERE id = ?";
        PreparedStatement pst2 = connection.prepareStatement(query2);
        pst2.setInt(1, id);
        pst2.executeUpdate();
        pst2.close();
    }

    public static boolean deleteSpanFromLine(int transit,LocalDate start, LocalDate end) throws SQLException{
        if(haveSpanInLine(transit,start,end)) {
            Statement statement = connection.createStatement();
            String query = "select id from spans where begin_date=\'"+start+"\' and end_date=\'"+end+"\' and transit=\'"+transit+"\'";
            ResultSet result=statement.executeQuery(query);
            ArrayList<Integer> spany=new ArrayList<>();
            while(result.next()){
                spany.add(result.getInt("id"));
            }
            statement.close();
            for(int i=0;i<spany.size();i++){
                removeReservationbySpan(spany.get(i));
                removeSpanByID(spany.get(i));
            }
            return true;
        }
        return false;
    }


    public static boolean haveTransitWithID(int id) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "Select count(*) from transits where id_transit = \'"+id+"\'";
        ResultSet result=statement.executeQuery(query);
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static void removeReservationbyTransit(int tr) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "Select id,reservation from transit_reservation where transit = \'"+tr+"\'";
        ResultSet result=statement.executeQuery(query);
        ArrayList<Integer> seats=new ArrayList<>();
        ArrayList<Integer> reser=new ArrayList<>();
        while(result.next()){
            seats.add(result.getInt("id"));
            reser.add(result.getInt("reservation"));
        }
        statement.close();
        for(int i=0;i<seats.size();i++){
            removeSeatByID(seats.get(i));
        }
        for(int i=0;i<reser.size();i++){
            cancelReservation(reser.get(i));
        }
        Statement statement1 = connection.createStatement();
        String query1 = "delete from transit_reservation where transit = \'"+tr+"\'";
        statement1.executeUpdate(query1);
        statement1.close();
    }

    public static int getTransitFromSpan(int span) throws SQLException{
        Statement statement = connection.createStatement();
        String query= "select transit from spans where id=\'"+span+"\'";
        ResultSet result=statement.executeQuery(query);
        while(result.next())
            return result.getInt("transit");
        return 0;
    }

    public static void removeReservationbySpan(int span) throws  SQLException{
        Statement statement = connection.createStatement();
        String query= "select * from departure_time where span=\'"+span+"\'";
        ResultSet result=statement.executeQuery(query);
        ArrayList<String> dep=new ArrayList<>();
        ArrayList<String> day=new ArrayList<>();
        while(result.next()){
            dep.add(result.getString("departure"));
            day.add(result.getString("day_of_the_week"));
        }
        statement.close();
        for(int i=0;i<dep.size();i++)
        {
            removeReservationbyDepartureTime(dep.get(i),day.get(i),span);
        }
    }

    public static void removeReservationbyDepartureTime(String dep, String day, int span) throws SQLException{
        int tran=getTransitFromSpan(span);
        Statement statement = connection.createStatement();
        String query = "select id, reservation from transit_reservation where transit=\'"+tran+"\' and departure_date::time=\'"+dep+"\' and extract(isodow from departure_date)=\'"+span+"\'";
        ResultSet result=statement.executeQuery(query);
        ArrayList<Integer> reser=new ArrayList<>();
        while(result.next()){
            reser.add(result.getInt("reservation"));
        }
        statement.close();
        for(int i=0;i<reser.size();i++){
            removeReservationsByID(reser.get(i));
        }
    }

    private static void removeSeatByID(Integer integer) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "delete from seat_reservation where transit_reservation_id = \'"+integer+"\'";
        statement.executeUpdate(query);
        statement.close();
    }

    public static void removeTransitByID(int id) throws SQLException{
        if (!haveTransitWithID(id)) throw new SQLException("No such line");
        Statement statement2=connection.createStatement();
        String query2= "select id from spans where transit=\'"+id+"\'";
        ResultSet result=statement2.executeQuery(query2);
        ArrayList<Integer> spany=new ArrayList<>();
        while(result.next()){
            spany.add(result.getInt("id"));
        }
        statement2.close();
        for(int i=0;i<spany.size();i++){
            removeSpanByID(spany.get(i));
        }
        removeReservationbyTransit(id);
        Statement statement = connection.createStatement();
        String query = "select reservation FROM transit_reservation WHERE transit = \'"+id+"\'";
        ResultSet result2=statement.executeQuery(query);
        ArrayList<Integer> reser=new ArrayList<>();
        while(result2.next()){
            reser.add(result.getInt("reservation"));
        }
        statement.close();
        for(int i=0;i<reser.size();i++){
            removeReservationsByID(reser.get(i));
        }
    }

    public static List<Line> getLines() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "select * from lines";
        ResultSet result = statement.executeQuery(query);
        List<Line> lines = new ArrayList<>();
        while (result.next()) {
            System.out.println(result.getInt("itr")+"    "+result.getString("stop1")+"      "+result.getString("stop2"));
            Line line = new Line(
                    result.getInt("itr"),
                    result.getString("stop1"),
                    result.getString("stop2")
            );
            lines.add(line);
        }
        return lines;
    }

    public static String getSpans(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select begin_date, end_date from spans where transit=\'"+id+"\'";
        ResultSet result = statement.executeQuery(query);
        String res="Start Date            End Date\n";
        while (result.next()) {
            System.out.println(result.getString("begin_date")+"      "+result.getString("end_date"));
            String t1=result.getString("begin_date");
            while(t1.length()<20)
                t1=t1+" ";
            res=res+t1+result.getString("end_date")+"\n";
        }
        return res;
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

    public static void removeReservationsByID(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "select id, transit, reservation from transit_reservation where reservation=\'"+id+"\'";
        ResultSet result=statement.executeQuery(query);
        ArrayList<Integer> seats=new ArrayList<>();
        while(result.next()){
            seats.add(result.getInt("id"));
        }
        statement.close();
        for(int i=0;i<seats.size();i++){
            removeSeatByID(seats.get(i));
        }
        cancelReservation(id);
        Statement statement1 = connection.createStatement();
        String query1 = "delete from transit_reservation where reservation=\'"+id+"\'";
        statement1.executeUpdate(query1);
        statement1.close();
    }

    public static int getIDFromParameters(int id1, int id2, LocalDate departure, LocalDate arrival) throws Exception
    {
        Statement statement = connection.createStatement();
        String query="Select id from buses WHERE start_city =\'"+id1+"\' and end_city=\'"+id2+"\' and departure=\'"+departure+"\' and arrival=\'"+arrival+"\'";
        ResultSet result=statement.executeQuery(query);
        if(result.next()){
            return result.getInt("id");
        }
        return -1;
    }

    public static boolean haveBusWithParameters(int id1, int id2, LocalDate departure, LocalDate arrival) throws SQLException{
        Statement statement = connection.createStatement();
        String query="Select count(*) from buses WHERE start_city =\'"+id1+"\' and end_city=\'"+id2+"\' and departure=\'"+departure+"\' and arrival=\'"+arrival+"\'";
        ResultSet result=statement.executeQuery(query);
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static void removeBusByParameters(int id1,int id2, LocalDate departure, LocalDate arrival) throws Exception{
        if (!haveBusWithParameters(id1,id2,departure,arrival)) throw new Exception();
        Statement statement = connection.createStatement();
        String query = "DELETE FROM buses WHERE start_city =\'"+id1+"\' and end_city=\'"+id2+"\' and departure=\'"+departure+"\' and arrival=\'"+arrival+"\'";
        statement.executeUpdate(query);
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
                        result.getDouble("price"),
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


    public static void uppdateCity(int id, String name, String country, double rating, double nightPrice) throws SQLException {
        String updateStatement =
                "update cities set name = '" + name
                        + "', country = '" + country
                        + "', rating = " + String.format(Locale.US,"%.2f", rating)
                        + ", average_price = " + String.format(Locale.US,"%.2f", nightPrice)
                        + " where id = " + id;

        PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
        preparedStatement.executeUpdate();
    }

    public static void addNewCity() throws SQLException {
        String insertStatement =
                "insert into cities (name, rating, average_price, country) values ('New City', 0, 0, 'Country');";

        PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
        preparedStatement.executeUpdate();
    }

    public static void deleteCity(int cityId) throws SQLException {
        String deleteStatement = "delete from cities where id = " + cityId;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
        preparedStatement.executeUpdate();
    }

    public static void updateStop(int id, String name, int cityId) throws SQLException {
        String updateStatement =
                "update bus_stops set stop_name = '" + name
                        + "', city = " + cityId
                        + " where id = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
        preparedStatement.executeUpdate();
    }

    public static void addNewStop() throws SQLException {
        String insertStatement =
                "insert into bus_stops(stop_name, city) VALUES ('New bus stop', (select id from cities limit 1));";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
        preparedStatement.executeUpdate();
    }

    public static void deleteStop(int stop_id) throws SQLException {
        String deleteStatement = "delete from bus_stops where id = " + stop_id;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
        preparedStatement.executeUpdate();
    }

}
