package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.AppTest;
import de.uniks.stpmon.team_m.controller.subController.AvatarSelectionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AvatarSelectionControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);
    @Mock
    Provider<FileChooser> fileChooserProvider;
    @InjectMocks
    AvatarSelectionController avatarSelectionController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        avatarSelectionController.setValues(bundle, null, null, avatarSelectionController, app);
        app.start(stage);
        app.show(avatarSelectionController);
        stage.requestFocus();
    }


    @Test
    void selectFileTest() {
        FileChooser fileChooser = mock(FileChooser.class);
        when(fileChooserProvider.get()).thenReturn(fileChooser);
        RadioButton avatar1RadioButton = lookup("#avatar1RadioButton").query();
        avatar1RadioButton.setSelected(true);
        ObservableList<FileChooser.ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
        File chooserFile = new File(Objects.requireNonNull(AppTest.class.getResource("images/Monster.png")).getPath());
        when(fileChooser.getExtensionFilters()).thenReturn(extensionFilters);
        when(fileChooser.showOpenDialog(null)).thenReturn(chooserFile);
        clickOn("#selectFileButton");
        ToggleGroup avatarToggleGroup = avatar1RadioButton.getToggleGroup();
        for (Toggle toggle : avatarToggleGroup.getToggles()) {
            assertFalse(toggle.isSelected());
        }
        TextField uploadTextField = lookup("#uploadTextField").query();
        assertEquals(chooserFile.getName(), uploadTextField.getText());
    }

    @Test
    void selectedAvatarTest() {
        final RadioButton avatar1RadioButton = lookup("#avatar1RadioButton").query();
        final RadioButton avatar2RadioButton = lookup("#avatar2RadioButton").query();
        final RadioButton avatar3RadioButton = lookup("#avatar3RadioButton").query();
        final RadioButton avatar4RadioButton = lookup("#avatar4RadioButton").query();
        clickOn(avatar1RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_1);
        clickOn(avatar2RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_2);
        clickOn(avatar3RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_3);
        clickOn(avatar4RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_4);
        clickOn("#avatar1ImageView");
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_1);
        clickOn("#avatar2ImageView");
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_2);
        clickOn("#avatar3ImageView");
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_3);
        clickOn("#avatar4ImageView");
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_4);
    }
}
