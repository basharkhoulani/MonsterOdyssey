package de.uniks.stpmon.team_m.controller;


import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class LoginController extends Controller{


    @FXML public Label informationLabel;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public ImageView hideImage;
    @FXML public Label errorLabel;
    @FXML public CheckBox rememberMeCheckbox;
    @FXML public Button signUpButton;
    @FXML public Button signInButton;

    @Inject
    public LoginController(){
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        return parent;
    }

    public void signIn(){

    }

    public void signUp(){

    }

}
