import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class StageChanger {

    private static Stage currentStage;

    public enum ApplicationStage {

        CITIES_MODER("xmlFiles/citiesModer.fxml", "cities and bus stops management"),

        FORM("xmlFiles/form.fxml", "form"),

        HISTORY("xmlFiles/history.fxml", "your reservations"),

        LOGIN("xmlFiles/login.fxml", "login"),

        MODER("xmlFiles/moder.fxml", "transits management"),

        SIGN_UP("xmlFiles/signUp.fxml", "sign up"),

        WELCOME("xmlFiles/welcome.fxml", "JourneyPlanner"),
        ;

        String url, title;

        ApplicationStage(String url, String title){
            this.url = url;
            this.title = title;
        }
    }

    public static void changeStage(Stage currentStage, String url, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(url));
        Pane pane = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle(title);

        Scene scene = new Scene(pane);

        newStage.setScene(scene);
        newStage.show();
        if (currentStage != null)
            currentStage.close();
        StageChanger.currentStage = newStage;
    }


    public static void changeStage(ApplicationStage appStage) throws IOException {
        changeStage(currentStage, appStage.url, appStage.title);
    }

    protected static Trip tripToDisplay;
    public static void displayMapWindow(Trip trip) throws IOException {
        tripToDisplay = trip;

        FXMLLoader myLoader = new FXMLLoader(Main.class.getResource("/xmlFiles/map.fxml"));
        Pane myPane = myLoader.load();

        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Map");
        stage.setScene(scene);
        stage.show();
    }
}
