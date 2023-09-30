package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CarBrandDto;
import lk.ijse.carhire.dto.CarCategoryDto;
import lk.ijse.carhire.dto.CarDto;
import lk.ijse.carhire.dto.CustomerDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CarBrandService;
import lk.ijse.carhire.service.custom.CarCategoryService;
import lk.ijse.carhire.service.custom.CarModelService;
import lk.ijse.carhire.service.custom.CarService;
import lk.ijse.carhire.util.CarReport;
import lk.ijse.carhire.util.CustomerReport;
import lk.ijse.carhire.util.OpenFile;
import lombok.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CarListViewController {
    public AnchorPane formNode;
    public TableView carTable;
    public Button updateBtn;
    public Button deleteBtn;
    private CarService carService;
    private static CarDto selectedDto;
    private CarCategoryService carCategoryService;
    private CarBrandService carBrandService;

    public CarListViewController() {
        carService = (CarService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR);
        carCategoryService = (CarCategoryService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_CATEGORY);
        carBrandService = (CarBrandService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR_BRAND);
        carTable = new TableView();
    }

    public void initialize() {
        loadTable();

        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }
    public void newBtnOnAction(ActionEvent actionEvent) {
        try {
            HBox formNode = FXMLLoader.load(this.getClass().getResource("/view/car_form_view.fxml"));
            AnchorPane.setTopAnchor(formNode, 0.0);
            AnchorPane.setBottomAnchor(formNode, 0.0);
            AnchorPane.setLeftAnchor(formNode, 0.0);
            AnchorPane.setRightAnchor(formNode, 0.0);
            this.formNode.getChildren().clear();
            this.formNode.getChildren().add(formNode);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        try {
            HBox formNode = FXMLLoader.load(this.getClass().getResource("/view/car_update_form_view.fxml"));
            AnchorPane.setTopAnchor(formNode, 0.0);
            AnchorPane.setBottomAnchor(formNode, 0.0);
            AnchorPane.setLeftAnchor(formNode, 0.0);
            AnchorPane.setRightAnchor(formNode, 0.0);
            this.formNode.getChildren().clear();
            this.formNode.getChildren().add(formNode);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        try {
            CarDto carDto = getTableSelection();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Car?");
            alert.setHeaderText("Confirm to delete entry");
            alert.setContentText("Car ID: " + carDto.getId() + "\nName: " + carDto.getCarBrand() + " " + carDto.getCarModel());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = carService.deleteCar(carDto.getId());
                reload();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void loadTable() {
        carTable.getColumns().clear();

        TableColumn carId = new TableColumn("ID");
        carId.prefWidthProperty().bind(carTable.widthProperty().divide(6).subtract(4));
        carId.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn vehicleNumber = new TableColumn("Vehicle Number");
        vehicleNumber.prefWidthProperty().bind(carTable.widthProperty().divide(6));
        vehicleNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));

        TableColumn category = new TableColumn("Category");
        category.prefWidthProperty().bind(carTable.widthProperty().divide(6));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn name = new TableColumn("Name");
        name.prefWidthProperty().bind(carTable.widthProperty().divide(6));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn dailyRental = new TableColumn("Daily Rental");
        dailyRental.prefWidthProperty().bind(carTable.widthProperty().divide(6));
        dailyRental.setCellValueFactory(new PropertyValueFactory<>("dailyRental"));

        TableColumn availability = new TableColumn("Availability");
        availability.prefWidthProperty().bind(carTable.widthProperty().divide(6));
        availability.setCellValueFactory(new PropertyValueFactory<>("availability"));

        carTable.getColumns().addAll(carId, vehicleNumber, category, name, dailyRental, availability);

        List<CarDto> carDtos = carService.getAllCars();
        for(CarDto carDto : carDtos) {
            carTable.getItems().add(new CarTableDataModel(
                    carDto.getId(),
                    carDto.getVehicleNumber(),
                    carDto.getCarCategory(),
                    carDto.getYear() + " " + carDto.getCarBrand() + " " + carDto.getCarModel(),
                    carDto.getDailyRental(),
                    carDto.getAvailability()
            ));
        }
    }

    public void carTableOnMouseClicked(MouseEvent mouseEvent) {
        updateBtn.setDisable(false);
        deleteBtn.setDisable(false);

        CarTableDataModel carTableDataModel = (CarTableDataModel) carTable.getSelectionModel().getSelectedItem();

        if(carTableDataModel != null) {
            selectedDto = carService.getCar(carTableDataModel.getCarId());

            if (mouseEvent.getClickCount() == 2) {
                try{
                    Stage stage = new Stage();
                    Parent node = FXMLLoader.load(this.getClass().getResource("/view/car_detail_view.fxml"));

                    Scene scene = new Scene(node);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }

    }

    public static CarDto getTableSelection() {
        return selectedDto;
    }

    public void reload() throws IOException {
        this.formNode.getChildren().clear();
        this.formNode.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/car_list_view.fxml")));
    }

    public void printBtnOnAction(ActionEvent actionEvent) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Print Car Details");
        dialog.setHeaderText("Select the required filter criteria");

        ToggleGroup group = new ToggleGroup();
        RadioButton all = new RadioButton("All Cars");

        RadioButton category = new RadioButton("Filter by Category");
        Label categoryLabel = new Label("Category: ");
        ComboBox categoryComboBox = new ComboBox();
        categoryComboBox.getItems().clear();
        List<CarCategoryDto> carCategoryDtos = carCategoryService.getAllCategories();
        ArrayList<String> categories = new ArrayList<>();
        for(CarCategoryDto carCategoryDto : carCategoryDtos) {
            categories.add(carCategoryDto.getCategory());
        }
        categoryComboBox.getItems().addAll(categories);
        categoryComboBox.getSelectionModel().select(0);

        RadioButton brand = new RadioButton("Filter by Brand");
        Label brandLabel = new Label("Brand: ");
        ComboBox brandComboBox = new ComboBox();
        brandComboBox.getItems().clear();
        List<CarBrandDto> carBrandDtos = carBrandService.getAllBrands();
        ArrayList<String> brands = new ArrayList<>();
        for(CarBrandDto carBrandDto : carBrandDtos) {
            brands.add(carBrandDto.getBrand());
        }
        brandComboBox.getItems().addAll(brands);
        brandComboBox.getSelectionModel().select(0);

        RadioButton available = new RadioButton("Filter by Car Availability");
        Label availableLabel = new Label("Availability: ");
        ComboBox availableComboBox = new ComboBox();
        availableComboBox.getItems().clear();
        availableComboBox.getItems().addAll("Available", "Unavailable");
        availableComboBox.getSelectionModel().select(0);

        group.getToggles().addAll(all, category, brand, available);
        ButtonType print = new ButtonType("Print");

        dialog.getDialogPane().getButtonTypes().addAll(print, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(all, 0, 0);
        gridPane.add(category, 0, 1);
        gridPane.add(categoryLabel, 1, 1);
        gridPane.add(categoryComboBox, 2, 1);
        gridPane.add(brand, 0, 2);
        gridPane.add(brandLabel, 1, 2);
        gridPane.add(brandComboBox, 2, 2);
        gridPane.add(available, 0, 3);
        gridPane.add(availableLabel, 1, 3);
        gridPane.add(availableComboBox, 2, 3);
        dialog.getDialogPane().setContent(gridPane);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(all)) {
                categoryComboBox.setDisable(true);
                brandComboBox.setDisable(true);
                availableComboBox.setDisable(true);
            } else if (newValue.equals(category)) {
                categoryComboBox.setDisable(false);
                brandComboBox.setDisable(true);
                availableComboBox.setDisable(true);
            } else if (newValue.equals(brand)) {
                categoryComboBox.setDisable(true);
                brandComboBox.setDisable(false);
                availableComboBox.setDisable(true);
            } else if (newValue.equals(available)) {
                categoryComboBox.setDisable(true);
                brandComboBox.setDisable(true);
                availableComboBox.setDisable(false);
            }
        });

        all.setSelected(true);

        Optional<ButtonType> result = dialog.showAndWait();
        ButtonType buttonType = result.orElse(ButtonType.CANCEL);

        if(buttonType == print && all.isSelected()) {
            printAll();
        } else if (buttonType == print && category.isSelected()) {
            printFilteredByCategory((String) categoryComboBox.getSelectionModel().getSelectedItem());
        } else if (buttonType == print && brand.isSelected()) {
            printFilteredByBrand((String) brandComboBox.getSelectionModel().getSelectedItem());
        } else if (buttonType == print && available.isSelected()) {
            printFilteredByAvailability((String) availableComboBox.getSelectionModel().getSelectedItem());
        }
    }

    public void printAll() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String filePath = "src/main/resources/report/car_report.jrxml";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("filter", "Showing All Cars");

            List<CarDto> carDtos = carService.getAllCars();
            List<CarReport> list = new ArrayList<>();
            for(CarDto carDto : carDtos) {
                CarReport entry = new CarReport(
                        carDto.getId(),
                        carDto.getVehicleNumber(),
                        carDto.getCarCategory(),
                        carDto.getCarBrand(),
                        carDto.getCarModel(),
                        carDto.getYear(),
                        carDto.getDailyRental(),
                        carDto.getAvailability()
                );
                list.add(entry);
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            String location = "Reports/Car Reports/Car Report " + sdf.format(new Date()) +".pdf";
            JasperExportManager.exportReportToPdfFile(print, location);
            OpenFile.openFile(location);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printFilteredByCategory(String category) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String filePath = "src/main/resources/report/car_report.jrxml";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("filter", "Filtered by Category: " + category);

            List<CarDto> carDtos = carService.getAllCars();
            List<CarReport> list = new ArrayList<>();
            for(CarDto carDto : carDtos) {
                if(carDto.getCarCategory().equals(category)) {
                    CarReport entry = new CarReport(
                            carDto.getId(),
                            carDto.getVehicleNumber(),
                            carDto.getCarCategory(),
                            carDto.getCarBrand(),
                            carDto.getCarModel(),
                            carDto.getYear(),
                            carDto.getDailyRental(),
                            carDto.getAvailability()
                    );
                    list.add(entry);
                }
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            String location = "Reports/Car Reports/Car Report Filtered by Category(" + category + ") " + sdf.format(new Date()) +".pdf";
            JasperExportManager.exportReportToPdfFile(print, location);
            OpenFile.openFile(location);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printFilteredByBrand(String brand) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String filePath = "src/main/resources/report/car_report.jrxml";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("filter", "Filtered by Brand: " + brand);

            List<CarDto> carDtos = carService.getAllCars();
            List<CarReport> list = new ArrayList<>();
            for(CarDto carDto : carDtos) {
                if(carDto.getCarBrand().equals(brand)) {
                    CarReport entry = new CarReport(
                            carDto.getId(),
                            carDto.getVehicleNumber(),
                            carDto.getCarCategory(),
                            carDto.getCarBrand(),
                            carDto.getCarModel(),
                            carDto.getYear(),
                            carDto.getDailyRental(),
                            carDto.getAvailability()
                    );
                    list.add(entry);
                }
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            String location = "Reports/Car Reports/Car Report Filtered by Brand(" + brand + ") " + sdf.format(new Date()) +".pdf";
            JasperExportManager.exportReportToPdfFile(print, location);
            OpenFile.openFile(location);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printFilteredByAvailability(String availability) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String filePath = "src/main/resources/report/car_report.jrxml";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("filter", "Filtered by Availability: " + availability);

            List<CarDto> carDtos = carService.getAllCars();
            List<CarReport> list = new ArrayList<>();
            for(CarDto carDto : carDtos) {
                if(carDto.getAvailability().equals(availability)) {
                    CarReport entry = new CarReport(
                            carDto.getId(),
                            carDto.getVehicleNumber(),
                            carDto.getCarCategory(),
                            carDto.getCarBrand(),
                            carDto.getCarModel(),
                            carDto.getYear(),
                            carDto.getDailyRental(),
                            carDto.getAvailability()
                    );
                    list.add(entry);
                }
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            String location = "Reports/Car Reports/Car Report Filtered by Availability(" + availability + ") " + sdf.format(new Date()) +".pdf";
            JasperExportManager.exportReportToPdfFile(print, location);
            OpenFile.openFile(location);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public class CarTableDataModel {
        private String carId;
        private String vehicleNumber;
        private String category;
        private String name;
        private Double dailyRental;
        private String availability;
    }
}
