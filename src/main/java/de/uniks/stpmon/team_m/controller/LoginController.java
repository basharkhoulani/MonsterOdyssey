package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.controller.subController.ChangeLanguageController;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.PasswordFieldSkin;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

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
    public Label gameNameLabel1;
    @FXML
    public Label gameNameLabel2;
    @FXML
    public ImageView gameIcon;
    @FXML
    public Button languageSettings;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<UserStorage> userStorage;
    @Inject
    AuthenticationService authenticationService;
    @Inject
    UsersService usersService;
    private PasswordFieldSkin skin;
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleBooleanProperty rememberMe = new SimpleBooleanProperty();
    private String information;

    private ChangeLanguageController changeLanguageController;


    /**
     * LoginController is used to show the login screen and to login or signup the user.
     */

    @Inject
    public LoginController() {
    }

    /**
     * This method is used to set the title of the login screen.
     */
    @Override
    public String getTitle() {
        return resources.getString("LOGIN.TITLE");
    }

    @Override
    public void init() {
        super.init();
        changeLanguageController = new ChangeLanguageController();
        changeLanguageController.init();
    }

    /**
     * This method is used to render JavaFX elements of the login screen.
     *
     * @return Parent object
     */

    @Override
    public Parent render() {
        final Parent parent = super.render();

        skin = new PasswordFieldSkin(passwordField);
        passwordField.setSkin(skin);

        usernameField.textProperty().bindBidirectional(username);
        passwordField.textProperty().bindBidirectional(password);
        rememberMeCheckbox.selectedProperty().bindBidirectional(rememberMe);

        BooleanBinding isInvalidUsername = username.isEmpty();
        BooleanBinding isInvalidPassword = password.length().lessThan(PASSWORD_CHARACTER_LIMIT);
        signInButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));
        signUpButton.disableProperty().bind(isInvalidPassword.or(isInvalidUsername));

        passwordField.setPromptText(resources.getString("PASSWORD.LESS.THAN.8.CHARACTERS"));

        passwordField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                signIn();
            }
        });

        showInformation();

        return parent;
    }

    /**
     * This method is used to log in the user and update the status to online.
     * It also shows the main menu screen. It handles the error if the user is not found.
     */

    public void signIn() {
        passwordErrorLabel.setText(EMPTY_STRING);
        disposables.add(authenticationService.login(username.get(), password.get(), rememberMe.get())
                .observeOn(FX_SCHEDULER).subscribe(loginResult -> {
                    userStatusUpdate(USER_STATUS_ONLINE);
                    app.show(mainMenuControllerProvider.get());
                }, error -> errorHandle(error.getMessage())));
    }

    /**
     * This method is used to sign up the user and show the main menu screen.
     * It handles the error if the username is already taken.
     */

    public void signUp() {
        passwordErrorLabel.setText(EMPTY_STRING);
        disposables.add(usersService.createUser(username.get(), null, password.get())
                .observeOn(FX_SCHEDULER).subscribe(userResult -> signIn(), error -> errorHandle(error.getMessage())));
    }

    /**
     * This method is used to show the password of the user in the PasswordField.
     */

    public void showPassword() {
        skin.setMask(skin.getNotMask());
        passwordField.setText(passwordField.getText());
    }

    /**
     * This method is used to show a message when the user has been deleted from AccountSettingController
     */

    public void showInformation() {
        informationLabel.setText(this.information);
    }

    /**
     * This method is used to set the information if the user has been deleted from AccountSettingController
     *
     * @param information String, the success message
     */

    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * This method is used to handle the error messages.
     *
     * @param error String, the error message
     */

    public void errorHandle(String error) {
        if (error.contains(HTTP_401)) {
            passwordErrorLabel.setText(resources.getString("SIGNIN.ERROR"));
        } else if (error.contains(HTTP_409)) {
            passwordErrorLabel.setText(resources.getString("USERNAME.TAKEN"));
        } else {
            passwordErrorLabel.setText(resources.getString("CUSTOM.ERROR"));
        }
    }

    /**
     * This method is used to update the status of the user.
     *
     * @param status String, the status of the user
     */

    public void userStatusUpdate(String status) {
        if (userStorage.get().get_id() != null) {
            disposables.add(usersService.updateUser(null, status, null, null, null).observeOn(FX_SCHEDULER)
                    .subscribe(user -> userStorage.get().setStatus(user.status()), error -> errorHandle(error.getMessage())));
        }
    }

    /**
     * This method is used to open the Change Language Pop up
     */
    public void changeLanguage() {
        Dialog<?> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        dialog.setTitle(resources.getString("CHOOSE.LANGUAGE"));
        changeLanguageController.setValues(resources, preferences, resourceBundleProvider, this, app);
        dialog.getDialogPane().setContent(changeLanguageController.render());
        dialog.showAndWait();
    }
}
