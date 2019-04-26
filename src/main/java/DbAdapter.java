import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

@SuppressWarnings("all")
public class DbAdapter {
    private String jdbUrl = "jdbc:postgresql://localhost:5432/";
    private String usename = "postgres";
    private String password = "postgres";

    static Connection connection = null;
    static Statement statement = null;
    private ResultSet resultSet = null;

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
            if (resultSet != null) resultSet.close();
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
    static public Integer getCityID(String name){
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
    static public String getCityName(Integer ID){
        try {
            statement = connection.createStatement();
            String query="Select name from cities where name=\'"+ID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){return result.getString("name");}
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    static public City getCityFromID(Integer ID){
        try {
            statement = connection.createStatement();
            String query="Select * from cities where name=\'"+ID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){
                return new City(ID,result.getString("name"),result.getDouble("rating"),result.getInt("price"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    static public List< ? extends Edge > getNeighbours(Integer cityID){ //geting list of cityID's neighbours
        ArrayList<Edge> a=new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query="Select * from cities where start_city=\'"+cityID+"\'";
            ResultSet result=statement.executeQuery(query);
            while (result.next()) {
                City r1=getCityFromID(cityID);
                City r2=getCityFromID(result.getInt("end_city"));
                Edge b=new Edge(result.getInt("id"),r1,r2,result.getInt("price"),result.getDate("departure"),result.getDate("arrival"));
                a.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }
    static public ArrayList<String> getCityList(){
        ArrayList<String> a =new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query="Select name from cities";
            ResultSet result=statement.executeQuery(query);
            while (result.next()){a.add(result.getString("name"));}
            return a;
        } catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }
}
