package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;


public class ChangeLanguageController extends Controller {
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
    public ChangeLanguageController() {

    }

    @Override
    public String getTitle() {
        return resources.getString("CHANGE.LANGUAGE.TITLE");
    }

    private void initRadioButtons() {
        ToggleGroup languageToggleGroup = new ToggleGroup();
        radioButtonLanguageEnglish.setToggleGroup(languageToggleGroup);
        radioButtonLanguageGerman.setToggleGroup(languageToggleGroup);
        radioButtonLanguageChinese.setToggleGroup(languageToggleGroup);
        Locale locale = resources.getLocale();
        switch (locale.toLanguageTag()) {
            case "de":
                radioButtonLanguageGerman.setSelected(true);
                break;
            case "en":
                radioButtonLanguageEnglish.setSelected(true);
                break;
            case "zh":
                radioButtonLanguageChinese.setSelected(true);
                break;
        }
    }

    public void applyLanguage() {
        if (radioButtonLanguageEnglish.isSelected()) {
            setLanguageEnglish();
        } else if (radioButtonLanguageGerman.isSelected()) {
            setLanguageGerman();
        } else if (radioButtonLanguageChinese.isSelected()) {
            setLanguageChinese();
        }
        Stage stage = (Stage) applyLanguageButton.getScene().getWindow();
        stage.close();
    }

    public void setLanguageEnglish() {
        setLanguage(Locale.ENGLISH);
    }

    public void setLanguageGerman() {
        setLanguage(Locale.GERMAN);
    }

    public void setLanguageChinese() {
        setLanguage(Locale.SIMPLIFIED_CHINESE);
    }


    private void setLanguage(Locale locale) {
        preferences.put("locale", locale.toLanguageTag());
        toReload.reload(toReload);
    }
}

