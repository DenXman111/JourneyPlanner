import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("JourneyPlanner");

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/xmlFiles/welcome.fxml"));

        Pane myPane = myLoader.load();
        WelcomeController controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
/*
        JarAdder jarAdder = new JarAdder();
        try {
            jarAdder.addJarToClasspath(new File("src/main/libs/jars/jfoenix-9.0.8.jar"));
        } catch (Exception e) {
            e.printStackTrace();
        }

 */
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