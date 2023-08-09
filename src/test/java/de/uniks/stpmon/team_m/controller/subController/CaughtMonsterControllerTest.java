package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.control.Button;
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

import java.util.*;
import static org.mockito.Mockito.*;

import javax.inject.Provider;

@ExtendWith(MockitoExtension.class)
public class CaughtMonsterControllerTest extends ApplicationTest{

    @Spy
    final
    App app = new App(null);
    @InjectMocks
    CaughtMonsterController caughtMonsterController;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;




    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        caughtMonsterController.setValues(bundle, null, null, caughtMonsterController, app);
        TrainerStorage trainerStorage = mock(TrainerStorage.class);
        when(trainerStorageProvider.get()).thenReturn(trainerStorage);

        Trainer trainer = new Trainer("2023-05-30T12:02:57.510Z",
                "2023-05-30T12:01:57.510Z",
                "6475e595ac3946b6a812d865",
                "646bab5cecf584e1be02598a",
                "6475e595ac3946b6a812d868",
                "Peter",
                "Premade_Character_02.png",
                0,
                List.of("64aa9f636cec1b8f0fac57dc"),
                List.of(1),
                List.of("6475e595ac3946b6a812d863"),
                "6475e595ac3946b6a812d863",
                33,
                18,
                1,
                null, null);

        when(trainerStorageProvider.get().getTrainer()).thenReturn(trainer);

        Opponent opponent = new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                "64abfde932eb8b56aac8efac",
                true,
                true,
                "64aa9f7132eb8b56aa9eb20c",
                null,
                List.of(),
                0);

        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        Monster monster = new Monster(
                "2023-06-05T17:02:40.357Z",
                "023-06-05T17:02:40.357Z",
                "647e1530866ace3595866db2",
                "647e15308c1bb6a91fb57321",
                1,
                2,
                0,
                abilities,
                new MonsterAttributes(16, 9, 9, 7),
                new MonsterAttributes(14, 8, 8, 5),
                List.of());

        String regionId = "646bab5cecf584e1be02598a";


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
        EncounterController encounterControllerMock = mock(EncounterController.class);


        caughtMonsterController.init(
                vBox,
                stackPane,
                opponent,
                regionId,
                monster,
                monsterTypeDto,
                null,
                encounterControllerMock);
        app.start(stage);
        app.show(caughtMonsterController);
        stage.requestFocus();
    }


    @Test
    public void caughtMonsterPopUpTest() {
        lookup("#congratulationLabel").query();
        final Button okButton = lookup("#okButton").queryButton();
        clickOn(okButton);
    }
}
