package lk.ijse.carhire.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carhire.dto.UserDto;
import lk.ijse.carhire.service.ServiceFactory;
import lk.ijse.carhire.service.custom.UserService;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFormController {

    @FXML
    private Button loginBtn;

    @FXML
    private Label loginLabel;

    @FXML
    private PasswordField pwdField;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField usernameField;

    private UserService userService;

    private static UserDto userDto;
    private static Stage stage;

    public LoginFormController() {
        userService = (UserService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.USER);
    }
    @FXML
    void loginBtnOnAction(ActionEvent event) {
        String inputUsername = usernameField.getText();
        String inputPassword = pwdField.getText();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputPassword.getBytes());
            byte [] digest = md.digest();
            String hashPassword = DatatypeConverter.printHexBinary(digest).toLowerCase();

            userDto = userService.getUser(inputUsername);

            if(userDto != null) {
                if(hashPassword.equals(userDto.getPassword())) {
                    stage = (Stage) this.rootNode.getScene().getWindow();
                    stage.close();

                    stage = new Stage();

                    Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/layout_view.fxml"));

                    Scene scene = new Scene(rootNode);
                    stage.setScene(scene);
                    stage.setTitle("CarHire");
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Incorrect credentials").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Incorrect credentials").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void pwdFieldOnAction(ActionEvent event) {
        loginBtnOnAction(event);
    }

    @FXML
    void usernameFieldOnAction(ActionEvent event) {
        pwdField.requestFocus();
    }

    public static String passUser() {
        return userDto.getUsername();
    }

    public static Stage passStage() {return stage;}
}
