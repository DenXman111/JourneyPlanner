import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Double.max;


public class ErrorWindow{
    private String message;
    static private Stage stage;
    protected ErrorWindow(String message){
        System.out.println(message);
        if (stage != null){
            stage.close();
        }
        this.message = message;
        stage = new Stage();
        StackPane pane = new StackPane();
        Text text = new Text(this.message);
        pane.getChildren().add(text);

        Scene scene = new Scene(pane, max(200, text.getLayoutBounds().getWidth() + 10), 100);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
