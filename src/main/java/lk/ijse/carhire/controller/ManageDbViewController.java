package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lk.ijse.carhire.dto.CarBrandDto;
import lk.ijse.carhire.dto.CarCategoryDto;
import lk.ijse.carhire.dto.CarModelDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CarBrandService;
import lk.ijse.carhire.service.custom.CarCategoryService;
import lk.ijse.carhire.service.custom.CarModelService;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageDbViewController {
    public AnchorPane formNode;
    public TableView categoryTable;
    public TableView brandTable;
    public TableView modelTable;
    public TextField categoryField;
    public TextField brandField;
    public TextField modelField;
    public Button categoryUpdateBtn;
    public Button categoryDeleteBtn;
    public Button brandUpdateBtn;
    public Button brandDeleteBtn;
    public Button modelUpdateBtn;
    public Button modelDeleteBtn;
    public Button modelCreateBtn;
    private CarCategoryService carCategoryService;
    private CarBrandService carBrandService;
    private CarModelService carModelService;
    private TableDataModel categoryTableDataModel;
    private TableDataModel brandTableDataModel;
    private TableDataModel modelTableDataModel;

    public ManageDbViewController() {
        carCategoryService = (CarCategoryService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_CATEGORY);
        carBrandService = (CarBrandService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_BRAND);
        carModelService = (CarModelService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_MODEL);

        categoryTable = new TableView();
        brandTable = new TableView();
        modelTable = new TableView();
    }

    @FXML
    public void initialize() {
        loadCategoryTable();
        loadBrandTable();
        loadModelTable();

        categoryTable.getSelectionModel().select(0);
        brandTable.getSelectionModel().select(0);

        categoryUpdateBtn.setDisable(true);
        categoryDeleteBtn.setDisable(true);
        brandUpdateBtn.setDisable(true);
        brandDeleteBtn.setDisable(true);
        modelCreateBtn.setDisable(true);
        modelUpdateBtn.setDisable(true);
        modelDeleteBtn.setDisable(true);
    }
    public void categoryCreateBtnOnAction(ActionEvent actionEvent) {
        try {
            CarCategoryDto carCategoryDto = new CarCategoryDto();
            carCategoryDto.setCategory(categoryField.getText());
            carCategoryDto.setCreatedBy(LoginFormController.passUser());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create new Category?");
            alert.setHeaderText("Confirm to create new entry");
            alert.setContentText("Category: " + categoryField.getText());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carCategoryService.saveCategory(carCategoryDto);
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void categoryUpdateBtnBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel tableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();
            CarCategoryDto previousDto = carCategoryService.getCategory(tableDataModel.getId());

            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Update Category Entry");
            textInputDialog.setHeaderText("Enter the new category");
            textInputDialog.setContentText("Current category: " + tableDataModel.getId());
            Optional<String> result = textInputDialog.showAndWait();
            String updatedCategory;
            if(result.isPresent()) {
                updatedCategory = textInputDialog.getEditor().getText();

                CarCategoryDto carCategoryDto = new CarCategoryDto();
                carCategoryDto.setCategory(updatedCategory);
                carCategoryDto.setCreatedBy(previousDto.getCreatedBy());
                carCategoryDto.setUpdatedBy(LoginFormController.passUser());

                String resp = carCategoryService.updateCategory(carCategoryDto, previousDto.getCategory());
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void categoryDeleteBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel tableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Category?");
            alert.setHeaderText("Confirm to delete entry");
            alert.setContentText("Category: " + tableDataModel.getId());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carCategoryService.deleteCategory(tableDataModel.getId());
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void brandCreateBtnOnAction(ActionEvent actionEvent) {
        try {
            CarBrandDto carBrandDto = new CarBrandDto();
            carBrandDto.setBrand(brandField.getText());
            carBrandDto.setCreatedBy(LoginFormController.passUser());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create new Brand?");
            alert.setHeaderText("Confirm to create new entry");
            alert.setContentText("Brand: " + brandField.getText());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carBrandService.saveBrand(carBrandDto);
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void brandUpdateBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel tableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();
            CarBrandDto previousDto = carBrandService.getBrand(tableDataModel.getId());

            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Update Brand Entry");
            textInputDialog.setHeaderText("Enter the new brand");
            textInputDialog.setContentText("Current brandy: " + tableDataModel.getId());
            Optional<String> result = textInputDialog.showAndWait();
            String updatedBrand;
            if(result.isPresent()) {
                updatedBrand = textInputDialog.getEditor().getText();

                CarBrandDto carBrandDto = new CarBrandDto();
                carBrandDto.setBrand(updatedBrand);
                carBrandDto.setCreatedBy(previousDto.getCreatedBy());
                carBrandDto.setUpdatedBy(LoginFormController.passUser());

                String resp = carBrandService.updateBrand(carBrandDto, previousDto.getBrand());
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void brandDeleteBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel tableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Brand?");
            alert.setHeaderText("Confirm to delete entry");
            alert.setContentText("Brand: " + tableDataModel.getId());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carBrandService.deleteBrand(tableDataModel.getId());
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void modelCreateBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel categoryTableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();
            TableDataModel brandTableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();
            CarModelDto carModelDto = new CarModelDto();
            carModelDto.setModel(modelField.getText());
            carModelDto.setBrand(brandTableDataModel.getId());
            carModelDto.setCategory(categoryTableDataModel.getId());
            carModelDto.setCreatedBy(LoginFormController.passUser());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create new Model?");
            alert.setHeaderText("Confirm to create new entry");
            alert.setContentText("Category: " + categoryTableDataModel.getId() + "\nBrand: " + brandTableDataModel.getId() + "\nModel: " + modelField.getText());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carModelService.saveModel(carModelDto);
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void modelUpdateBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel categoryTableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();
            TableDataModel brandTableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();
            TableDataModel modelTableDataModel = (TableDataModel) modelTable.getSelectionModel().getSelectedItem();
            CarModelDto previousDto = carModelService.getModel(modelTableDataModel.getId());

            Dialog dialog = new Dialog<>();
            dialog.setTitle("Update Model Entry");
            dialog.setHeaderText("Enter the new model details");

            Label categoryDetail = new Label("Previous Category: " + categoryTableDataModel.getId());
            ComboBox categoryComboBox = new ComboBox();
            categoryComboBox.setPromptText("Choose new category");
            categoryComboBox.getItems().clear();
            List<CarCategoryDto> carCategoryDtos = carCategoryService.getAllCategories();
            ArrayList<String> categories = new ArrayList<>();
            for(CarCategoryDto carCategoryDto : carCategoryDtos) {
                categories.add(carCategoryDto.getCategory());
            }
            categoryComboBox.getItems().addAll(categories);

            Label brandDetail = new Label("Previous Brand: " + brandTableDataModel.getId());
            ComboBox brandComboBox = new ComboBox();
            brandComboBox.setPromptText("Choose new brand");
            brandComboBox.getItems().clear();
            List<CarBrandDto> carBrandDtos = carBrandService.getAllBrands();
            ArrayList<String> brands = new ArrayList<>();
            for(CarBrandDto carBrandDto : carBrandDtos) {
                brands.add(carBrandDto.getBrand());
            }
            brandComboBox.getItems().addAll(brands);

            Label modelDetail = new Label("Previous Model: " + modelTableDataModel.getId());
            TextField modelField = new TextField();
            modelField.setPromptText("Enter new model");

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));
            gridPane.add(categoryDetail, 0, 0);
            gridPane.add(categoryComboBox, 1, 0);
            gridPane.add(brandDetail, 0, 1);
            gridPane.add(brandComboBox, 1, 1);
            gridPane.add(modelDetail, 0, 2);
            gridPane.add(modelField, 1, 2);
            dialog.getDialogPane().setContent(gridPane);

            Optional<ButtonType> result = dialog.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                CarModelDto carModelDto = new CarModelDto();
                carModelDto.setCategory(String.valueOf(categoryComboBox.getSelectionModel().getSelectedItem()));
                carModelDto.setBrand(String.valueOf(brandComboBox.getSelectionModel().getSelectedItem()));
                carModelDto.setModel(modelField.getText());
                carModelDto.setCreatedBy(previousDto.getCreatedBy());
                carModelDto.setUpdatedBy(LoginFormController.passUser());

                String resp = carModelService.updateModel(carModelDto, previousDto.getModel());
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void modelDeleteBtnOnAction(ActionEvent actionEvent) {
        try {
            TableDataModel tableDataModel = (TableDataModel) modelTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Model?");
            alert.setHeaderText("Confirm to delete entry");
            alert.setContentText("Model: " + tableDataModel.getId());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carModelService.deleteModel(tableDataModel.getId());
                clear();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void loadCategoryTable() {
        categoryTable.getColumns().clear();

        TableColumn category = new TableColumn("Categories");
        category.prefWidthProperty().bind(categoryTable.widthProperty().subtract(2));
        category.setCellValueFactory(new PropertyValueFactory<>("id"));

        categoryTable.getColumns().addAll(category);

        categoryTable.getItems().add(new TableDataModel("-All-"));
        List<CarCategoryDto> carCategoryDtos = carCategoryService.getAllCategories();
        for (CarCategoryDto carCategoryDto : carCategoryDtos) {
            categoryTable.getItems().add(new TableDataModel(carCategoryDto.getCategory()));
        }
    }

    public void loadBrandTable() {
        brandTable.getColumns().clear();

        TableColumn brand = new TableColumn("Brands");
        brand.prefWidthProperty().bind(brandTable.widthProperty().subtract(2));
        brand.setCellValueFactory(new PropertyValueFactory<>("id"));

        brandTable.getColumns().add(brand);

        brandTable.getItems().add(new TableDataModel("-All-"));
        List<CarBrandDto> carBrandDtos = carBrandService.getAllBrands();
        for (CarBrandDto carBrandDto : carBrandDtos) {
            brandTable.getItems().add(new TableDataModel(carBrandDto.getBrand()));
        }
    }

    public void loadModelTable() {
        modelTable.getColumns().clear();

        TableColumn model = new TableColumn("Models");
        model.prefWidthProperty().bind(brandTable.widthProperty().subtract(2));
        model.setCellValueFactory(new PropertyValueFactory<>("id"));

        modelTable.getColumns().add(model);

        List<CarModelDto> carModelDtos = carModelService.getAllModels();
        for (CarModelDto carModelDto : carModelDtos) {
            modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
        }
    }

    public void clear() throws IOException {
        AnchorPane formNode = FXMLLoader.load(this.getClass().getResource("/view/manage_db_view.fxml"));
        AnchorPane.setTopAnchor(formNode, 0.0);
        AnchorPane.setBottomAnchor(formNode, 0.0);
        AnchorPane.setLeftAnchor(formNode, 0.0);
        AnchorPane.setRightAnchor(formNode, 0.0);
        this.formNode.getChildren().clear();
        this.formNode.getChildren().add(formNode);
    }

    public void categoryTableOnMouseClicked(MouseEvent mouseEvent) {
        categoryTableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();
        brandTableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();

        if(categoryTableDataModel.getId() != "-All-") {
            categoryUpdateBtn.setDisable(false);
            categoryDeleteBtn.setDisable(false);
        } else {
            categoryUpdateBtn.setDisable(true);
            categoryDeleteBtn.setDisable(true);
        }

        if(brandTableDataModel.getId() == "-All-" && categoryTableDataModel.getId() == "-All-") {
            modelCreateBtn.setDisable(true);
            modelTable.getItems().clear();
            loadModelTable();
        } else if (brandTableDataModel.getId() == "-All-") {
            modelCreateBtn.setDisable(true);
            modelTable.getItems().clear();

            List<CarModelDto> carModelDtos = carModelService.getAllModels();
            for (CarModelDto carModelDto : carModelDtos) {
                if(carModelDto.getCategory() == categoryTableDataModel.getId()) {
                    modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
                }
            }
        } else if (categoryTableDataModel.getId() == "-All-") {
            modelCreateBtn.setDisable(true);
            modelTable.getItems().clear();

            List<CarModelDto> carModelDtos = carModelService.getAllModels();
            for (CarModelDto carModelDto : carModelDtos) {
                if(carModelDto.getBrand() == brandTableDataModel.getId()) {
                    modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
                }
            }
        } else {
            modelCreateBtn.setDisable(false);
            modelTable.getItems().clear();

            List<CarModelDto> carModelDtos = carModelService.getAllModels();
            for (CarModelDto carModelDto : carModelDtos) {
                if(carModelDto.getCategory() == categoryTableDataModel.getId() && carModelDto.getBrand() == brandTableDataModel.getId()) {
                    modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
                }
            }
        }

        modelTableDataModel = (TableDataModel) modelTable.getSelectionModel().getSelectedItem();

        if(modelTableDataModel != null) {
            modelUpdateBtn.setDisable(false);
            modelDeleteBtn.setDisable(false);
        } else {
            modelUpdateBtn.setDisable(true);
            modelDeleteBtn.setDisable(true);
        }
    }

    public void brandTableOnMouseClicked(MouseEvent mouseEvent) {
        categoryTableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();
        brandTableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();

        if(brandTableDataModel.getId() != "-All-") {
            brandUpdateBtn.setDisable(false);
            brandDeleteBtn.setDisable(false);
        } else {
            brandUpdateBtn.setDisable(true);
            brandDeleteBtn.setDisable(true);
        }


        if(brandTableDataModel.getId() == "-All-" && categoryTableDataModel.getId() == "-All-") {
            modelCreateBtn.setDisable(true);
            modelTable.getItems().clear();
            loadModelTable();
        } else if (categoryTableDataModel.getId() == "-All-") {
            modelCreateBtn.setDisable(true);
            modelTable.getItems().clear();

            List<CarModelDto> carModelDtos = carModelService.getAllModels();
            for (CarModelDto carModelDto : carModelDtos) {
                if(carModelDto.getBrand() == brandTableDataModel.getId()) {
                    modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
                }
            }
        } else if (brandTableDataModel.getId() == "-All-") {
            modelCreateBtn.setDisable(true);
            modelTable.getItems().clear();

            List<CarModelDto> carModelDtos = carModelService.getAllModels();
            for (CarModelDto carModelDto : carModelDtos) {
                if(carModelDto.getCategory() == categoryTableDataModel.getId()) {
                    modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
                }
            }
        } else {
            modelCreateBtn.setDisable(false);
            modelTable.getItems().clear();

            List<CarModelDto> carModelDtos = carModelService.getAllModels();
            for (CarModelDto carModelDto : carModelDtos) {
                if(carModelDto.getCategory() == categoryTableDataModel.getId() && carModelDto.getBrand() == brandTableDataModel.getId()) {
                    modelTable.getItems().add(new TableDataModel(carModelDto.getModel()));
                }
            }
        }

        modelTableDataModel = (TableDataModel) modelTable.getSelectionModel().getSelectedItem();

        if(modelTableDataModel != null) {
            modelUpdateBtn.setDisable(false);
            modelDeleteBtn.setDisable(false);
        } else {
            modelUpdateBtn.setDisable(true);
            modelDeleteBtn.setDisable(true);
        }
    }

    public void modelTableOnMouseClicked(MouseEvent mouseEvent) {
        categoryTableDataModel = (TableDataModel) categoryTable.getSelectionModel().getSelectedItem();
        brandTableDataModel = (TableDataModel) brandTable.getSelectionModel().getSelectedItem();
        modelTableDataModel = (TableDataModel) modelTable.getSelectionModel().getSelectedItem();

        modelUpdateBtn.setDisable(false);
        modelDeleteBtn.setDisable(false);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public class TableDataModel {
        private String id;
    }
}
