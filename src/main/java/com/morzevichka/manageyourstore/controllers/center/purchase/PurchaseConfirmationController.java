package com.morzevichka.manageyourstore.controllers.center.purchase;

import com.morzevichka.manageyourstore.entity.Client;
import com.morzevichka.manageyourstore.entity.Product;
import com.morzevichka.manageyourstore.services.impl.OrderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PurchaseConfirmationController implements Initializable {
    @FXML private VBox parent;
    @FXML private Label usernameLabel;
    @FXML private Label totalPriceLabel;
    @FXML private Label bonusLabel;
    @FXML private Button applyBonusButton;
    @FXML private ComboBox<String> paymentMethodComboBox;

    private final OrderService orderService;

    private ArrayList<Product> products;

    private Client client;

    private double bonus;

    private double totalPrice;

    private double currentPrice;


    public PurchaseConfirmationController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentMethodComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    currentPrice = newVal.equals("СБП") ? currentPrice * 0.95d : totalPrice;

                    totalPriceLabel.setText(String.format("%.2f ₽", currentPrice));
                }
        );
    }

    public void initData(ArrayList<Product> products, Client client, double totalPrice) {
        this.products = products;
        this.client = client;
        this.totalPrice = totalPrice;
        this.currentPrice = totalPrice;
        initLabels();
    }

    private void initLabels() {
        if (client == null) {
            applyBonusButton.setDisable(true);
            usernameLabel.setText("Пользователь не указан");
        } else {
            usernameLabel.setText(client.getFirstName() + " " + client.getSecondName());
            bonus = client.getBonus().doubleValue();
            bonusLabel.setText(String.format("%.2f", bonus));
        }

        totalPriceLabel.setText(String.format("%.2f ₽", currentPrice));
    }

    @FXML
    private void makePurchase(ActionEvent event) {
        try {
            orderService.createPurchase(products, client, BigDecimal.valueOf(currentPrice));
            informationHandler(Alert.AlertType.INFORMATION, "Успех", "Спасибо за покупку");
        }catch (IllegalArgumentException | IllegalStateException e) {
            informationHandler(Alert.AlertType.ERROR, "Ошибка", e.getMessage());
        } catch (Exception e) {
            informationHandler(Alert.AlertType.ERROR, "Ошибка", "Ошибка при выполенение покупки");
        }
    }

    @FXML
    private void applyBonus(ActionEvent event) {
        currentPrice = currentPrice - bonus;
        bonus = 0.0;
        client.setBonus(BigDecimal.valueOf(bonus));

        totalPriceLabel.setText(String.format("%.2f ₽", currentPrice));
        bonusLabel.setText(String.format("%.2f", bonus));

        applyBonusButton.setDisable(true);
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) parent.getScene().getWindow();
        stage.close();
    }

    private void informationHandler(Alert.AlertType type, String title, String message) {
        Alert errorAlert = new Alert(type);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}