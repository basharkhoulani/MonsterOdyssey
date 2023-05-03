package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.TokenStorage;
import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.LOGIN_TITLE;

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
    public Button hideButton;
    public Label usernameErrorLabel;
    public Label passwordErrorLabel;

    @Inject
    AuthenticationService authenticationService;
    @Inject
    TokenStorage tokenStorage;

    private PasswordFieldSkin skin;

    private BooleanBinding isInvalidUsername;
    private BooleanBinding isInvalidPassword;

    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();

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
        isInvalidPassword = password.length().lessThan(8);
        signInButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));
        signUpButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));

        passwordField.setPromptText("Password must have at least 8 character.");

        return parent;
    }


    public void signIn() {
        if (isInvalidPassword.or(isInvalidUsername).get()){
            return;
        }

        /* disposables.add(authenticationService
                .login(username.get(), password.get())
                .observeOn(FX_SCHEDULER)
                .subscribe(lr -> {
                //app.show(mainMenuControllerProvider.get());
                //TODO: test müssen auch ohne Serververbindung laufen. Wirkliche Funktionalität kommt später.
                }, error ->{
                    //passwordErrorLabel.setText(error.getMessage());
                })); */

        app.show(mainMenuControllerProvider.get());
    }

    public void signUp() {
        // TODO: from UsersService

        app.show(mainMenuControllerProvider.get());
    }

    public void showPassword() {
        skin.setMask(!skin.getMask());
        passwordField.setText(passwordField.getText());
    }

}
