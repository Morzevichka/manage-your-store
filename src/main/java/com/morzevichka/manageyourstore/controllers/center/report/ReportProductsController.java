package com.morzevichka.manageyourstore.controllers.center.report;

import com.morzevichka.manageyourstore.dto.ProductSalesDto;
import com.morzevichka.manageyourstore.services.ProductServiceImpl;
import com.morzevichka.manageyourstore.services.impl.ProductService;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportProductsController implements Initializable {
    @FXML private DatePicker soldFromDate;
    @FXML private DatePicker soldToDate;

    @FXML private TableView<ProductSalesDto> productTable;
    @FXML private TableColumn<ProductSalesDto, String> productNameColumn;
    @FXML private TableColumn<ProductSalesDto, Double> quantityColumn;
    @FXML private TableColumn<ProductSalesDto, Double> totalPriceColumn;

    private final ProductService productService;

    public ReportProductsController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        fillData();
    }

    private void initColumns() {
        productNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        totalPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));
    }

    private void fillData() {
        updateProductTable(null, null);
    }

    @FXML
    private void applyDates(ActionEvent event) {
        updateProductTable(soldFromDate.getValue(), soldToDate.getValue());
    }

    private void updateProductTable(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            ReportController.informationHandler(Alert.AlertType.ERROR, "Ошибка", "Дата от не может быть позже даты до!");
            return;
        }

        ObservableList<ProductSalesDto> observableList = FXCollections.observableList(productService.productSalesBetween(fromDate, toDate));
        productTable.setItems(observableList);
    }
}
