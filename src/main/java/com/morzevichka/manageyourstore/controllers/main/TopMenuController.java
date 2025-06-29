package com.morzevichka.manageyourstore.controllers.main;

import com.morzevichka.manageyourstore.controllers.account.AccountController;
import com.morzevichka.manageyourstore.controllers.auth.LoginController;
import com.morzevichka.manageyourstore.dto.UserSesssion;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.utils.LoadStagesUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TopMenuController {
    @FXML
    private MenuItem closeWindow;
    @FXML
    private MenuItem exitApp;
    @FXML
    private MenuItem logIn;
    @FXML
    private MenuItem account;
    @FXML
    private MenuItem exit;

    private MainController mainController;

    public void setMainSceneController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void logIn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = LoadStagesUtil.loadFXMLLoaderWithController(false, "views/auth/login",
                LoginController.class,
                new WorkerServiceImpl(), mainController);

        Objects.requireNonNull(fxmlLoader);

        LoadStagesUtil.loadStage(fxmlLoader, "Авторизация");
    }

    @FXML
    private void account(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = LoadStagesUtil.loadFXMLLoaderWithController("views/account/account",
                AccountController.class,
                new WorkerServiceImpl());

        Objects.requireNonNull(fxmlLoader);

        LoadStagesUtil.loadStage(fxmlLoader, UserSesssion.getInstance().getWorker().getUsername() + " Аккаунт");
    }

    @FXML
    private void exit(ActionEvent event) {
        UserSesssion.getInstance().clearSession();
        toggleLogInSuccessful(false);
    }

    @FXML
    private void clearMainScreen(ActionEvent actionEvent) {
        mainController.clearMainScreen();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stageMenu = (Stage) closeWindow.getParentPopup().getOwnerWindow();
        stageMenu.setIconified(true);
    }

    @FXML
    private void exitApp(ActionEvent event) {
        Stage stageMenu = (Stage) exitApp.getParentPopup().getOwnerWindow();
        stageMenu.close();
    }

    public void toggleLogInSuccessful(boolean visible) {
        account.setVisible(visible);
        exit.setVisible(visible);
        logIn.setVisible(!visible);
    }
}
