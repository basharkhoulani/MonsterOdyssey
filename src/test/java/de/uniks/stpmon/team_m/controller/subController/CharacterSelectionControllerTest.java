package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.WelcomeSceneController;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterSelectionControllerTest extends ApplicationTest {
    @InjectMocks
    CharacterSelectionController characterSelectionController;
    @Mock
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Spy
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        characterSelectionController.setValues(bundle, null, null, characterSelectionController, app);
        final WelcomeSceneController welcomeSceneController = mock(WelcomeSceneController.class);
        when(welcomeSceneControllerProvider.get()).thenReturn(welcomeSceneController);
        doNothing().when(app).show(welcomeSceneController);

        final TrainerStorage trainerStorage = mock(TrainerStorage.class);
        when(trainerStorageProvider.get()).thenReturn(trainerStorage);

        app.start(stage);
        app.show(characterSelectionController);
        stage.requestFocus();
    }

    @Test
    void selectCharacterTest() {
        clickOn("Previous");
        verify(app).show(welcomeSceneControllerProvider.get());
        clickOn("Next");

        ImageView arrowLeft = lookup("#arrowLeft").query();
        ImageView arrowRight = lookup("#arrowRight").query();

        clickOn(arrowLeft);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);
        clickOn(arrowRight);

        clickOn("Next");
    }

}
