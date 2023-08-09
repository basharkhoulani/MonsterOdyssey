package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import io.reactivex.rxjava3.core.Observable;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MonstersDetailControllerTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @InjectMocks
    MonstersDetailController monstersDetailController;
    @InjectMocks
    IngameController ingameController;
    @Mock
    Provider<PresetsService> presetsServiceProvider;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        VBox monsterDetailVBox = new VBox();
        monstersDetailController.setValues(bundle, null, null, monstersDetailController, app);
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
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
        PresetsService presetsService = mock(PresetsService.class);
        when(presetsServiceProvider.get()).thenReturn(presetsService);
        when(presetsService.getAbility(anyInt())).thenReturn(Observable.just(new AbilityDto(
                1,
                "Tackle",
                "A physical attack in which the user charges and slams into the target with its whole body.",
                "normal",
                35,
                1.0,
                2)));
        monstersDetailController.init(ingameController, monsterDetailVBox, monster, monsterTypeDto, null, bundle, presetsService);
                when(presetsService.getAbilities()).thenReturn(Observable.just(List.of(
                new AbilityDto(
                        1,
                        "Tackle",
                        "A physical attack in which the user charges and slams into the target with its whole body.",
                        "normal",
                        35,
                        1.0,
                        2
                        ))));
        app.start(stage);
        app.show(monstersDetailController);
        stage.requestFocus();
    }

    @Test
    void controllerTest() {
        moveTo("Flamander");
        moveTo("Level: 1");
        moveTo("Health: 14.0/14.0");
        moveTo("Attack: 8/8");
        moveTo("Defense: 8/8");
        moveTo("Speed: 5/5");

        moveTo("Tackle");
        moveTo("100.0 %");
        moveTo("2 DMG");
        moveTo("A physical attack in which the user charges and slams into the target with its whole body.");

    }
}
