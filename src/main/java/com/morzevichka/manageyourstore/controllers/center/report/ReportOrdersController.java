package com.morzevichka.manageyourstore.controllers.center.report;

import com.morzevichka.manageyourstore.dto.OrderClientDto;
import com.morzevichka.manageyourstore.services.OrderServiceImpl;
import com.morzevichka.manageyourstore.services.impl.OrderService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportOrdersController implements Initializable {

    @FXML private DatePicker ordersFromDate;
    @FXML private DatePicker ordersToDate;

    @FXML private TableView<OrderClientDto> orderTable;
    @FXML private TableColumn<OrderClientDto, Long> orderIdColumn;
    @FXML private TableColumn<OrderClientDto, Long> orderIdWorker;
    @FXML private TableColumn<OrderClientDto, String> customerColumn;
    @FXML private TableColumn<OrderClientDto, Timestamp> orderDateColumn;
    @FXML private TableColumn<OrderClientDto, Double> orderTotalColumn;


    private final OrderService orderService;

    public ReportOrdersController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        fillData();
    }

    private void initColumns() {
        orderIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        orderIdWorker.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getWorkerId()));

        customerColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));

        orderDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPurchaseDate()));

        orderTotalColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));
    }

    private void fillData() {
        updateProductTable(null, null);
    }

    @FXML
    private void applyDate(ActionEvent event) {
        updateProductTable(ordersFromDate.getValue(), ordersToDate.getValue());
    }


    private void updateProductTable(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            ReportController.informationHandler(Alert.AlertType.ERROR, "Ошибка", "Дата от не может быть позже даты до!");
            return;
        }

        ObservableList<OrderClientDto> observableList = FXCollections.observableList(orderService.ordersWithClientNameBetween(fromDate, toDate));
        orderTable.setItems(observableList);
    }
}