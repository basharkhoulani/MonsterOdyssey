package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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

import static org.junit.jupiter.api.Assertions.*;

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
    PresetsService presetsService;


    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        levelUpController.setValues(bundle, null, null, levelUpController, app);


        when(presetsService.getMonster(anyInt())).thenReturn(Observable.just(
                new MonsterTypeDto(
                        1,
                        "Salamander",
                        "salamander.png",
                        List.of("fire"),
                        "A fire lizard. It's hot."
                )
        ));

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
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new VBox(), new HBox());
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(vBox);
        levelUpController.init(
                vBox,
                stackPane,
                monster,
                monsterTypeDto,
                monster,
                new ArrayList<>(),
                new ArrayList<>(),
                true);
        app.start(stage);
        app.show(levelUpController);
        stage.requestFocus();
    }


    @Test
    public void levelUpPopUpTest() {
        final Label level = lookup("#levelLabel").query();
        assertEquals(level.getText(), "1 -> 1");
        final Label health = lookup("#healthLabel").query();
        assertEquals(health.getText(), "14.0 -> 14.0");
        final Label attack = lookup("#attackLabel").query();
        assertEquals(attack.getText(), "8 -> 8");
        final Label defense = lookup("#defenseLabel").query();
        assertEquals(defense.getText(), "8 -> 8");
        final Label speed = lookup("#speedLabel").query();
        assertEquals(speed.getText(), "5 -> 5");
    }
}
