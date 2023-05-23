package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import de.uniks.stpmon.team_m.utils.UserStorage;
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
    UserStorage user;
    @Inject
    UsersService usersService;

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

        // Show the Current UserName
        usernameField.setPromptText(user.getName());

        // show password
        skin = new PasswordFieldSkin(passwordField);
        passwordField.setSkin(skin);

        // bind the username and password
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
        usernameField.setDisable(!usernameField.isDisabled());
    }

    public void saveUsername() {
        informationLabel.setText(EMPTY_STRING);
        usernameErrorLabel.setText(EMPTY_STRING);

        disposables.add(usersService
                .updateUser(username.get(), null, null, null, null)
                .observeOn(FX_SCHEDULER)
                .subscribe(userResult -> {
                    user.setName(userResult.name());
                    informationLabel.setText(USERNAME_SUCCESS_CHANGED);
                    usernameField.setDisable(true);
                    usernameField.setText(EMPTY_STRING);
                    usernameField.setPromptText(user.getName());
                }, error -> usernameErrorLabel.setText(errorHandle(error.getMessage()))));

    }

    public void showPassword() {
        skin.setMask(skin.getNotMask());
        passwordField.setText(passwordField.getText());
    }


    public void editPassword() {
        passwordField.setDisable(!passwordField.isDisabled());
        showPasswordButton.setDisable(!showPasswordButton.isDisabled());
    }

    public void savePassword() {
        informationLabel.setText(EMPTY_STRING);
        passwordErrorLabel.setText(EMPTY_STRING);

        disposables.add(usersService
                .updateUser(null, null, null, null, password.get())
                .observeOn(FX_SCHEDULER)
                .subscribe(userResult -> {
                    passwordField.setText(EMPTY_STRING);
                    passwordField.setPromptText(PASSWORD_LESS_THAN_8_CHARACTERS);
                    passwordField.setDisable(true);
                    showPasswordButton.setDisable(true);
                    informationLabel.setText(PASSWORD_SUCCESS_CHANGED);
                }, error -> passwordErrorLabel.setText(errorHandle(error.getMessage()))));
    }

    public void deleteAccount(Alert alert) {
        disposables.add(usersService.deleteUser()
                .observeOn(FX_SCHEDULER)
                .subscribe(delete -> {
                    LoginController loginController = loginControllerProvider.get();
                    loginController.setInformation(DELETE_SUCCESS);
                    app.show(loginController);
                }, error -> errorAlert(alert)));
    }

    public void cancel() {
        app.show(mainMenuControllerProvider.get());
    }

    public void showDeletePopUp() {
        Alert alert = new Alert(Alert.AlertType.WARNING, SURE, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.initOwner(app.getStage());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAccount(alert);
        }
    }

    private void errorAlert(Alert alert) {
        alert.setContentText(CUSTOM_ERROR);
        alert.setTitle(ERROR);
        alert.getButtonTypes().remove(ButtonType.CANCEL);
        alert.showAndWait();
    }

    public String errorHandle(String error){
        if(error.contains(HTTP_409)){
            return USERNAME_TAKEN;
        } else {
            return CUSTOM_ERROR;
        }
    }
}
