package Database;

import java.sql.*;

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
}
