package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CustomerDto;

public class CustomerDetailViewController {
    public Label idLabel;
    public Label nameLabel;
    public Label nicLabel;
    public Label emailLabel;
    public Label addressLabel;
    public Label phone1Label;
    public Label phone2Label;
    public Label createdByLabel;
    public Label updatedByLabel;
    public AnchorPane formNode;

    public void initialize() {
        try {
            CustomerDto customerDto = CustomerListViewController.getTableSelection();

            idLabel.setText(idLabel.getText() + " " + customerDto.getId());
            nameLabel.setText(nameLabel.getText() + " " + customerDto.getTitle() + ". " + customerDto.getFirstName() + " " + customerDto.getLastName());
            nicLabel.setText(nicLabel.getText() + " " + customerDto.getNic());
            emailLabel.setText(emailLabel.getText() + " " + customerDto.getEmail());
            addressLabel.setText(addressLabel.getText() + " " + customerDto.getAddress() + " " + customerDto.getCity() + " " + customerDto.getProvince());
            phone1Label.setText(phone1Label.getText() + " " + (customerDto.getPhone().get(0) == null ? "" : customerDto.getPhone().get(0)));
            phone2Label.setText(phone2Label.getText() + " " + (customerDto.getPhone().get(1) == null ? "" : customerDto.getPhone().get(1)));
            createdByLabel.setText(createdByLabel.getText() + " " + customerDto.getCreatedBy());
            updatedByLabel.setText(updatedByLabel.getText() + " " + (customerDto.getUpdatedBy() == null ? "" : customerDto.getUpdatedBy()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void closeBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) this.formNode.getScene().getWindow();
        stage.close();
    }
}
