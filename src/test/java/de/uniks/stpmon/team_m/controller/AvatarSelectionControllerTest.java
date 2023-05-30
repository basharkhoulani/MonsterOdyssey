package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.AppTest;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals(chooserFile.getAbsolutePath(), uploadTextField.getText());
    }

    @Test
    void selectAvatarTest() {
        RadioButton avatar1RadioButton = lookup("#avatar1RadioButton").query();
        TextField uploadTextField = lookup("#uploadTextField").query();
        RadioButton avatar2RadioButton = lookup("#avatar2RadioButton").query();
        RadioButton avatar3RadioButton = lookup("#avatar3RadioButton").query();
        RadioButton avatar4RadioButton = lookup("#avatar4RadioButton").query();
        clickOn(uploadTextField);
        write("/pictures/avatar.png");
        clickOn(avatar1RadioButton);
        assertTrue(avatar1RadioButton.isSelected());
        assertTrue(uploadTextField.getText().isEmpty());
        clickOn(avatar2RadioButton);
        assertTrue(avatar2RadioButton.isSelected());
        assertFalse(avatar1RadioButton.isSelected());
        clickOn(avatar3RadioButton);
        assertTrue(avatar3RadioButton.isSelected());
        assertFalse(avatar2RadioButton.isSelected());
        clickOn(avatar4RadioButton);
        assertTrue(avatar4RadioButton.isSelected());
        assertFalse(avatar3RadioButton.isSelected());
        clickOn(uploadTextField);
        assertFalse(avatar4RadioButton.isSelected());
    }
}
