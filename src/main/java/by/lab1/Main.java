package by.lab1;

import by.lab1.initializer.CanvasInitializer;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        CanvasInitializer initializer = new CanvasInitializer();
        initializer.initScene(stage);
    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}