package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.CarDto;
import lk.ijse.carhire.dto.CustomerDto;
import lk.ijse.carhire.dto.RentDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.CarService;
import lk.ijse.carhire.service.custom.CustomerService;
import lk.ijse.carhire.service.custom.RentService;
import lk.ijse.carhire.util.DateUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class RentFormViewController {
    public TextField customerIdField;
    public TextField carIdField;
    public TextField depositField;
    public TextField advanceField;
    public Label dailyRentalLabel;
    public Label totalLabel;
    public DatePicker fromDate;
    public DatePicker toDate;
    public HBox formNode;
    public Label customerStatusLabel;
    public Label carStatusLabel;
    public Label fromDateStatusLabel;
    public Label toDateStatusLabel;
    public Label rentIdLabel;
    private RentService rentService;
    private CustomerService customerService;
    private CarService carService;
    private String errorMsg;
    private String customerErrorMsg;
    private String carErrorMsg;

    public RentFormViewController() {
        rentService = (RentService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.RENT);
        customerService = (CustomerService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CUSTOMER);
        carService = (CarService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.CAR);
    }

    public void initialize() {
        rentIdLabel.setText(generateId());
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        try {
            RentDto rentDto = new RentDto();

            if(errorMsg == null) {
                errorMsg = validateData();
            }

            if(errorMsg == null) {
                rentDto.setRentId(rentIdLabel.getText());
                rentDto.setCustomerId(customerIdField.getText());
                rentDto.setCarId(carIdField.getText());
                rentDto.setFromDate(DateUtil.asDate(fromDate.getValue()));
                rentDto.setToDate(DateUtil.asDate(toDate.getValue()));
                rentDto.setDailyRental(Double.parseDouble(dailyRentalLabel.getText()));
                rentDto.setTotal(Double.parseDouble(totalLabel.getText()));
                rentDto.setRefundableDeposit(depositField.getText() == "" ? 0.0 : Double.parseDouble(depositField.getText()));
                rentDto.setAdvancePayment(advanceField.getText() == "" ? 0.0 : Double.parseDouble(advanceField.getText()));
                rentDto.setIsReturned("Not Returned");
                rentDto.setDaysOverdue(0);
                rentDto.setBalance(0.0);
                rentDto.setCreatedBy(LoginFormController.passUser());

                String resp = rentService.saveRent(rentDto);
                new Alert(Alert.AlertType.INFORMATION, resp).show();
                loadList();
            } else {
                new Alert(Alert.AlertType.ERROR, errorMsg).show();
                errorMsg = null;
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

    public void customerCreateBtnOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent node = FXMLLoader.load(this.getClass().getResource("/view/customer_form_view.fxml"));
            Scene scene = new Scene(node);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Create New Customer");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void carCreateBtnOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent node = FXMLLoader.load(this.getClass().getResource("/view/car_form_view.fxml"));
            Scene scene = new Scene(node);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Create New Car");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void customerIdOnKeyTyped(KeyEvent keyEvent) {
        CustomerDto customerDto = customerService.getCustomer(customerIdField.getText());

        if(customerDto != null) {
            if(customerDto.getRenting().equals("No")) {
                customerStatusLabel.setText(customerDto.getTitle() + ". " + customerDto.getFirstName() + " " + customerDto.getLastName());
                customerErrorMsg = null;
            } else {
                customerStatusLabel.setText("Customer already renting");
                customerErrorMsg = "Customer already renting";
            }
        } else {
            customerStatusLabel.setText("Invalid Customer ID");
            customerErrorMsg = "Invalid Customer ID";
        }
    }

    public void carIdOnKeyTyped(KeyEvent keyEvent) {
        CarDto carDto = carService.getCar(carIdField.getText());

        if(carDto != null) {
            if (carDto.getAvailability().equals("Available")) {
                carStatusLabel.setText(carDto.getYear() + " " + carDto.getCarBrand() + " " + carDto.getCarModel());
                dailyRentalLabel.setText(String.valueOf(carDto.getDailyRental()));
                carErrorMsg = null;
            } else {
                carStatusLabel.setText("Car already rented");
                carErrorMsg = "Car already rented";
            }
        } else {
            carStatusLabel.setText("Invalid Car ID");
            carErrorMsg = "Invalid Car ID";
        }
    }

    public void loadList() throws IOException {
        AnchorPane formNode = FXMLLoader.load(this.getClass().getResource("/view/rent_list_view.fxml"));
        AnchorPane.setTopAnchor(formNode, 0.0);
        AnchorPane.setBottomAnchor(formNode, 0.0);
        AnchorPane.setLeftAnchor(formNode, 0.0);
        AnchorPane.setRightAnchor(formNode, 0.0);
        AnchorPane rootNode = (AnchorPane) this.formNode.getParent();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(formNode);
    }

    public void fromDateOnAction(ActionEvent actionEvent) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = sdf.parse(sdf.format(new Date()));
        System.out.println("Current date: " + currentDate);
        Date fromDate = DateUtil.asDate(this.fromDate.getValue());
        if(currentDate.before(fromDate) | currentDate.equals(fromDate)) {
            fromDateStatusLabel.setText("Valid Date");
        } else {
            fromDateStatusLabel.setText("Invalid Date");
        }

        if(this.fromDate != null && this.toDate != null && dailyRentalLabel != null) {
            LocalDate from = this.fromDate.getValue();
            LocalDate to = this.toDate.getValue();
            String days = String.valueOf(ChronoUnit.DAYS.between(from, to));
            totalLabel.setText(String.valueOf(Double.parseDouble(days) * Double.parseDouble(dailyRentalLabel.getText())));
        }
    }

    public void toDateOnAction(ActionEvent actionEvent) throws Exception{
        Date fromDate = DateUtil.asDate(this.fromDate.getValue());
        Date toDate = DateUtil.asDate(this.toDate.getValue());
        if(fromDate.before(toDate)) {
            toDateStatusLabel.setText("Valid Date");
        } else {
            toDateStatusLabel.setText("Invalid Date");
        }

        if(this.fromDate != null && this.toDate != null && dailyRentalLabel != null) {
            LocalDate from = this.fromDate.getValue();
            LocalDate to = this.toDate.getValue();
            String days = String.valueOf(ChronoUnit.DAYS.between(from, to));
            totalLabel.setText(String.valueOf(Double.parseDouble(days) * Double.parseDouble(dailyRentalLabel.getText())));
        }
    }

    public String generateId() {
        try {
            List<RentDto> rentDtos = rentService.getAllRents();
            RentDto rentDto = rentDtos.get(rentDtos.size() - 1);

            String prevId = rentDto.getRentId().substring(4);
            Integer incremetedId = Integer.parseInt(prevId) + 1;
            String id = "RENT" + String.format("%04d", incremetedId);
            return id;

        } catch (Exception e) {
            return "RENT0001";
        }
    }

    public String validateData() {
        Long days;
        try {
            LocalDate from = fromDate.getValue();
            LocalDate to = toDate.getValue();
            days = ChronoUnit.DAYS.between(from, to);
        } catch (Exception e) {
            days = 0L;
        }
        //System.out.println(days);

        Boolean isDepositValid;
        try {
            Double.parseDouble(depositField.getText());
            isDepositValid = true;
        } catch (Exception e) {
            if(depositField.getText() == ""){
                isDepositValid = true;
            } else {
                isDepositValid = false;
            }
        }

        Boolean isAdvanceValid;
        try {
            Double.parseDouble(advanceField.getText());
            isAdvanceValid = true;
        } catch (Exception e) {
            if(advanceField.getText() == ""){
                isAdvanceValid = true;
            } else {
                isAdvanceValid = false;
            }
        }

        if(customerIdField.getText() == "") {
            return "Customer ID not Entered";
        } else if (customerErrorMsg != null) {
            return customerErrorMsg;
        } else if (carIdField.getText() == "") {
            return "Car ID not Entered";
        } else if (carErrorMsg != null) {
            return carErrorMsg;
        } else if (fromDate.getValue() == null) {
            return "Rent From Date not entered";
        } else if (toDate.getValue() == null) {
            return "Rent To Date not entered";
        }else if (days > 30) {
            return "Rent duration exceeded. Maximum rent period is 30 days";
        } else if (!isDepositValid) {
            return "Invalid Refundable Deposit Entered";
        } else if (!isAdvanceValid) {
            return "Invalid Advance Payment Entered";
        } else {
            return null;
        }
    }
}
