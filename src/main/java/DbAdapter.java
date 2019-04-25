import java.io.FileInputStream;
import java.sql.*;
import java.util.Scanner;

@SuppressWarnings("all")
public class DbAdapter {
    private String jdbUrl = "jdbc:postgresql://localhost:5432/";
    private String usename = "postgres";
    private String password = "postgres";

    private Connection connection = null;
    private Statement statement = null;
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
    public Integer starting_city(String name){
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
}