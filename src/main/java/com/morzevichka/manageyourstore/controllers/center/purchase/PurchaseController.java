package com.morzevichka.manageyourstore.controllers.center.purchase;

import com.morzevichka.manageyourstore.App;
import com.morzevichka.manageyourstore.entity.Client;
import com.morzevichka.manageyourstore.entity.Product;
import com.morzevichka.manageyourstore.entity.Status;
import com.morzevichka.manageyourstore.exceptions.UserBannedException;
import com.morzevichka.manageyourstore.services.impl.ClientService;
import com.morzevichka.manageyourstore.services.impl.OrderService;
import com.morzevichka.manageyourstore.services.impl.ProductService;
import com.morzevichka.manageyourstore.utils.LoadStagesUtil;
import com.morzevichka.manageyourstore.utils.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    @FXML private TextField searchTextField;
    @FXML private ListView<Product> productsListView;
    @FXML private ListView<Product> purchaseListView;
    @FXML private Label totalPriceTextField;
    @FXML private TextField clientNumberTextField;

    private final OrderService orderService;

    private final ProductService productService;

    private final ClientService clientService;

    private FilteredList<Product> filteredProducts;

    private final ObservableList<Product> selectedItems = FXCollections.observableArrayList();

    private Client client;

    public PurchaseController(ProductService productService, OrderService orderService, ClientService clientService) {
        this.productService = productService;
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initProductList();
        initSearch();
        initPurchaseList();
        initClientSearch();
    }

    private void initProductList() {
        ObservableList<Product> products = FXCollections.observableList(productService.findAllProducts());
        filteredProducts = new FilteredList<>(products);

        productsListView.setItems(filteredProducts);
        productsListView.setCellFactory(param -> new ListCell<Product>() {
            private FXMLLoader fxmlLoader;
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (fxmlLoader == null) {
                        try {
                            fxmlLoader = LoadStagesUtil.loadFXML("views/center/purchase/productItemView");
                            fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    ProductItemController productItemController = fxmlLoader.getController();
                    productItemController.setData(item);
                    productItemController.forceBlackText();

                    Node root = fxmlLoader.getRoot();

                    final ContextMenu contextMenu = new ContextMenu();
                    MenuItem addProduct = new MenuItem("Добавить продукт");

                    addProduct.setOnAction(evt -> selectedItems.add(item));

                    contextMenu.getItems().add(addProduct);

                    root.setOnContextMenuRequested(event -> contextMenu.show(root, event.getScreenX(), event.getScreenY()));

                    root.setOnMouseClicked(mouseEvent -> {
                        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                            selectedItems.add(item);
                        }
                    });

                    setText(null);
                    setStyle("-fx-background-color: white;" +
                            "-fx-text-fill: black;" +
                            "-fx-border-color: #cccccc;" +
                            "-fx-border-width: 0 0 1 0;" +
                            "-fx-padding: 10;");
                    setGraphic(root);
                }
            }
        });
    }

    private void initClientSearch() {
        clientNumberTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            int maxLen = 12;
            if (newVal.startsWith("8")) {
                maxLen = 11;
            }
            if (newVal.length() > maxLen) {
                String text = newVal.substring(0, maxLen);
                clientNumberTextField.setText(text);
            }
        });
    }

    private void initPurchaseList() {
        purchaseListView.setItems(selectedItems);

        purchaseListView.getItems().addListener((ListChangeListener<Product>) change -> {
            double total = purchaseListView.getItems()
                    .stream()
                    .mapToDouble(item -> item.getCost().doubleValue())
                    .sum();
            totalPriceTextField.setText(String.format("%.2f", total));
        });

        purchaseListView.setCellFactory(param -> new ListCell<Product>() {
            private FXMLLoader fxmlLoader;

            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (fxmlLoader == null) {
                        try {
                            fxmlLoader = new FXMLLoader(App.class.getResource("views/center/purchase/productItemView.fxml"));
                            fxmlLoader.load();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ProductItemController productItemController = fxmlLoader.getController();
                    productItemController.setData(item);
                    productItemController.forceBlackText();

                    Node root = fxmlLoader.getRoot();

                    final ContextMenu contextMenu = new ContextMenu();
                    MenuItem deleteProduct = new MenuItem("Удалить продукт");

                    deleteProduct.setOnAction(event -> {
                        selectedItems.remove(item);
                    });

                    contextMenu.getItems().add(deleteProduct);

                    root.setOnContextMenuRequested(event -> contextMenu.show(root, event.getScreenX(), event.getScreenY()));

                    setText(null);
                    setStyle("-fx-background-color: white;" +
                            "-fx-text-fill: black;" +
                            "-fx-border-color: #cccccc;" +
                            "-fx-border-width: 0 0 1 0;" +
                            "-fx-padding: 10;");
                    setGraphic(root);
                }
            }
        });
    }

    private void initSearch() {
        searchTextField.textProperty().addListener(change -> {
            filteredProducts.setPredicate(product -> {
                String filterText = searchTextField.getText();

                if (filterText == null || filterText.isEmpty()) {
                    return true;
                }

                String name = product.getName().toLowerCase();
                String quantity = String.valueOf(product.getQuantity());
                String price = String.valueOf(product.getCost());
                String barcode = product.getBarcode();

                return name.contains(filterText) || quantity.contains(filterText) || price.contains(filterText) || barcode.contains(filterText);

            });
        });
    }

    @FXML
    private void clearSearch(ActionEvent event) {
        searchTextField.setText(null);
    }

    @FXML
    private void clearPurchaseList(ActionEvent event) {
        purchaseListView.getItems().removeAll(selectedItems);
    }

    @FXML
    private void findClient(ActionEvent event) {
        String number = clientNumberTextField.getText();

        number = number.startsWith("8") ? "+7" + number.substring(1) : number;

        try {
            ValidationUtil.isValidPhone(number);
            client = clientService.findClientByPhone(number);
            if (client.getStatus().equals(Status.BANNED)) {
                throw new UserBannedException("Пользователь заблокирован");
            }
            informationHandler(Alert.AlertType.INFORMATION, "Информация", "Клиент найден");
        } catch (IllegalArgumentException e) {
            informationHandler(Alert.AlertType.ERROR, "Ошибка поиска клиента", e.getMessage());
        } catch (UserBannedException e) {
            informationHandler(Alert.AlertType.ERROR, "Ошибка", e.getMessage());
        }
    }

    @FXML
    private void purchase(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = LoadStagesUtil.loadFXMLLoaderWithController(
                false,
                "views/center/purchase/purchaseConfirmationView",
                PurchaseConfirmationController.class,
                orderService);

        Objects.requireNonNull(fxmlLoader);

        Parent parent = fxmlLoader.load();

        PurchaseConfirmationController purchaseConfirmationController = fxmlLoader.getController();
        purchaseConfirmationController.initData(new ArrayList<>(selectedItems), client, Double.parseDouble(totalPriceTextField.getText()));

        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Подтверждение покупки");

        stage.setOnHidden(event1 -> selectedItems.removeAll(new ArrayList<>(selectedItems)));

        stage.show();
    }

    private void informationHandler(Alert.AlertType type, String title, String message) {
        Alert errorAlert = new Alert(type);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}