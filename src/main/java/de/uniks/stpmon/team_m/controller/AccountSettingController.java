package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

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
        // TODO functionally implement
    }

    public void showPassword() {
        skin.setMask(skin.getMask());
        passwordField.setText(passwordField.getText());
    }

    public void editPassword() {
        passwordField.setDisable(false);
    }

    public void savePassword() {
        passwordField.setDisable(true);
        // TODO functionally implement
    }

    public void deleteAccount() {
    }

    public void cancel() {
        app.show(mainMenuControllerProvider.get());
    }


}
