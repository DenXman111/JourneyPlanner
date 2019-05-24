import java.sql.*;
import java.util.ArrayList;

public class GCloudConnectionTest {
    @SuppressWarnings("SqlResolve")
    public static void main(String[] args){
        System.out.println("Start");
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("message me for text - Ł.S.");
            statement = connection.createStatement();
            String query = "Select * from guestbook";
            ResultSet result = statement.executeQuery(query);

            ArrayList<String> guests = new ArrayList<>();
            while (result.next()) guests.add(result.getString("content"));
            System.out.println(guests);
            System.out.println("(:");
        } catch (SQLException e) {
            System.out.println(":(");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("not found :(");
            e.printStackTrace();
        }
        finally {
            try{
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
