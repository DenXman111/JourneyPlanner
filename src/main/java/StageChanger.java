import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class StageChanger {

    private static Stage currentStage;

    public enum ApplicationStage {

        CITIES_MODER("xmlFiles/citiesModer.fxml", "cities and bus stops management", true),

        DATA_BASE_WINDOW("xmlFiles/dataBaseWindow.fxml", "database settings", false),

        FORM("xmlFiles/form.fxml", "form", true),

        HISTORY("xmlFiles/history.fxml", "your reservations", true),

        LOGIN("xmlFiles/login.fxml", "login", true),

        MODER("xmlFiles/moder.fxml", "transits management", true),

        SIGN_UP("xmlFiles/signUp.fxml", "sign up", true),

        WELCOME("xmlFiles/welcome.fxml", "JourneyPlanner", true),
        ;

        String url, title;
        boolean resizable;

        ApplicationStage(String url, String title, boolean resizable){
            this.url = url;
            this.title = title;
            this.resizable = resizable;
        }
    }

    public static void changeStage(Stage currentStage, String url, String title, boolean resizable) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(url));
        Pane pane = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle(title);

        Scene scene = new Scene(pane);

        newStage.setScene(scene);
        newStage.setResizable(resizable);
        newStage.show();
        if (currentStage != null)
            currentStage.close();
        StageChanger.currentStage = newStage;
    }


    public static void changeStage(ApplicationStage appStage) throws IOException {
        changeStage(currentStage, appStage.url, appStage.title, appStage.resizable);
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

    public static void displayLinesWindow(List<Line> lines) throws IOException {
        LinesController.setLines(lines);

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("xmlFiles/lines.fxml"));
        Pane pane = loader.load();

        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setTitle("lines");
        stage.setScene(scene);
        stage.show();
    }
}
