import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
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

    @SuppressWarnings("WeakerAccess")
    public static final ExecutorService executors = Executors.newFixedThreadPool(3,
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

        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(Main.class.getResourceAsStream("jpConnection.json"))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            System.out.println("Buckets:");
            Page<Bucket> buckets = storage.list();
            for (Bucket bucket : buckets.iterateAll()) {
                System.out.println(bucket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            launch(args);
        } finally {
            DbAdapter.disconnect();
        }
    }
}