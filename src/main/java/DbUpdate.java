import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;

@SuppressWarnings("WeakerAccess")
public class DbUpdate {
    public static void execute(Callable<?> dbFunction, Runnable platformFunction){
        Main.executors.submit(new Task<Integer>() {
            @Override
            protected Integer call(){
                try{
                    dbFunction.call();
                }catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater( () -> new ErrorWindow(e.getMessage()) );
                }
                Platform.runLater(platformFunction);
                return 100;
            }
        });
    }
}
