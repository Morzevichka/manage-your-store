package com.morzevichka.manageyourstore.controllers.center.report;

import com.morzevichka.manageyourstore.dto.WorkerSalesDto;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.services.impl.WorkerService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportWorkersController implements Initializable {
    @FXML private DatePicker workerFromDate;
    @FXML private DatePicker workerToDate;

    @FXML private TableView<WorkerSalesDto> workerTable;
    @FXML private TableColumn<WorkerSalesDto, Long> workerIdColumn;
    @FXML private TableColumn<WorkerSalesDto, String> workerNameColumn;
    @FXML private TableColumn<WorkerSalesDto, String> workerLoginColumn;
    @FXML private TableColumn<WorkerSalesDto, String> workerRoleColumn;
    @FXML private TableColumn<WorkerSalesDto, Double> workerTotalSoldColumn;

    private final WorkerService workerService;

    public ReportWorkersController(WorkerServiceImpl workerService) {
        this.workerService = workerService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        fillData();
    }

    private void initColumns() {
        workerIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        workerNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        workerLoginColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUsername()));
        workerRoleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRole()));
        workerTotalSoldColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSumSales()));
    }

    private void fillData() {
        updateProductTable(null, null);
    }

    @FXML
    private void applyDates(ActionEvent event) {
        updateProductTable(workerFromDate.getValue(), workerToDate.getValue());
    }

    private void updateProductTable(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            ReportController.informationHandler(Alert.AlertType.ERROR, "Ошибка", "Дата от не может быть позже даты до!");
            return;
        }

        ObservableList<WorkerSalesDto> observableList = FXCollections.observableList(workerService.workerSalesBetween(fromDate, toDate));
        workerTable.setItems(observableList);
    }
}
