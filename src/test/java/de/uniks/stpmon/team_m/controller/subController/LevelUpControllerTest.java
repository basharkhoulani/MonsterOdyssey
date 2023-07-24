package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LevelUpControllerTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @InjectMocks
    LevelUpController levelUpController;
    @Mock
    Provider<EncounterController> encounterControllerProvider;


    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        levelUpController.setValues(bundle, null, null, levelUpController, app);
        final EncounterController encounterController = mock(EncounterController.class);
        //when(encounterControllerProvider.get()).thenReturn(encounterController);
        //doNothing().when(app).show(encounterController);
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        Monster monster = new Monster("2023-06-05T17:02:40.357Z",
                "023-06-05T17:02:40.357Z",
                "647e1530866ace3595866db2",
                "647e15308c1bb6a91fb57321",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(14, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5),
                List.of());
        MonsterTypeDto monsterTypeDto = new MonsterTypeDto(
                1,
                "Flamander",
                "Flamander_1.png",
                List.of("fire"),
                "Flamander is a small, agile monster that lives in the hot deserts of the world.");

        levelUpController.init(
                new VBox(),
                new StackPane(),
                encounterController,
                monster,
                monsterTypeDto,
                monster,
                new ArrayList<Integer>());
        app.start(stage);
        app.show(levelUpController);
        stage.requestFocus();
    }


    @Test
    public void levelUpPopUpTest() {

        final Button okButton = lookup("#okButton").query();
        clickOn(okButton);
    }


}
