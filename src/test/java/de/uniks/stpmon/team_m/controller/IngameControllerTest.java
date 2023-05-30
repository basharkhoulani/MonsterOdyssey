package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

@ExtendWith(MockitoExtension.class)
public class IngameControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;

    @InjectMocks
    IngameController ingameController;

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void showHelp() {

    }

    @Test
    void pauseGame() {
    }
}
