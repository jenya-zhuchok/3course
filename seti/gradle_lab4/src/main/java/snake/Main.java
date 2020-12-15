package snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();

        loader.setLocation(getClass().getResource("/layouts/mainFrame.fxml"));
            loader.load();


        Parent root = loader.getRoot();
        primaryStage.setTitle("Snake");
        primaryStage.setScene(new Scene(root, 600, 410));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
