package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;

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
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public LoginController(){
    }

    @Override
    public String getTitle(){
        return "Sign Up & In";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        Image hideEye = new Image(getClass().getResource("../views/hideEye.png").toString());
        hideImage.setImage(hideEye);


        return parent;
    }



    public void signIn(){
        app.show(mainMenuControllerProvider.get());
    }

    public void signUp(){
        app.show(mainMenuControllerProvider.get());
    }

}
