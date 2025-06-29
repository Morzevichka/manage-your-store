package com.morzevichka.manageyourstore.controllers.center.categories;

import com.morzevichka.manageyourstore.entity.Category;
import com.morzevichka.manageyourstore.services.CategoryServiceImpl;
import com.morzevichka.manageyourstore.services.impl.CategoryService;
import com.morzevichka.manageyourstore.utils.LoadStagesUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TableColumn<Category, Long> idColumn;
    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TextField searchTextField;
    @FXML
    private ToggleButton notIgnoreCaseToggleButton;
    @FXML
    private ToggleButton fullWordToggleButton;
    @FXML
    private Button removeButton;

    private final CategoryService categoryService;

    private ObservableList<Category> categories;

    private FilteredList<Category> filteredList;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        fillTable();
        initActions();
    }

    @FXML
    private void updateTable(ActionEvent event) {
        fillTable();
    }

    @FXML
    private void createCategory(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = LoadStagesUtil.loadFXML("views/center/categories/addCategory");
        fxmlLoader.setControllerFactory(clazz -> {
            if (clazz == AddCategoryController.class) {
                return new AddCategoryController((CategoryServiceImpl) categoryService);
            } else {
                try {
                    return clazz.getConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        LoadStagesUtil.loadStage(fxmlLoader, "Создание категории", this::fillTable);
    }

    @FXML
    private void removeCategory(ActionEvent event) {
        ObservableList<Category> selected = categoriesTable.getSelectionModel().getSelectedItems();

        List<Category> toDelete = new ArrayList<>(selected);

        categories.removeAll(selected);

        for (Category elem : toDelete) {
            categoryService.deleteCategory(elem);
        }
    }

    @FXML
    private void clearSelectionKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            searchTextField.setText(null);
        }
    }

    private void initColumns() {
        nameColumn.prefWidthProperty().bind(categoriesTable.widthProperty().multiply(0.85));
        idColumn.prefWidthProperty().bind(categoriesTable.widthProperty().multiply(0.15));

        idColumn.setCellValueFactory(new PropertyValueFactory<Category, Long>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
    }

    private void fillTable() {
        searchTextField.setText(null);
        categories = FXCollections.observableList(categoryService.findAllCategories());
        filteredList = new FilteredList<>(categories);
        categoriesTable.setItems(filteredList);
    }

    private void initActions() {
        // search field actions
        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(category -> searchFilters(category, newVal));
        });

        notIgnoreCaseToggleButton.selectedProperty().addListener(isSelected -> {
            filteredList.setPredicate(category -> searchFilters(category, searchTextField.getText()));
        });

        fullWordToggleButton.selectedProperty().addListener(isSelected -> {
            filteredList.setPredicate(category -> searchFilters(category, searchTextField.getText()));
        });

        // remove or edit rows actions
        removeButton.setDisable(true);
        categoriesTable.setEditable(true);
        nameColumn.setEditable(false);
        categoriesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Category category = event.getRowValue();
            category.setName(event.getNewValue());
            categoryService.updateCategory(category);
        });

        categoriesTable.setRowFactory(tableView -> {
            final TableRow<Category> row = new TableRow<>();

            final ContextMenu rowMenu = new ContextMenu();
            MenuItem editCategory = new MenuItem("Редактировать");
            MenuItem removeCategory = new MenuItem("Удалить");

            editCategory.setOnAction(e -> {
                row.setEditable(true);
                nameColumn.setEditable(true);
                categoriesTable.edit(row.getIndex(), nameColumn);
                nameColumn.setEditable(false);
            });

            removeCategory.setOnAction(e -> {
                Category category = row.getItem();
                categoryService.deleteCategory(category);
                categories.remove(category);
            });

            rowMenu.getItems().addAll(editCategory, removeCategory);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu) null));

            row.setOnMouseClicked(mouseEvent -> {
                if (row.isEmpty()) {
                    categoriesTable.getSelectionModel().clearSelection();
                }
            });
            return row;
        });

        categoriesTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Category>) change -> {
            removeButton.setDisable(categoriesTable.getSelectionModel().getSelectedItems().isEmpty());
        });
    }

    private boolean searchFilters(Category category, String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        String name = category.getName();
        String filter = filterText;

        if (!notIgnoreCaseToggleButton.isSelected()) {
            name = name.toLowerCase();
            filter = filter.toLowerCase();
        }

        if (fullWordToggleButton.isSelected()) {
            return name.equals(filter);
        } else {
            return name.contains(filter);
        }
    }
}