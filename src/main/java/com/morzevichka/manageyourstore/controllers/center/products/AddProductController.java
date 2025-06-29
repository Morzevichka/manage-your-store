package com.morzevichka.manageyourstore.controllers.center.products;

import com.morzevichka.manageyourstore.entity.Category;
import com.morzevichka.manageyourstore.entity.Product;
import com.morzevichka.manageyourstore.services.CategoryServiceImpl;
import com.morzevichka.manageyourstore.services.ProductServiceImpl;
import com.morzevichka.manageyourstore.services.impl.CategoryService;
import com.morzevichka.manageyourstore.services.impl.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox<Category> categories;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private Label message;

    private final CategoryService categoryService;

    private final ProductService productService;

    public AddProductController(ProductServiceImpl productService, CategoryServiceImpl categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Category> categoriesList = FXCollections.observableList(
                categoryService.findAllCategories());

        System.out.println(Arrays.toString(categoriesList.toArray()));

        categories.getItems().addAll(categoriesList);

        categories.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category == null ? "Без категории" : category.getName();
            }

            @Override
            public Category fromString(String s) {
                return categories.getItems().stream()
                        .filter(x -> x.getName().equalsIgnoreCase(s))
                        .findFirst()
                        .orElse(null);
            }
        });

        categories.setValue(categoriesList.isEmpty() ? null : categoriesList.getFirst());

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
    }

    @FXML
    private void add(ActionEvent event) {
        Product product = productService.findProductByName(nameTextField.getText());
        if (product != null) {
            message.setText("Данный продукт уже существует");
            return;
        }
        product = new Product();
        product.setName(nameTextField.getText());
        product.setBarcode(Product.generateBarcode());
        product.setCategory(categories.getValue());
        product.setCost(new BigDecimal(priceTextField.getText()));
        product.setQuantity(1);
        product.setLastEditDate(Timestamp.valueOf(LocalDateTime.now()));

        try {
            productService.saveProduct(product);
        } catch (Exception e) {
            message.setText(e.getMessage());
        }

        close(null);
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) (anchorPane.getScene().getWindow());
        stage.close();
    }
}
