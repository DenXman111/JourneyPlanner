import java.io.FileInputStream;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("all")
public class DbAdapter {
    private String jdbUrl = "jdbc:postgresql://localhost:5432/";
    private String usename = "postgres";
    private String password = "postgres";

    private static Connection connection = null;
    private static Statement statement = null;

    public DbAdapter(){

    }

    public void connect(){
        try {
            connection = DriverManager.getConnection(jdbUrl, usename, password);
        } catch (SQLException e){
            e.printStackTrace();
            System.exit(0);
        }
    }
    public void create(){
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DROP DATABASE IF EXISTS JourneyPlanner");
            statement.executeUpdate("CREATE DATABASE JourneyPlanner");
            System.out.println("Database created successfully...");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void create_tables(String arg) {
        try {
            statement = connection.createStatement();
            Scanner scanner = new Scanner(new FileInputStream("src/main/Database/" + arg));
            String tmp = "";
            while (scanner.hasNext()) {
                tmp = tmp + scanner.nextLine();
            }
            statement.executeUpdate(tmp);
            System.out.println("Tables created successfully...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Integer getCityID(String name){
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
    }

    public static City getCityFromID(Integer ID) throws DatabaseException{
        try {
            statement = connection.createStatement();
            String query="Select * from cities where id=\'"+ID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){
                return new City(ID,result.getString("name"),result.getDouble("rating"),result.getInt("average_price"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getCountryFromID(Integer ID){
        try {
            statement = connection.createStatement();
            String query="Select * from cities where id=\'"+ID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){
                return result.getString("country");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void addNewUser(String username, String password, String email, String name, String surname) throws SQLException{
        statement = connection.createStatement();
        String query="INSERT INTO users(username, password, email, name, surname) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);
        pst.setString(2, password);
        pst.setString(3, email);
        pst.setString(4, name);
        pst.setString(5, surname);
        pst.executeUpdate();
    }
    public static void addNewBus(String cityA, String cityB, int price, LocalDate departure, LocalDate arrival, int places) throws SQLException{
        statement = connection.createStatement();
        String query = "INSERT INTO buses VALUES(nextval(\'buses_seq\'), ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, getCityID(cityA));
        pst.setInt(2, getCityID(cityB));
        pst.setInt(3, price);
        pst.setDate(4, Date.valueOf(departure));
        pst.setDate(5, Date.valueOf(arrival));
        pst.setInt(6, places);
        pst.executeUpdate();
    }

    public static boolean haveBusWithID(int id) throws SQLException{

        statement = connection.createStatement();
        String query="Select count(*) from buses where id = \'"+id+"\'";
        ResultSet result=statement.executeQuery(query);
        if (result.next()){
            if (result.getInt("count") == 0) return false; else
                return true;
        } else return false;
    }

    public static void removeBusByID(int id) throws SQLException{
        if (!haveBusWithID(id)) throw new SQLException();
        statement = connection.createStatement();
        String query = "DELETE FROM buses WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public static boolean haveUser(String username, String password){
        try {
            statement = connection.createStatement();
            String query="Select count(*) from users where username = \'"+username+"\' AND password = \'"+password+"\'";
            ResultSet result=statement.executeQuery(query);
            if (result.next()){
                if (result.getInt("count") == 0) return false; else
                    return true;
            } else return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean haveModer(String username, String password){
        try {
            statement = connection.createStatement();
            String query="Select count(*) from moderators where username = \'"+username+"\' AND password = \'"+password+"\'";
            ResultSet result=statement.executeQuery(query);
            if (result.next()){
                if (result.getInt("count") == 0) return false; else
                    return true;
            } else return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static List< Edge > getNeighbours(Integer cityID){ //geting list of cityID's neighbours
        ArrayList<Edge> a=new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query="Select * from buses where start_city=\'"+cityID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()) {
                City r1=getCityFromID(cityID);
                City r2=getCityFromID(result.getInt("end_city"));
                Edge b=new Edge(result.getInt("id"),r1,r2,result.getInt("price"),
                        result.getDate("departure").toLocalDate(),
                        result.getDate("arrival").toLocalDate());
//                        result.getDate("departure").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
//                        result.getDate("arrival").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                a.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }

    public static ArrayList<String> getCityList(){
        ArrayList<String> a =new ArrayList<>();
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
        Collections.sort(a);
        return a;
    }
    public static List< EdgesInOut > getCitiesBetween(Integer prev_cityID,Integer next_cityID){
        ArrayList<EdgesInOut> a=new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query="Select * from( select end_city as ec1, id as id1, price as p1, departure as d1, arrival as a1 from buses where start_city=\'"+prev_cityID+"\') b1 join (select start_city as sc2, end_city as ec2, id as id2, price as p2, departure as d2, arrival as a2 from buses where end_city=\'"+next_cityID+"\') b2 on ec1=sc2";
            ResultSet result=statement.executeQuery(query);
            while (result.next()) {
                City r1=getCityFromID(prev_cityID);
                City r2=getCityFromID(result.getInt("ec1"));
                Edge b=new Edge(result.getInt("id1"),r1,r2,result.getInt("p1"),
                        result.getDate("d1").toLocalDate(),
                        result.getDate("a1").toLocalDate());
                City r3=getCityFromID(next_cityID);
                Edge c=new Edge(result.getInt("id2"),r2,r3,result.getInt("p2"),
                        result.getDate("d2").toLocalDate(),
                        result.getDate("a2").toLocalDate());
                EdgesInOut d=new EdgesInOut(b,c);
                a.add(d);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }
    public static ArrayList <Trip> getHistory(String user) {
        ArrayList <Trip> out=new ArrayList();
        try {
            statement = connection.createStatement();
            String query="select distinct id from trips where traveler=\'"+user+"\'";
            ResultSet result=statement.executeQuery(query);
            ArrayList<String> id_list=new ArrayList<>();
            while (result.next()) {
                id_list.add(result.getString("id"));
            }
            for(int i=0;i<id_list.size();i++) {
                Trip tmp=new Trip();
                String tr = id_list.get(i);
                String query2 = "Select buses.* from buses join (select * from trips where id=\'" + tr + "\' and traveler=\'"+user+"\') as a on buses.id=a.bus_id order by a.id,departure;";
                ResultSet result2 = statement.executeQuery(query2);
                while (result2.next()) {
                    System.out.println(tr);
                    City r1 = getCityFromID(result2.getInt("start_city"));
                    City r2 = getCityFromID(result2.getInt("end_city"));
                    Edge b = new Edge(result2.getInt("id"), r1, r2, result2.getInt("price"),
                            result2.getDate("departure").toLocalDate(),
                            result2.getDate("arrival").toLocalDate());
                    tmp.pushEdge(b);
                }
                out.add(tmp);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }
    public static void reserve(Trip arg,String user){
        try {
            statement = connection.createStatement();
            List<Edge> a=arg.getPlan();
            for (int i = 0; i < a.size(); i++) {
                int g = a.get(i).getBusId();
                if (i==0){
                    String query = "insert into trips values (nextval('res_id'), "+g+", '"+user+"')";
                    statement.executeUpdate(query);
                }
                else {
                    String query = "insert into trips values (currval('res_id'), " + g + ", '" + user + "')";
                    statement.executeUpdate(query);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
