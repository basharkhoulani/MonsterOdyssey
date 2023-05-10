package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;


public class AccountSettingController extends Controller {

    @FXML
    public Label informationLabel;
    @FXML
    public TextField usernameField;
    @FXML
    public Button usernameEditButton;
    @FXML
    public Button saveUsernameButton;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button showPasswordButton;
    @FXML
    public Button passwordEditButton;
    @FXML
    public Button savePasswordButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Button deleteAccountButton;
    @FXML
    public Label passwordErrorLabel;
    @FXML
    public Label usernameErrorLabel;
    @FXML
    public Label titleLabel;

    private PasswordFieldSkin skin;
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<LoginController> loginControllerProvider;

    @Inject
    AccountSettingController() {
    }

    @Override
    public String getTitle() {
        return ACCOUNT_SETTINGS_TITLE;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        // Firstly disable the editfield
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        showPasswordButton.setDisable(true);

        // Secondly show password
        skin = new PasswordFieldSkin(passwordField);
        passwordField.setSkin(skin);

        //Thirdly bind the username and password
        usernameField.textProperty().bindBidirectional(username);
        passwordField.textProperty().bindBidirectional(password);

        BooleanBinding isInvalidUsername = username.isEmpty();
        saveUsernameButton.disableProperty().bind(isInvalidUsername);

        BooleanBinding isInvalidPassword = password.length().lessThan(PASSWORD_CHARACTER_LIMIT);
        savePasswordButton.disableProperty().bind(isInvalidPassword);

        passwordField.setPromptText(PASSWORD_LESS_THAN_8_CHARACTERS);

        return parent;
    }

    public void editUsername() {
        usernameField.setDisable(false);
    }

    public void saveUsername() {
        usernameField.setDisable(true);

        //TODO functionally implement

        //username has been changed successfully
        usernameErrorLabel.setText("");
        informationLabel.setText("Your username has been changed successfully.");
    }

    public void showPassword() {
        skin.setMask(skin.getNotMask());
        passwordField.setText(passwordField.getText());
    }


    public void editPassword(){
        passwordField.setDisable(false);
        showPasswordButton.setDisable(false);
    }

    public void savePassword() {
        passwordField.setDisable(true);
        showPasswordButton.setDisable(true);
        //TODO functionally implement

        //password has been changed successfully
        passwordErrorLabel.setText("");
        informationLabel.setText("Your Password has been changed successfully.");
    }

    public void deleteAccount(){
        LoginController loginController = loginControllerProvider.get();
        loginController.setInformation("Account successfully deleted");
        app.show(loginController);
    }

    public void cancel(){ app.show(mainMenuControllerProvider.get()); }

    public void showDeletePopUp() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.initOwner(app.getStage());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            deleteAccount();
        }
    }
}
