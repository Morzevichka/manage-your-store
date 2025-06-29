package com.morzevichka.manageyourstore.controllers.center.products;

import com.morzevichka.manageyourstore.entity.Category;
import com.morzevichka.manageyourstore.entity.Product;
import com.morzevichka.manageyourstore.services.CategoryServiceImpl;
import com.morzevichka.manageyourstore.services.ProductServiceImpl;
import com.morzevichka.manageyourstore.services.impl.CategoryService;
import com.morzevichka.manageyourstore.services.impl.ProductService;
import com.morzevichka.manageyourstore.utils.LoadStagesUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class ProductController implements Initializable {
    @FXML private AnchorPane anchorPane;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Long> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> barcodeColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Category> categoryColumn;
    @FXML private TableColumn<Product, BigDecimal> priceColumn;
    @FXML private TableColumn<Product, Timestamp> lastEditColumn;

    @FXML private TextField searchTextField;
    @FXML private ToggleButton notIgnoreCaseToggleButton;
    @FXML private ToggleButton fullWordToggleButton;
    @FXML private Button removeButton;

    private final ProductService productService;

    private final CategoryService categoryService;

    private ObservableList<Product> products;

    private ObservableList<Category> categories;

    private FilteredList<Product> filteredList;

    private TableColumn<Product, ?> columnClicked;

    public ProductController(ProductServiceImpl productService, CategoryServiceImpl categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initNodes();
        initCategories();
        initColumns();
        fillTable();
        initSearch();
        initRows();
    }

    @FXML
    private void updateTable() {
        fillTable();
    }

    @FXML
    private void createProduct(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = LoadStagesUtil.loadFXMLLoaderWithController("views/center/products/addProduct",
                AddProductController.class,
                new ProductServiceImpl(),
                new CategoryServiceImpl());

        Objects.requireNonNull(fxmlLoader);

        LoadStagesUtil.loadStage(fxmlLoader, "Создание продукта", this::fillTable);
    }

    @FXML
    private void removeProduct(ActionEvent event) {
        ObservableList<Product> selected = productsTable.getSelectionModel().getSelectedItems();

        products.removeAll(selected);

        for (Product elem : selected) {
            productService.deleteProduct(elem);
        }
    }

    @FXML
    private void clearSelectionKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            searchTextField.setText(null);
        }
    }

    private void initNodes() {
        removeButton.setDisable(true);
        productsTable.setEditable(true);
        productsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        productsTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Product>) change -> {
            removeButton.setDisable(productsTable.getSelectionModel().getSelectedItems().isEmpty());
        });
    }

    private void initCategories() {
        categories = FXCollections.observableList(categoryService.findAllCategories());
        categories.add(null);
    }

    private void initColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArticle()));
        idColumn.setEditable(false);

        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setName(event.getNewValue());
            productService.updateProduct(product);
        });
        nameColumn.setEditable(true);

        barcodeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBarcode()));
        barcodeColumn.setEditable(false);

        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setQuantity(event.getNewValue());
            productService.updateProduct(product);
        });
        quantityColumn.setEditable(true);

        categoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory()));
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "Без категории";
            }

            @Override
            public Category fromString(String s) {
                return categories.stream()
                        .filter(x -> x.getName().equals(s))
                        .findFirst()
                        .orElse(null);
            }
        }, categories));
        categoryColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            Category category = event.getNewValue();
            product.setCategory(category);
            productService.updateProduct(product);
        });
        categoryColumn.setEditable(true);

        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCost()));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setCost(event.getNewValue());
            productService.updateProduct(product);
        });
        priceColumn.setEditable(true);

        lastEditColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastEditDate()));
        lastEditColumn.setEditable(false);
    }

    private void fillTable() {
        searchTextField.setText(null);
        products = FXCollections.observableList(productService.findAllProductWithCategory());
        filteredList = new FilteredList<>(products);
        productsTable.setItems(filteredList);
    }

    private void initSearch() {
        searchTextField.textProperty().addListener(change -> {
            filteredList.setPredicate(this::applyFilter);
        });

        notIgnoreCaseToggleButton.selectedProperty().addListener(isSelected -> {
            filteredList.setPredicate(this::applyFilter);
        });

        fullWordToggleButton.selectedProperty().addListener(isSelected -> {
            filteredList.setPredicate(this::applyFilter);
        });
    }

    public boolean applyFilter(Product product) {
        String filterText = searchTextField.getText();

        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        String name = product.getName();
        String quantity = String.valueOf(product.getQuantity());
        String categoryName = product.getCategory().getName();
        String price = String.valueOf(product.getCost());
        String barcode = product.getBarcode();

        if (!notIgnoreCaseToggleButton.isSelected()) {
            name = name.toLowerCase();
            categoryName = categoryName.toLowerCase();
        }

        if (fullWordToggleButton.isSelected()) {
            return name.equals(filterText) || quantity.equals(filterText) || categoryName.equals(filterText) || price.equals(filterText) || barcode.equals(filterText);
        } else {
            return name.contains(filterText) || quantity.contains(filterText) || categoryName.contains(filterText) || price.contains(filterText) || barcode.contains(filterText);
        }
    }

    private void initRows() {
        productsTable.setRowFactory(table -> {
            final TableRow<Product> row = new TableRow<>();

            final ContextMenu contextMenu = new ContextMenu();
            MenuItem editProduct = new MenuItem("Редактировать");
            MenuItem removeProduct = new MenuItem("Удалить");

            row.setOnMouseClicked(mouseEvent -> {
                if (!row.isEmpty() && mouseEvent.getButton() == MouseButton.SECONDARY) {
                    Node node = mouseEvent.getPickResult().getIntersectedNode();

                    while (node != null && (! (node instanceof TableCell))) {
                        node = node.getParent();
                    }

                    if (node instanceof TableCell<?, ?> cell) {
                        columnClicked = (TableColumn<Product, ?>) cell.getTableColumn();
                        editProduct.setText("Редактировать " + cell.getTableColumn().getText().toLowerCase());
                        if (!columnClicked.isEditable()) {
                            editProduct.setDisable(true);
                        }
                    }
                } else if (row.isEmpty()) {
                    productsTable.getSelectionModel().clearSelection();
                }
            });

            editProduct.setOnAction(event -> {
                TableColumn<Product, ?> col = columnClicked;
                if (col.isEditable()) {
                    productsTable.edit(row.getIndex(), columnClicked);
                }
            });

            removeProduct.setOnAction(e -> {
                Product product = row.getItem();
                productService.deleteProduct(product);
                products.remove(product);
            });

            contextMenu.getItems().addAll(editProduct, removeProduct);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu) null)
            );

            return row;
        });
    }
}