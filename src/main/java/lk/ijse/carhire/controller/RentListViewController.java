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
import lk.ijse.carhire.dto.RentDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.RentService;
import lombok.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class RentListViewController {
    public AnchorPane formNode;
    public TableView rentTable;
    public Button updateBtn;
    public Button deleteBtn;
    private RentService rentService;
    private static RentDto selectedDto;

    public RentListViewController() {
        rentService = (RentService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.RENT);
        rentTable = new TableView();
    }

    @FXML
    public void initialize() {
        loadTable();
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    public void newBtnOnAction(ActionEvent actionEvent) {
        try {
            HBox formNode = FXMLLoader.load(this.getClass().getResource("/view/rent_form_view.fxml"));
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
            HBox formNode = FXMLLoader.load(this.getClass().getResource("/view/rent_update_form_view.fxml"));
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
            RentDto rentDto = getTableSelection();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Rent?");
            alert.setHeaderText("Confirm to delete entry");
            alert.setContentText("Rent ID: " + rentDto.getRentId() + "\nCustomer ID: " + rentDto.getCustomerId() + "\nCar ID: " + rentDto.getCarId());
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType buttonType = result.orElse(ButtonType.CANCEL);

            if(buttonType == ButtonType.OK) {
                String resp = rentService.deleteRent(rentDto.getRentId());
                reload();
                new Alert(Alert.AlertType.INFORMATION, resp).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void loadTable() {
        rentTable.getColumns().clear();

        TableColumn rentId = new TableColumn("ID");
        rentId.prefWidthProperty().bind(rentTable.widthProperty().divide(6).subtract(2));
        rentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));

        TableColumn customerId = new TableColumn("Customer ID");
        customerId.prefWidthProperty().bind(rentTable.widthProperty().divide(6).subtract(2));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn carId = new TableColumn("Car ID");
        carId.prefWidthProperty().bind(rentTable.widthProperty().divide(6));
        carId.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn fromDate = new TableColumn("From");
        fromDate.prefWidthProperty().bind(rentTable.widthProperty().divide(6));
        fromDate.setCellValueFactory(new PropertyValueFactory<>("fromDate"));

        TableColumn toDate = new TableColumn("To");
        toDate.prefWidthProperty().bind(rentTable.widthProperty().divide(6));
        toDate.setCellValueFactory(new PropertyValueFactory<>("toDate"));

        TableColumn isReturned = new TableColumn("Is Returned");
        isReturned.prefWidthProperty().bind(rentTable.widthProperty().divide(6));
        isReturned.setCellValueFactory(new PropertyValueFactory<>("isReturned"));

        rentTable.getColumns().addAll(rentId, customerId, carId, fromDate, toDate, isReturned);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<RentDto> rentDtos = rentService.getAllRents();
        for (RentDto rentDto : rentDtos) {
            rentTable.getItems().add(new RentTableDataModel(
                    rentDto.getRentId(),
                    rentDto.getCustomerId(),
                    rentDto.getCarId(),
                    sdf.format(rentDto.getFromDate()),
                    sdf.format(rentDto.getToDate()),
                    rentDto.getIsReturned()
            ));
        }
    }

    public static RentDto getTableSelection() {
        return selectedDto;
    }

    public void reload() throws IOException {
        this.formNode.getChildren().clear();
        this.formNode.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/rent_list_view.fxml")));
    }

    public void rentTableOnMouseClicked(MouseEvent mouseEvent) {
        updateBtn.setDisable(false);
        deleteBtn.setDisable(false);

        RentTableDataModel rentTableDataModel = (RentTableDataModel) rentTable.getSelectionModel().getSelectedItem();
        if(rentTableDataModel != null) {
            selectedDto = rentService.getRent(rentTableDataModel.getRentId());

            if(mouseEvent.getClickCount() == 2) {
                try{
                    Stage stage = new Stage();
                    Parent node = FXMLLoader.load(this.getClass().getResource("/view/rent_detail_view.fxml"));

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
    public class RentTableDataModel {
        private String rentId;
        private String customerId;
        private String carId;
        private String fromDate;
        private String toDate;
        private String isReturned;
    }
}
