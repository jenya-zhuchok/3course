package snake.GUI.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsFrame {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private TextField foodStatic;

    @FXML
    private TextField foodPerPlayer;

    @FXML
    private TextField stateDelayMs;

    @FXML
    private TextField deadFoodProb;

    @FXML
    private TextField connectionPort;

    @FXML
    private TextField pingDelayMs;

    @FXML
    private TextField nodeTimeoutMs;

    @FXML
    private Button createNewGame;

    @FXML
    private Button backToMain;

    @FXML
    void initialize() {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();

        backToMain.setOnAction(event -> {
            backToMain.getScene().getWindow().hide();
            loader.setLocation(getClass().getResource("/layouts/mainFrame.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });


    }
}
