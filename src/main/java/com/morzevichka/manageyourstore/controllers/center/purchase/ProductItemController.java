package com.morzevichka.manageyourstore.controllers.center.purchase;

import com.morzevichka.manageyourstore.entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ProductItemController {
    @FXML private ImageView quantityIcon;
    @FXML private Label productName;
    @FXML private Label productBarcode;
    @FXML private Label productQuantity;
    @FXML private Label productPrice;

    public void setData(Product product) {
        quantityIcon.setImage(new Image(getClass().getResource("/org/example/views/center/purchase/vecteezy_box-carton-delivery-line-style-icon_2590547.jpg").toExternalForm()));
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
