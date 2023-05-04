package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.ACCOUNT_SETTINGS_TITLE;

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

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<LoginController> loginControllerProvider;

    @Inject
    AccountSettingController(){
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

        isInvalidUsername = username.isEmpty();
        saveUsernameButton.disableProperty().bind(isInvalidUsername);

        isInvalidPassword = password.length().lessThan(8);
        savePasswordButton.disableProperty().bind(isInvalidPassword);

        passwordField.setPromptText("Password must have at least 8 character.");

        return parent;
    }

    public void editUsername(){ usernameField.setDisable(false); }

    public void saveUsername(){
        usernameField.setDisable(true);
        //TODO functionally implement

        //username has been changed successfully
        usernameErrorLabel.setText("");
        informationLabel.setText("Your username has been changed successfully.");
    }

    public void showPassword(){
        skin.setMask(!skin.getMask());
        passwordField.setText(passwordField.getText());
    }

    public void editPassword(){
        passwordField.setDisable(false);
        showPasswordButton.setDisable(false);
    }

    public void savePassword(){
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
        System.out.println("Your Account is deleted successfully.");
    }

    public void cancel(){ app.show(mainMenuControllerProvider.get()); }


    public void showDeletePopUp() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.initOwner(app.getStage());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            deleteAccount();
        }
    }
}
