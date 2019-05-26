import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve", "FieldCanBeLocal"})
public class GCloudConnectionTest {

    private static String user = "application_user";

    private static String password = "qjNds5HVPTMTuCy";

    private static String socketFactory = "com.google.cloud.sql.postgres.SocketFactory";

    private static String jdbcUrl = "jdbc:postgresql://google/postgres?cloudSqlInstance=ferrous-phoenix-241520:europe-north1:journey-planer";


    public static void main(String[] args){
        System.out.println("Start");
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcUrl + "&socketFactory=" + socketFactory + "&user=" + user + "&password=" + password);
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
                if (connection != null) {
                    connection.close();
                    System.out.println("Closed connection");
                }
                if (statement != null) {
                    statement.close();
                    System.out.println("Closed statement");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
