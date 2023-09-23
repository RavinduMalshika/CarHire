package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CustomerDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CustomerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormViewController {
    public HBox formNode;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField nicField;
    public TextField addressField;
    public TextField cityField;
    public ComboBox provinceComboBox;
    public ComboBox titleComboBox;
    public TextField phone1Field;
    public TextField phone2Field;
    public TextField emailField;
    public Label idLabel;
    private CustomerService customerService;
    private String errorMsg;

    public CustomerFormViewController() {
        customerService = (CustomerService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CUSTOMER);
    }

    @FXML
    public void initialize() {
        titleComboBox.getItems().clear();
        titleComboBox.getItems().addAll("Mr", "Ms", "Mrs");

        provinceComboBox.getItems().clear();
        provinceComboBox.getItems().addAll("Central", "Eastern", "North Central", "North Western", "Northern", "Sabaragamuwa", "Southern", "Uva", "Western");

        idLabel.setText(generateId());
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        try {
            CustomerDto customerDto = new CustomerDto();

            errorMsg = validateData();
            if(errorMsg == null) {
                customerDto.setId(idLabel.getText());
                customerDto.setTitle(String.valueOf(titleComboBox.getSelectionModel().getSelectedItem()));
                customerDto.setFirstName(firstNameField.getText());
                customerDto.setLastName(lastNameField.getText());
                customerDto.setNic(nicField.getText());
                customerDto.setAddress(addressField.getText());
                customerDto.setCity(cityField.getText());
                customerDto.setProvince(String.valueOf(provinceComboBox.getSelectionModel().getSelectedItem()));
                List<String> phone = new ArrayList<>();
                phone.add(phone1Field.getText());
                phone.add(phone2Field.getText());
                customerDto.setPhone(phone);
                customerDto.setEmail(emailField.getText());
                customerDto.setRenting("No");
                customerDto.setCreatedBy(LoginFormController.passUser());

                String resp = customerService.saveCustomer(customerDto);
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
            AnchorPane formNode = FXMLLoader.load(this.getClass().getResource("/view/customer_list_view.fxml"));
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
            List<CustomerDto> customerDtos = customerService.getAllCustomers();
            CustomerDto customerDto = customerDtos.get(customerDtos.size() - 1);

            String prevId = customerDto.getId().substring(4);
            Integer incremetedId = Integer.parseInt(prevId) + 1;
            String id = "CUST" + String.format("%04d", incremetedId);
            return id;

        } catch (Exception e) {
            return "CUST0001";
        }
    }

    public String validateData() {
        Boolean isPhone1Valid;
        try {
            Integer.parseInt(phone1Field.getText());
            isPhone1Valid = true;
        } catch (Exception e) {
            isPhone1Valid = false;
        }

        Boolean isPhone2Valid;
        try {
            Integer.parseInt(phone1Field.getText());
            isPhone2Valid = true;
        } catch (Exception e) {
            isPhone2Valid = false;
        }

        if(firstNameField.getText() == "") {
            return "First Name not entered";
        } else if (lastNameField.getText() == "") {
            return "Last Name not entered";
        } else if (nicField.getText() == "") {
            return "NIC not entered";
        } else if (addressField.getText() == "") {
            return "Address not entered";
        } else if (cityField.getText() == "") {
            return "City not entered";
        } else if (provinceComboBox.getSelectionModel().getSelectedItem() == null) {
            return "Province not entered";
        } else if (!(isPhone1Valid || phone1Field.getText() == "")) {
            return "Invalid Phone Number 1";
        } else if (!(isPhone2Valid || phone2Field.getText() == "")) {
            return "Invalid Phone Number 2";
        }else if (!(emailField.getText().contains("@") || emailField.getText() == "")) {
            return "Invalid email";
        } else {
            return null;
        }
    }
}
