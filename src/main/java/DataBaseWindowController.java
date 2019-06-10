import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;

import java.io.IOException;

public class DataBaseWindowController {
    public TextField hostField;
    public TextField portField;
    public TextField userField;
    public PasswordField passwordField;
    public Label urlLabel;
    public Button createButton;
    public Button startButton;
    public ProgressBar indicator;

    private boolean runningProcess;

    public void updateUrl() {
        if (runningProcess) return;
        String url = "jdbc:postgresql://" + hostField.getText() + ":" + portField.getText() + "/" + userField.getText();
        urlLabel.setText(url);
        DbAdapter.jdbcUrl = url;
        DbAdapter.user = userField.getText();
        DbAdapter.password = passwordField.getText();
    }

    public void createClicked() {
        updateUrl();
        createButton.setDisable(true);
        startButton.setDisable(true);
        runningProcess = true;
        indicator.setVisible(true);
        Task<Integer> createTask = new Task<Integer>() {
            @Override
            protected Integer call() {

                try {
                    DbAdapter.connect();
                    DbAdapter.create();
                    DbAdapter.createTables();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    createButton.setDisable(false);
                    startButton.setDisable(false);
                    runningProcess = false;
                    indicator.setVisible(false);
                });
                return 100;
            }
        };
        indicator.progressProperty().bind(createTask.progressProperty());
        new Thread(createTask).start();
    }

    public void startClicked() throws IOException {
        StageChanger.changeStage(StageChanger.ApplicationStage.WELCOME);
    }
}
