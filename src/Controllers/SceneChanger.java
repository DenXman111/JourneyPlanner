package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Basic handler for changing scenes. In future it should work differently and better, it's just the easiest way for now.
 */

class SceneChanger {
    static void changeScene(Stage stage, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(SceneChanger.class.getResource(fxmlFile));
        stage.setScene(new Scene(root));
    }
}
