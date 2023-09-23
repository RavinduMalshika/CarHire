package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CustomerDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CustomerService;
import lombok.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CustomerListViewController {
    public AnchorPane formNode;
    public TableView customerTable;
    public Button updateBtn;
    public Button deleteBtn;
    private CustomerService customerService;
    private static CustomerDto selectedDto;

    public CustomerListViewController() {
        customerService = (CustomerService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CUSTOMER);
        customerTable = new TableView();
        loadTable();
    }

    @FXML
    public void initialize() {
        loadTable();
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    public void newBtnOnAction(ActionEvent actionEvent) {
        try {
            HBox formNode = FXMLLoader.load(this.getClass().getResource("/view/customer_form_view.fxml"));
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
            HBox formNode = FXMLLoader.load(this.getClass().getResource("/view/customer_update_form_view.fxml"));
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
            CustomerDto customerDto = getTableSelection();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer?");
            alert.setHeaderText("Confirm to delete entry");
            alert.setContentText("Customer ID: " + customerDto.getId() + "\nName: " + customerDto.getFirstName() + " " + customerDto.getLastName());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = customerService.deleteCustomer(customerDto.getId());
                reload();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void loadTable() {
        customerTable.getColumns().clear();

        TableColumn customerId = new TableColumn("ID");
        customerId.prefWidthProperty().bind(customerTable.widthProperty().divide(10).subtract(2));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn name = new TableColumn("Name");
        name.prefWidthProperty().bind(customerTable.widthProperty().divide(10).multiply(3));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn nic = new TableColumn("NIC");
        nic.prefWidthProperty().bind(customerTable.widthProperty().divide(5));
        nic.setCellValueFactory(new PropertyValueFactory<>("nic"));

        TableColumn address = new TableColumn("Address");
        address.prefWidthProperty().bind(customerTable.widthProperty().divide(10).multiply(3));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn renting = new TableColumn("Renting");
        renting.prefWidthProperty().bind(customerTable.widthProperty().divide(10));
        renting.setCellValueFactory(new PropertyValueFactory<>("renting"));

        customerTable.getColumns().addAll(customerId, name, nic, address, renting);

        List<CustomerDto> customerDtos = customerService.getAllCustomers();
        for(CustomerDto customerDto : customerDtos) {
            customerTable.getItems().add(new CustomerTableDataModel(
                    customerDto.getId(),
                    customerDto.getTitle() + ". " + customerDto.getFirstName() + " " + customerDto.getLastName(),
                    customerDto.getNic(),
                    customerDto.getAddress() + ", " + customerDto.getCity() + ", " + customerDto.getProvince(),
                    customerDto.getRenting()
            ));
        }
    }

    public static CustomerDto getTableSelection () {
        return selectedDto;
    }

    public void reload() throws IOException {
        this.formNode.getChildren().clear();
        this.formNode.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/customer_list_view.fxml")));
    }

    public void customerTableOnMouseClicked(MouseEvent mouseEvent) {
        updateBtn.setDisable(false);
        deleteBtn.setDisable(false);

        CustomerTableDataModel customerTableDataModel = (CustomerTableDataModel) customerTable.getSelectionModel().getSelectedItem();
        if(customerTableDataModel != null) {
            selectedDto = customerService.getCustomer(customerTableDataModel.getCustomerId());

            if (mouseEvent.getClickCount() == 2) {
                try{
                    Stage stage = new Stage();
                    Parent node = FXMLLoader.load(this.getClass().getResource("/view/customer_detail_view.fxml"));

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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public class CustomerTableDataModel {
        private String customerId;
        private String name;
        private String nic;
        private String address;
        private String renting;
    }
}
