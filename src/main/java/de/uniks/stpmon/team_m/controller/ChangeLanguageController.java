package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;


import static de.uniks.stpmon.team_m.Constants.CHANGE_LANGUAGE_TITLE;

public class ChangeLanguageController extends Controller{
    @FXML
    public Label changeLanguageLabel;

    @FXML
    public RadioButton radioButtonLanguageEnglish;

    @FXML
    public RadioButton radioButtonLanguageGerman;

    @FXML
    public RadioButton radioButtonLanguageChinese;

    @FXML
    public Button applyLanguageButton;

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initRadioButtons();
        return parent;
    }

    @Inject
    Provider<AccountSettingController> accountSettingControllerProvider;

    @Inject
    Provider<LoginController> loginControllerProvider;

    @Inject
    public ChangeLanguageController(){

    }

    @Override
    public String getTitle() {
        return CHANGE_LANGUAGE_TITLE;
    }

    private void initRadioButtons() {
        ToggleGroup languageToggleGroup = new ToggleGroup();
        radioButtonLanguageEnglish.setToggleGroup(languageToggleGroup);
        radioButtonLanguageEnglish.setSelected(true);
        radioButtonLanguageGerman.setToggleGroup(languageToggleGroup);
        radioButtonLanguageChinese.setToggleGroup(languageToggleGroup);
    }

    public void applyLanguage(){
        Stage stage = (Stage) applyLanguageButton.getScene().getWindow();
        stage.close();
    }



    public void setLanguageEnglish() {

    }

    public void setLanguageGerman() {

    }

    public void setLanguageChinese() {

    }
}

