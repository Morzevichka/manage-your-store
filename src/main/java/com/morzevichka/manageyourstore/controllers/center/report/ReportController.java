package com.morzevichka.manageyourstore.controllers.center.report;

import com.morzevichka.manageyourstore.services.OrderItemServiceImpl;
import com.morzevichka.manageyourstore.services.OrderServiceImpl;
import com.morzevichka.manageyourstore.services.ProductServiceImpl;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.services.impl.OrderItemService;
import com.morzevichka.manageyourstore.services.impl.OrderService;
import com.morzevichka.manageyourstore.services.impl.ProductService;
import com.morzevichka.manageyourstore.utils.LoadStagesUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    @FXML private Label totalProductsLabel;
    @FXML private Label soldProductsLabel;
    @FXML private Label totalOrdersLabel;

    @FXML private Tab productsTab;
    @FXML private Tab ordersTab;
    @FXML private Tab workersTab;

    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final OrderService orderService;

    public ReportController(OrderItemServiceImpl orderItemService,
                            ProductServiceImpl productService,
                            OrderServiceImpl orderService) {
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.orderService = orderService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalProductsLabel.setText(String.valueOf(productService.countProducts()));
        soldProductsLabel.setText(String.valueOf(orderItemService.totalProductQuantity()));
        totalOrdersLabel.setText(String.valueOf(orderService.countOrders()));

        try {
            FXMLLoader productFXML = LoadStagesUtil.loadFXML("views/center/report/reportProducts");
            productFXML.setController(new ReportProductsController((ProductServiceImpl) productService));
            productsTab.setContent(productFXML.load());

            FXMLLoader orderFXML = LoadStagesUtil.loadFXML("views/center/report/reportOrders");
            orderFXML.setController(new ReportOrdersController((OrderServiceImpl) orderService));
            ordersTab.setContent(orderFXML.load());

            FXMLLoader workerFXML = LoadStagesUtil.loadFXML("views/center/report/reportWorkers");
            workerFXML.setController(new ReportWorkersController(new WorkerServiceImpl()));
            workersTab.setContent(workerFXML.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void informationHandler(Alert.AlertType type, String title, String message) {
        Alert errorAlert = new Alert(type);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
