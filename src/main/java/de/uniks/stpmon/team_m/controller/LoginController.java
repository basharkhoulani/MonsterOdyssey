package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.controller.subController.PasswordFieldSkin;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;

public class LoginController extends Controller {

    @FXML
    public Label informationLabel;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public CheckBox rememberMeCheckbox;
    @FXML
    public Button signUpButton;
    @FXML
    public Button signInButton;
    public Button hideButton;

    private PasswordFieldSkin skin;

    private BooleanBinding isInvalid;
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;


    @Inject
    public LoginController() {
    }

    @Override
    public String getTitle() {
        return "Sign Up & In";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        skin = new PasswordFieldSkin(passwordField);
        passwordField.setSkin(skin);

        usernameField.textProperty().bindBidirectional(username);
        passwordField.textProperty().bindBidirectional(password);

        isInvalid = username.isEmpty().or(password.length().lessThan(8));
        signInButton.disableProperty().bind(isInvalid);
        signUpButton.disableProperty().bind(isInvalid);

        return parent;
    }


    public void signIn() {
        app.show(mainMenuControllerProvider.get());
    }

    public void signUp() {
        app.show(mainMenuControllerProvider.get());
    }

    public void showPassword(ActionEvent mouseEvent) {
        skin.setMask(!skin.getMask());
        passwordField.setText(passwordField.getText());

    }

}
