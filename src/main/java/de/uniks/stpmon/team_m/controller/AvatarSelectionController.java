package de.uniks.stpmon.team_m.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.inject.Inject;
import java.io.File;

import static de.uniks.stpmon.team_m.Constants.IMAGE;
import static de.uniks.stpmon.team_m.Constants.SELECT_AVATAR_PICTURE;

public class AvatarSelectionController extends Controller {

    @FXML
    public Label uploadErrorLabel;
    @FXML
    public ImageView avatar1ImageView;
    @FXML
    public RadioButton avatar1RadioButton;
    @FXML
    public ImageView avatar2ImageView;
    @FXML
    public RadioButton avatar2RadioButton;
    @FXML
    public ImageView avatar3ImageView;
    @FXML
    public RadioButton avatar3RadioButton;
    @FXML
    public ImageView avatar4ImageView;
    @FXML
    public RadioButton avatar4RadioButton;
    @FXML
    public ToggleGroup selectAvatar;
    @FXML
    public TextField uploadTextField;
    @FXML
    public Button selectFileButton;
    @Inject
    FileChooser fileChooser;


    @Inject
    public AvatarSelectionController() {
        super();
    }

    @Override
    public Parent render() {
        return super.render();
    }

    public void selectAvatar1() {
        uploadTextField.clear();
    }

    public void selectAvatar2() {
        uploadTextField.clear();
    }

    public void selectAvatar3() {
        uploadTextField.clear();
    }

    public void selectAvatar4() {
        uploadTextField.clear();
    }

    public void clickTextField() {
        for (Toggle radioButton : selectAvatar.getToggles()) {
            if (radioButton.isSelected()) {
                radioButton.setSelected(false);
            }
        }
    }

    public void selectFile() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(IMAGE, "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle(SELECT_AVATAR_PICTURE);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            for (Toggle radioButton : selectAvatar.getToggles()) {
                if (radioButton.isSelected()) {
                    radioButton.setSelected(false);
                }
            }
            uploadTextField.setText(selectedFile.getAbsolutePath());
        }
    }
}
