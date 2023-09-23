package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CarDto;

public class CarDetailViewController {
    public AnchorPane formNode;
    public Label idLabel;
    public Label vehicleNumberLabel;
    public Label categoryLabel;
    public Label modelLabel;
    public Label dailyRentalLabel;
    public Label availabilityLabel;
    public Label brandLabel;
    public Label createdByLabel;
    public Label updatedByLabel;
    public Label yearLabel;

    public void initialize() {
        CarDto carDto = CarListViewController.getTableSelection();

        idLabel.setText(idLabel.getText() + " " + carDto.getId());
        vehicleNumberLabel.setText(vehicleNumberLabel.getText() + " " + carDto.getVehicleNumber());
        categoryLabel.setText(categoryLabel.getText() + " " + carDto.getCarCategory());
        brandLabel.setText(brandLabel.getText() + " " + carDto.getCarBrand());
        modelLabel.setText(modelLabel.getText() + " " + carDto.getCarModel());
        yearLabel.setText(yearLabel.getText() + " " + carDto.getYear());
        dailyRentalLabel.setText(dailyRentalLabel.getText() + " " + carDto.getDailyRental());
        availabilityLabel.setText(availabilityLabel.getText() + " " + carDto.getAvailability());
        createdByLabel.setText(categoryLabel.getText() + " " + carDto.getCreatedBy());
        updatedByLabel.setText(updatedByLabel.getText() + " " + (carDto.getUpdatedBy() == null ? "" : carDto.getUpdatedBy()));
    }

    public void closeBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) this.formNode.getScene().getWindow();
        stage.close();
    }
}
