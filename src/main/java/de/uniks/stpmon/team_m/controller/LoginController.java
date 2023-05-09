package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.TokenStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
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


    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleBooleanProperty rememberMe = new SimpleBooleanProperty();
    private String information = EMPTY_STRING;


    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    AuthenticationService authenticationService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    UserStorage user;
    @Inject
    UsersService usersService;


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
        rememberMeCheckbox.selectedProperty().bindBidirectional(rememberMe);

        isInvalidUsername = username.isEmpty();
        isInvalidPassword = password.length().lessThan(PASSWORD_CHARACTER_LIMIT);
        signInButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));
        signUpButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));

        passwordField.setPromptText(PASSWORD_LESS_THAN_8_CHARACTERS);

        showInformation();

        return parent;
    }

    public void signIn() {
        passwordErrorLabel.setText(EMPTY_STRING);

        if (isInvalidPassword.or(isInvalidUsername).get()) { return; }

        disposables.add(authenticationService
                .login(username.get(), password.get(), rememberMe.get())
                .observeOn(FX_SCHEDULER)
                .subscribe(loginResult -> {
                    app.show(mainMenuControllerProvider.get());
                    }, error -> {
                    passwordErrorLabel.setText(error.getMessage());
            }));
    }

    public void signUp() {
        passwordErrorLabel.setText(EMPTY_STRING);

        if(isInvalidPassword.or(isInvalidUsername).get()){ return; }

        disposables.add(usersService
                .createUser(username.get(), null, password.get())
                .observeOn(FX_SCHEDULER)
                .subscribe(userResult -> {
                    signIn();
                    }, error ->{
                    passwordErrorLabel.setText(error.getMessage());
                }));
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
