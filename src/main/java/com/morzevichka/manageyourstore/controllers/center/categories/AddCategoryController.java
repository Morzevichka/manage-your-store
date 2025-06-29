package com.morzevichka.manageyourstore.controllers.center.categories;

import com.morzevichka.manageyourstore.entity.Category;
import com.morzevichka.manageyourstore.services.CategoryServiceImpl;
import com.morzevichka.manageyourstore.services.impl.CategoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoryController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button addButton;

    private final CategoryService categoryService;

    public AddCategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        anchorPane.sceneProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                newVal.setOnKeyPressed(key -> {
                    switch (key.getCode()) {
                        case ENTER -> add(null);
                        case ESCAPE -> close(null);
                    }
                });
            }
        });

        addButton.setDisable(true);

        nameTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                messageLabel.setText("Категория не может быть пустой");
                addButton.setDisable(true);
            } else if (newVal.matches("^[0-9]+.*")) {
                messageLabel.setText("Категория не может начинаться с цифр");
                addButton.setDisable(true);
            } else if (newVal.length() > 20) {
                messageLabel.setText("Категория не может быть большее 20 символов");
                addButton.setDisable(true);
            } else if (newVal.startsWith(" ")) {
                messageLabel.setText("Не может начитаться с пробела");
                addButton.setDisable(true);
            } else {
                messageLabel.setText(null);
                addButton.setDisable(false);
            }
        });
    }

    @FXML
    private void add(ActionEvent event) {
        Category category = categoryService.findCategoryByName(nameTextField.getText());
        if (category != null) {
            messageLabel.setText("Данная категория уже существует");
            return;
        }
        category = new Category();
        category.setName(nameTextField.getText());
        categoryService.saveCategory(category);
        close(null);
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) (anchorPane.getScene().getWindow());
        stage.close();
    }
}
