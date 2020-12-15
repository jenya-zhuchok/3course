package snake.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainFrame {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> gameTable;

    @FXML
    private Button newGameButton;

    @FXML
    private Button connectGameButton;

    @FXML
    void initialize() {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();

        newGameButton.setOnAction(event -> {
            newGameButton.getScene().getWindow().hide();
            loader.setLocation(getClass().getResource("/snake/GUI/layouts/settingsFrame.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

        connectGameButton.setOnAction(event -> {

        });
    }
}


