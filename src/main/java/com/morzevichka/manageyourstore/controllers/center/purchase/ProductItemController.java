package com.morzevichka.manageyourstore.controllers.center.purchase;

import com.morzevichka.manageyourstore.entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.Objects;

public class ProductItemController {
    @FXML private ImageView quantityIcon;
    @FXML private Label productName;
    @FXML private Label productBarcode;
    @FXML private Label productQuantity;
    @FXML private Label productPrice;

    public void setData(Product product) {
        InputStream imageStream = getClass().getResourceAsStream("/com/morzevichka/manageyourstore/views/center/purchase/vecteezy_box-carton-delivery-line-style-icon_2590547.jpg");
        Objects.requireNonNull(imageStream);
        Image image = new Image(imageStream);
        quantityIcon.setImage(image);
        productName.setText(product.getName());
        productBarcode.setText(product.getBarcode());
        productQuantity.setText(String.valueOf(product.getQuantity()));
        productPrice.setText(String.valueOf(product.getCost()));
    }

    public void forceBlackText() {
        productName.setTextFill(Color.BLACK);
        productBarcode.setTextFill(Color.BLACK);
        productQuantity.setTextFill(Color.BLACK);
        productPrice.setTextFill(Color.BLACK);
    }
}
