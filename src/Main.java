import Controllers.MainController;
import Controllers.WelcomeController;
import Database.DbAdapter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("JourneyPlanner");

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();
        dbAdapter.create();
        dbAdapter.create_tables("DB.sql");
        launch(args);
        dbAdapter.disconnect();
    }
}

