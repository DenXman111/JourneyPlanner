import java.io.FileInputStream;
import java.sql.*;
import java.time.ZoneId;
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

    public static void addNewUser(String username, int password, String email, String name, String surname) throws SQLException{
//        try {
            statement = connection.createStatement();
//            String query="INSERT INTO users VALUES(" + username + ", " + password + ", " + email + ", " + name + ", " + surname + ")";
            String query="INSERT INTO users(username, password, email, name, surname) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setInt(2, password);
            pst.setString(3, email);
            pst.setString(4, name);
            pst.setString(5, surname);
            pst.executeUpdate();
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
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
}
