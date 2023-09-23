package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CarBrandDto;
import lk.ijse.carhire.dto.CarCategoryDto;
import lk.ijse.carhire.dto.CarDto;
import lk.ijse.carhire.dto.CarModelDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CarBrandService;
import lk.ijse.carhire.service.custom.CarCategoryService;
import lk.ijse.carhire.service.custom.CarModelService;
import lk.ijse.carhire.service.custom.CarService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarFormViewController {
    public HBox formNode;
    public TextField yearField;
    public TextField vehicleNumberField;
    public TextField dailyRentalField;
    public ComboBox categoryComboBox;
    public ComboBox brandComboBox;
    public ComboBox modelComboBox;
    public Label idLabel;
    private CarService carService;
    private CarCategoryService carCategoryService;
    private CarBrandService carBrandService;
    private CarModelService carModelService;
    private String errorMsg;

    public CarFormViewController() {
        carService = (CarService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR);
        carCategoryService = (CarCategoryService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_CATEGORY);
        carBrandService = (CarBrandService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_BRAND);
        carModelService = (CarModelService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_MODEL);
    }

    public void initialize() {
        categoryComboBox.getItems().clear();
        List<CarCategoryDto> carCategoryDtos = carCategoryService.getAllCategories();
        ArrayList<String> categories = new ArrayList<>();
        for(CarCategoryDto carCategoryDto : carCategoryDtos) {
            categories.add(carCategoryDto.getCategory());
        }
        categoryComboBox.getItems().addAll(categories);

        brandComboBox.getItems().clear();
        List<CarBrandDto> carBrandDtos = carBrandService.getAllBrands();
        ArrayList<String> brands = new ArrayList<>();
        for(CarBrandDto carBrandDto : carBrandDtos) {
            brands.add(carBrandDto.getBrand());
        }
        brandComboBox.getItems().addAll(brands);

        modelComboBox.getItems().clear();
        List<CarModelDto> carModelDtos = carModelService.getAllModels();
        ArrayList<String> models = new ArrayList<>();
        for(CarModelDto carModelDto : carModelDtos) {
            models.add(carModelDto.getModel());
        }
        modelComboBox.getItems().addAll(models);

        idLabel.setText(generateId());
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        try {
            CarDto carDto = new CarDto();

            errorMsg = validateData();
            if(errorMsg == null) {
                carDto.setId(idLabel.getText());
                carDto.setVehicleNumber((vehicleNumberField.getText()));
                carDto.setCarCategory(String.valueOf(categoryComboBox.getSelectionModel().getSelectedItem()));
                carDto.setCarBrand(String.valueOf(brandComboBox.getSelectionModel().getSelectedItem()));
                carDto.setCarModel(String.valueOf(modelComboBox.getSelectionModel().getSelectedItem()));
                carDto.setYear(Integer.parseInt(yearField.getText()));
                carDto.setDailyRental(Double.parseDouble(dailyRentalField.getText()));
                carDto.setAvailability("Available");
                carDto.setCreatedBy(LoginFormController.passUser());
                String resp = carService.saveCar(carDto);
                new Alert(Alert.AlertType.INFORMATION, resp).show();
                loadList();
            } else {
                new Alert(Alert.AlertType.ERROR, errorMsg).show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }
    }

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        try {
            loadList();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void loadList() throws IOException {
        if((Stage) this.formNode.getScene().getWindow() == LoginFormController.passStage()) {
            AnchorPane formNode = FXMLLoader.load(this.getClass().getResource("/view/car_list_view.fxml"));
            AnchorPane.setTopAnchor(formNode, 0.0);
            AnchorPane.setBottomAnchor(formNode, 0.0);
            AnchorPane.setLeftAnchor(formNode, 0.0);
            AnchorPane.setRightAnchor(formNode, 0.0);
            AnchorPane rootNode = (AnchorPane) this.formNode.getParent();
            rootNode.getChildren().clear();
            rootNode.getChildren().add(formNode);
        } else {
            Stage stage = (Stage) this.formNode.getScene().getWindow();
            stage.close();
        }
    }

    public String generateId() {
        try {
            List<CarDto> carDtos = carService.getAllCars();
            CarDto carDto = carDtos.get(carDtos.size() - 1);

            String prevId = carDto.getId().substring(3);
            Integer incremetedId = Integer.parseInt(prevId) + 1;
            String id = "CAR" + String.format("%04d", incremetedId);
            return id;

        } catch (Exception e) {
            return "CAR0001";
        }
    }

    public String validateData() {
        Boolean isYearValid;
        try {
            Integer.parseInt(yearField.getText());
            isYearValid = true;
        } catch (Exception e) {
            isYearValid = false;
        }

        Boolean isDailyRentalValid;
        try {
            Double.parseDouble(dailyRentalField.getText());
            isDailyRentalValid = true;
        } catch (Exception e) {
            isDailyRentalValid = false;
        }

        if(vehicleNumberField.getText() == ""){
            return "Empty Vehicle Number";
        } else if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
            return "Category not selected";
        } else if (brandComboBox.getSelectionModel().getSelectedItem() == null) {
            return "Brand not selected";
        } else if (modelComboBox.getSelectionModel().getSelectedItem() == null) {
            return "Model not selected";
        } else if (!isYearValid) {
            return "Invalid Year Entered";
        } else if (!isDailyRentalValid) {
            return "Invalid Daily Rental";
        }else {
            return null;
        }
    }
}
