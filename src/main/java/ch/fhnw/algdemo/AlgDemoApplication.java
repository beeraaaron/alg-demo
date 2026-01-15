package ch.fhnw.algdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AlgDemoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(AlgDemoApplication.class.getResource("control/main.fxml"));
        Scene scene = new Scene(loader.load(), 1400, 700);
        stage.setTitle("Algorithm Demonstrator");
        stage.setScene(scene);
        stage.show();
    }
}
