import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {

    @SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
    protected static String APIkey = "AIzaSyAE3KHmNCMilnkhmDhdMLvM2Nvpcbc1XaA";

    @SuppressWarnings("WeakerAccess")
    public static ExecutorService daemonExecutor = Executors.newFixedThreadPool(1,
            r -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            });

    @SuppressWarnings("WeakerAccess")
    public static void restartDaemonExecutor(){
        daemonExecutor.shutdownNow();
        daemonExecutor = Executors.newFixedThreadPool(1,
                r -> {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                });
    }

    @SuppressWarnings("WeakerAccess")
    public static final ExecutorService executors = Executors.newFixedThreadPool(3,
            r -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            });

    @Override
    public void start(Stage primaryStage) throws Exception{
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }


    public static void main(String[] args) {

        try{
            launch(args);
        } finally {
            DbAdapter.disconnect();
        }
    }
}