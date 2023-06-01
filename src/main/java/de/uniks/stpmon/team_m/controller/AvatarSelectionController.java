package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.inject.Inject;
import java.io.File;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;

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

    // the avatar selected with the radiobutton
    public String selectedAvatar;

    // default avatars
    public String avatar1 = "https://gravatar.com/avatar/85f983f7459c2d26000ea0df68c74ede?s=400&d=robohash&r=x";
    public String avatar2 = "https://robohash.org/2e1549952236b1e98fc61f3985d7ffd6?set=set4&bgset=bg2&size=400x400";
    public String avatar3 = "https://gravatar.com/avatar/d457438462edd31dd0208e8ef1044f9e?s=400&d=identicon&r=x";
    public String avatar4 = "https://gravatar.com/avatar/0d0954aa7a5e3932e9669699c76d2ada?s=400&d=monsterid&r=x";

    @Inject
    public AvatarSelectionController() {
        super();
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        // show images
        avatar1ImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(AVATAR_1)).toString()));
        avatar2ImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(AVATAR_2)).toString()));
        avatar3ImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(AVATAR_3)).toString()));
        avatar4ImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(AVATAR_4)).toString()));

        return parent;
    }

    /**
     * This method converts images to URI.
     */
    public String convertToURI(String filePath) throws IOException {
        File file = new File(filePath);
        String contentType = Files.probeContentType(file.toPath());
        byte[] data = Files.readAllBytes(file.toPath());
        String base64str = Base64.getEncoder().encodeToString(data);
        return "data:" + contentType + ";base64," + base64str;
    }

    /**
     * This method selects default avatar 1.
     */
    public void selectAvatar1() {
        selectedAvatar = AVATAR_1;
        uploadTextField.clear();
    }

    /**
     * This method selects default avatar 2.
     */
    public void selectAvatar2() {
        selectedAvatar = AVATAR_2;
        uploadTextField.clear();
    }

    /**
     * This method selects default avatar 3.
     */
    public void selectAvatar3() {
        selectedAvatar = AVATAR_3;
        uploadTextField.clear();
    }

    /**
     * This method selects default avatar 4.
     */
    public void selectAvatar4() {
        selectedAvatar = AVATAR_4;
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
        FileChooser fileChooser = new FileChooser();
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
            selectedAvatar = "file:" + selectedFile.getAbsolutePath();
        }
    }
}
