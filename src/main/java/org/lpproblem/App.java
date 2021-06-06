package org.lpproblem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final String BOOTSTRAP_PREFIX = "http://getbootstrap.com/components/#";

    private static Stage stage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        FXMLLoader root = new FXMLLoader(new File("src/main/java/org/lpproblem/View/MainScreen.fxml").toURI().toURL());
        stage.setTitle("Least Cost Method");
        stage.setScene(new Scene(root.load(), 1000, 600));
        stage.setMaxWidth(1000);
        stage.setMaxHeight(600);
        stage.initStyle(StageStyle.DECORATED);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setPrimaryStage(Stage stage) {
        App.stage = stage;
    }

    public static Stage getMainStage() {
        return App.stage;
    }

    private void setPrimaryScene(Scene scene) {
        App.scene = scene;
    }

    public static Scene getMainScene() {
        return App.scene;
    }


}