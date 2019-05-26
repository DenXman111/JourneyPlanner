import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {
    @SuppressWarnings("WeakerAccess")
    protected static String APIkey = "AIzaSyAE3KHmNCMilnkhmDhdMLvM2Nvpcbc1XaA";

    @SuppressWarnings("WeakerAccess")
    public static final ExecutorService daemonExecutor = Executors.newFixedThreadPool(1,
            r -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            });

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("JourneyPlanner");

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