package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationTest;


import javax.inject.Inject;
import javax.inject.Provider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbilitiesMenuControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);

    @InjectMocks
    AbilitiesMenuController abilitiesMenuController;

    @Mock
    EncounterOpponentsService encounterOpponentsService;
    @Mock
    Provider<EncounterOpponentStorage> encounterOpponentStorageProvider;
    @Mock
    Provider<PresetsService> presetsServiceProvider;
    @InjectMocks
    EncounterController encounterController;


    @Override
    public void start(Stage stage){
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        HBox battelMenu = new HBox();
        abilitiesMenuController.setValues(bundle, null, null, abilitiesMenuController, app);

        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1",35);
        Monster monster = new Monster("2023-06-05T17:02:40.357Z",
                "023-06-05T17:02:40.357Z",
                "647e1530866ace3595866db2",
                "647e15308c1bb6a91fb57321",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(14, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5));
        PresetsService presetsService = mock(PresetsService.class);
        when(presetsService.getAbilities()).thenReturn(Observable.just(List.of(new AbilityDto(
                1,
                "Tackle",
                "A physical attack in which the user charges and slams into the target with its whole body.",
                "normal",
                35,
                1.0,
                2))));
        abilitiesMenuController.init(monster, presetsService, encounterController);
        app.start(stage);
        app.show(abilitiesMenuController);
        stage.requestFocus();
    }

    @Test
    void controllerTest() {
        final Button abilityButton1 = lookup("#abilityButton1").query();
        final Button abilityButton2 = lookup("#abilityButton2").query();
        final Button abilityButton3 = lookup("#abilityButton3").query();
        final Button abilityButton4 = lookup("#abilityButton4").query();

        assertEquals("Tackle 35/35", abilityButton1.getText());
        assertFalse(abilityButton2.isVisible());
        assertFalse(abilityButton3.isVisible());
        assertFalse(abilityButton4.isVisible());
    }

}