import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;

@SuppressWarnings("WeakerAccess")
public class DbUpdate {
    public static void execute(Callable<?> dbFunction, Runnable platformFunction){
        execute(dbFunction, platformFunction, null);
    }

    public static void execute(Callable<?> dbFunction, Runnable platformFunction, String message){
        Main.executors.submit(new Task<Integer>() {
            @Override
            protected Integer call(){
                try{
                    dbFunction.call();
                }catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater( () -> new ErrorWindow(message == null ? e.getMessage() : message) );
                }
                Platform.runLater(platformFunction);
                return 100;
            }
        });
    }
}
