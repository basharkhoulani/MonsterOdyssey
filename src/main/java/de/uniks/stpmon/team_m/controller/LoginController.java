package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.*;

public class LoginController extends Controller {

    @FXML
    public Label informationLabel;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public CheckBox rememberMeCheckbox;
    @FXML
    public Button signUpButton;
    @FXML
    public Button signInButton;
    @FXML
    public Button hideButton;
    @FXML
    public Label usernameErrorLabel;
    @FXML
    public Label passwordErrorLabel;
    @FXML
    public Label welcomeLabel;
    @FXML
    public Label gameNameLabel;

    private PasswordFieldSkin skin;

    private BooleanBinding isInvalidUsername;
    private BooleanBinding isInvalidPassword;


    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private String information = "";


    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;


    @Inject
    public LoginController() {
    }

    @Override
    public String getTitle() {
        return LOGIN_TITLE;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        skin = new PasswordFieldSkin(passwordField);
        passwordField.setSkin(skin);

        usernameField.textProperty().bindBidirectional(username);
        passwordField.textProperty().bindBidirectional(password);

        isInvalidUsername = username.isEmpty();
        isInvalidPassword = password.length().lessThan(PASSWORD_CHARACTER_LIMIT);
        signInButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));
        signUpButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));

        passwordField.setPromptText(PASSWORD_LESS_THAN_8_CHARACTERS);

        showInformation();

        return parent;
    }

    public void signIn() {
        if (isInvalidPassword.or(isInvalidUsername).get()) {
            return;
        }
        // TODO: test müssen auch ohne Serververbindung laufen. Wirkliche Funktionalität kommt später.

        app.show(mainMenuControllerProvider.get());
    }

    public void signUp() {
        // TODO: from UsersService

        app.show(mainMenuControllerProvider.get());
    }

    public void showPassword() {
        skin.setMask(skin.getNotMask());
        passwordField.setText(passwordField.getText());
    }

    public void showInformation() {
        informationLabel.setText(this.information);
    }

    public void setInformation(String information){
        this.information = information;
    }

}
