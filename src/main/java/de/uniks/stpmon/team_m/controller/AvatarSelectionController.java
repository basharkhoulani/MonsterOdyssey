package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.inject.Inject;
import java.io.File;
import static de.uniks.stpmon.team_m.Constants.IMAGE;
import static de.uniks.stpmon.team_m.Constants.SELECT_AVATAR_PICTURE;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

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

    public String selectedAvatar;

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

/*
        //HTTP 400 Validation Error
        //uri wird nicht vom server erkannt
        try {
            avatar1 = convertToURI("src/main/resources/de/uniks/stpmon/team_m/images/cat.jpeg");
            avatar2 = convertToURI("src/main/resources/de/uniks/stpmon/team_m/images/monster.png");
            avatar3 = convertToURI("src/main/resources/de/uniks/stpmon/team_m/images/rabbit.png");
            avatar4 = convertToURI("src/main/resources/de/uniks/stpmon/team_m/images/sheep_icon.png");
            System.out.println(avatar1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
 */

        avatar1ImageView.setImage(new Image(avatar1));
        avatar2ImageView.setImage(new Image(avatar2));
        avatar3ImageView.setImage(new Image(avatar3));
        avatar4ImageView.setImage(new Image(avatar4));

        return parent;
    }

    public String convertToURI(String filePath) throws IOException {
        File file = new File(filePath);
        String contentType = Files.probeContentType(file.toPath());
        byte[] data = Files.readAllBytes(file.toPath());
        String base64str = Base64.getEncoder().encodeToString(data);
        return "data:" + contentType + ";base64," + base64str;
    }

    public void selectAvatar1() {
        selectedAvatar = avatar1;
        uploadTextField.clear();
    }

    public void selectAvatar2() {
        selectedAvatar = avatar2;
        uploadTextField.clear();
    }

    public void selectAvatar3() {
        selectedAvatar = avatar3;
        uploadTextField.clear();
    }

    public void selectAvatar4() {
        selectedAvatar = avatar4;
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
        }
    }
}
