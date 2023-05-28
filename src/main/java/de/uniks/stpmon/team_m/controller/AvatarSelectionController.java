package de.uniks.stpmon.team_m.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.inject.Inject;

import static de.uniks.stpmon.team_m.Constants.AVATAR_TITLE;

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
    public AvatarSelectionController() {
        super();
    }
    @Override
    public Parent render() {
        return super.render();
    }

    public void selectAvatar1() {

    }

    public void selectAvatar2() {

    }

    public void selectAvatar3() {

    }

    public void selectAvatar4() {

    }

    public void uploadAvatarPicture() {

    }

    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(AVATAR_TITLE);
        java.io.File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            uploadTextField.setText(selectedFile.getAbsolutePath());
        }
    }
}
