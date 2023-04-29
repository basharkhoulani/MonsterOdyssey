package de.uniks.stpmon.team_m.controller;

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
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        return parent;
    }

    public void editUsername(){ }

    public void saveUsername(){ }

    public void showPassword(){ }

    public void editPassword(){ }

    public void savePassword(){ }

    public void deleteAccount(){ }

    public void cancel(){ app.show(mainMenuControllerProvider.get()); }



}
