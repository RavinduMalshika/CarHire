package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CarDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CarService;
import lombok.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CarListViewController {
    public AnchorPane formNode;
    public TableView carTable;
    public Button updateBtn;
    public Button deleteBtn;
    private CarService carService;
    private static CarDto selectedDto;

    public CarListViewController() {
        carService = (CarService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR);
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
