import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Double.max;


@SuppressWarnings("WeakerAccess")
public class LinesWindow {
    static private Stage stage;
    LinesWindow(String message){
        if (stage != null){
            stage.close();
        }
        stage = new Stage();
        StackPane pane = new StackPane();
        Text text = new Text(message);
        pane.getChildren().add(text);

        Scene scene = new Scene(pane, max(200, text.getLayoutBounds().getWidth() + 10), text.getLayoutBounds().getHeight() + 10);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
