package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


@ExtendWith(MockitoExtension.class)
public class EvolutionControllerTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @InjectMocks
    EvolutionController evolutionController;


    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        evolutionController.setValues(bundle, null, null, evolutionController, app);

        //when(presetsService.getMonsterImage(1)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));

        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        Monster oldMonster = new Monster("2023-06-05T17:02:40.357Z",
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
        Monster newMonster = new Monster("2023-06-05T17:02:40.357Z",
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

        evolutionController.init(
                vBox,
                stackPane,
                newMonster,
                monsterTypeDto,
                oldMonster,
                monsterTypeDto);
        app.start(stage);
        app.show(evolutionController);
        stage.requestFocus();
    }


    @Test
    public void levelUpPopUpTest() {
        final TextFlow textFlow = lookup("#evolutionTextFlow").query();
        final Text text = (Text) textFlow.getChildren().get(1);
        assertEquals("Incredible! Flamander evolves to Flamander!", text.getText());
        final Button okButton = lookup("#okButton").queryButton();
        clickOn(okButton);
    }
}
