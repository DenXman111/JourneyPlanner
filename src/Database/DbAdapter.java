package Database;

import javafx.scene.input.Dragboard;

import java.sql.*;

public class DbAdapter {
    private String jdbUrl = "jdbc:postgresql://localhost:5432/journeyplannerdatabase";
    private String usename = "postgres";
    private String password = "postgrespassword";

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public DbAdapter(){

    }

    public void connect(){
        try {
            connection = DriverManager.getConnection(jdbUrl, usename, password);
            System.out.println("Connected to database!!!");
        } catch (SQLException e){
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
