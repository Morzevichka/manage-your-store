package com.morzevichka.manageyourstore;

import com.morzevichka.manageyourstore.utils.HibernateUtil;
import com.morzevichka.manageyourstore.utils.LoadStagesUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class App extends Application {

    private final boolean SET_MAXIMIZED_WINDOW = true;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenRes.getWidth();
        int height = (int) screenRes.getHeight();

        HibernateUtil.getSessionFactory();
        HibernateUtil.setUpTestUserForH2DB();

        Scene scene = new Scene(LoadStagesUtil.loadFXML("views/main/main").load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Your store");
        primaryStage.setMaximized(SET_MAXIMIZED_WINDOW);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}