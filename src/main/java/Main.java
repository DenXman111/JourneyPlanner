import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.lynden.gmapsfx.*;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("JourneyPlanner");

        com.lynden.gmapsfx.GoogleMapView googleMapView = null;

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        DbAdapter dbAdapter = new DbAdapter();
        try{
            dbAdapter.connect();
            dbAdapter.create();
            dbAdapter.create_tables("DB.sql");
            launch(args);
        } finally {
            dbAdapter.disconnect();
        }
    }
}