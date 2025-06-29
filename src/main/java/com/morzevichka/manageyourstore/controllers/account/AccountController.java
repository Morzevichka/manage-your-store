package com.morzevichka.manageyourstore.controllers.account;

import com.morzevichka.manageyourstore.dto.UserSesssion;
import com.morzevichka.manageyourstore.entity.Worker;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.services.impl.WorkerService;
import com.morzevichka.manageyourstore.utils.PictureUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;

import javafx.scene.layout.HBox;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML private HBox hBoxScene;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label lastLoginLabel;
    @FXML private Label registerDateLabel;
    @FXML private GridPane changePasswordGridPane;
    @FXML private PasswordField currentPasswordPasswordField;
    @FXML private PasswordField newPasswordPasswordField;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label changePasswordLabel;
    @FXML private Button changePasswordButton;
    @FXML private Button cancelChangePasswordButton;
    @FXML private Button uploadImageButton;
    @FXML private ImageView profileImageView;
    @FXML private Label imageLoadLabel;

    private final WorkerService workerService;

    public AccountController(WorkerServiceImpl workerService) {
        this.workerService = workerService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Worker user = UserSesssion.getInstance().getWorker();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole().name());
        lastLoginLabel.setText(user.getLastLogin().toString());
        registerDateLabel.setText(user.getRegisterDate().toString());

        if (user.getProfilePicture() == null) {
            uploadImageButton.setVisible(true);
            profileImageView.setVisible(false);
        } else {
            try {
                profileImageView.setImage(PictureUtil.loadImage(user.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @FXML
    private void showChangePasswordFieldsButtonOnAction() {
        togglePasswordsFields(true);
    }

    @FXML
    private void closeChangePasswordFieldsButtonOnAction() {
        togglePasswordsFields(false);
    }

    @FXML
    private void changePasswordButtonOnAction(ActionEvent event) {
        if (currentPasswordPasswordField.getText().isEmpty()) {
            throw new IllegalStateException("Введите пароль для продолжения");
        }
        if (newPasswordPasswordField.getText().equals(confirmPasswordPasswordField.getText())) {
            throw new IllegalStateException("Пароли не совпадают");
        }
        try {
            workerService.changePassword(
                    UserSesssion.getInstance().getWorker().getUsername(),
                    currentPasswordPasswordField.getText(),
                    newPasswordPasswordField.getText()
            );
            UserSesssion.getInstance().updateUser();
            togglePasswordsFields(false);
        } catch (IllegalArgumentException | IllegalStateException e) {
            changePasswordLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void uploadImageButtonOnAction(ActionEvent event) {
        Window stage = hBoxScene.getScene().getWindow();
        try {
            Image image = PictureUtil.loadImageFromFileChooser(stage, UserSesssion.getInstance().getWorker().getId());
            profileImageView.setImage(image);
            uploadImageButton.setVisible(false);
            imageLoadLabel.setVisible(false);
        } catch (Exception e) {
            imageLoadLabel.setText(e.getMessage());
        }
    }

    private void togglePasswordsFields(boolean visible) {
        changePasswordGridPane.setVisible(visible);
        changePasswordButton.setVisible(visible);
        changePasswordLabel.setVisible(visible);
        cancelChangePasswordButton.setVisible(visible);
    }
}

