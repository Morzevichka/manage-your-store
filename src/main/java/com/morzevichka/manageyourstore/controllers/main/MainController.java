package com.morzevichka.manageyourstore.controllers.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private BorderPane mainScene;

    @FXML
    public TopMenuController topMenuController;
    @FXML
    public LeftMenuController leftMenuController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topMenuController.setMainSceneController(this);
        leftMenuController.setMainSceneController(this);
    }

    public void setMainScreen(Parent view) {
        mainScene.setCenter(view);
    }

    public void clearMainScreen() {
        mainScene.setCenter(null);
    }
}
