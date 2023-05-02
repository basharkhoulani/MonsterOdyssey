package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.PasswordFieldSkin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

public class AccountSettingController extends Controller{

    public Label informationLabel;
    public TextField usernameField;
    public Button usernameEditButton;
    public Button saveUsernameButton;
    public PasswordField passwordField;
    public Button showPasswordButton;
    public Button passwordEditButton;
    public Button savePasswordButton;
    public Button cancelButton;
    public Button deleteAccountButton;
    public Label passwordErrorLabel;
    public Label usernameErrorLabel;

    private PasswordFieldSkin skin;
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();

    private BooleanBinding isInvalidUsername;
    private BooleanBinding isInvalidPassword;

    private PasswordFieldSkin skin;
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    AccountSettingController(){
    }
    @Override
    public String getTitle() {
        return "Account Setting";
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

        isInvalidUsername = username.isEmpty();
        saveUsernameButton.disableProperty().bind(isInvalidUsername);

        isInvalidPassword = password.length().lessThan(8);
        savePasswordButton.disableProperty().bind(isInvalidPassword);

        usernameErrorLabel.textProperty().bind(Bindings.when(isInvalidUsername).then("Username must not be empty").otherwise(""));
        passwordErrorLabel.textProperty().bind(Bindings.when(isInvalidPassword).then("password must have at least 8 characters.").otherwise(""));


        return parent;
    }

    public void editUsername(){ usernameField.setDisable(false); }

    public void saveUsername(){
        usernameField.setDisable(true);
        //TODO functionally implement
    }

    public void showPassword(){
        skin.setMask(!skin.getMask());
        passwordField.setText(passwordField.getText());
    }

    public void editPassword(){ passwordField.setDisable(false);}

    public void savePassword(){
        passwordField.setDisable(true);
        //TODO functionally implement
    }

    public void deleteAccount(){ }

    public void cancel(){ app.show(mainMenuControllerProvider.get()); }



}
