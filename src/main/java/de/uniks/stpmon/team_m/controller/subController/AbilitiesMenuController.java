package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;

public class AbilitiesMenuController extends Controller {
    @FXML
    public Button goBackButton;
    @FXML
    public Button abilityButton1;
    @FXML
    public Button abilityButton2;
    @FXML
    public Button abilityButton3;
    @FXML
    public Button abilityButton4;

    @Inject
    Provider<EncounterOpponentStorage> encounterOpponentStorageProvider;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    PresetsService presetsService;
    private Monster monster;
    private EncounterController encounterController;
    private Opponent currentOpponent;
    private List<AbilityDto> abilities;


    @Inject
    public AbilitiesMenuController() {
    }

    public void init(Monster monster, PresetsService presetsService, EncounterController encounterController, Opponent currentOpponent, List<AbilityDto> abilities) {
        super.init();
        this.monster = monster;
        this.presetsService = presetsService;
        this.encounterController = encounterController;
        this.currentOpponent = currentOpponent;
        this.abilities = abilities;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        initButtons();
        return parent;
    }

    private void initButtons() {
        // Abilities
        List<Button> abilityButtons = new ArrayList<>(Arrays.asList(abilityButton1, abilityButton2, abilityButton3, abilityButton4));

        int i = 0;
        if (monster != null) {
            for (Map.Entry<String, Integer> entry : monster.abilities().entrySet()) {
                AbilityDto ability = abilities.get(Integer.parseInt(entry.getKey()) - 1);Button abilityButton = abilityButtons.get(i);
                abilityButton.setText(ability.name() + " " + entry.getValue() + "/" + ability.maxUses());
                // Disable Button if no uses left
                if (entry.getValue() == 0) {
                    abilityButton.setDisable(true);
                }
                // Change Color
                if (TYPESCOLORPALETTE.containsKey(ability.type())) {
                    abilityButton.setStyle("-fx-background-color: " + TYPESCOLORPALETTE.get(ability.type()) + ";-fx-border-color: black");
                }
                // setOnAction
                abilityButton.setOnAction(actionEvent -> useAbility(ability));
                i++;
            }
        }
        while (i < 4) {
            abilityButtons.get(i).setVisible(false);
            i++;
        }

    }

    private void useAbility(AbilityDto ability) {
        String regionId = encounterOpponentStorageProvider.get().getRegionId();
        String encounterId = encounterOpponentStorageProvider.get().getEncounterId();
        String opponentId = currentOpponent._id();
        // here to put the selected target
        String targetId = encounterOpponentStorageProvider.get().getTargetOpponent().trainer();
        Move move = new AbilityMove("ability", ability.id(), targetId);

        disposables.add(encounterOpponentsService.updateOpponent(regionId, encounterId, opponentId, null, move).observeOn(FX_SCHEDULER).subscribe(
                opponent -> {
                    encounterController.updateDescription(resources.getString("YOU.USED") + " " + ability.name() + ". \n", true);
                    encounterController.resetRepeatedTimes();
                    encounterController.increaseCurrentMonsterIndex();
                    encounterController.goBackToBattleMenu();
                }, Throwable::printStackTrace));
    }

    public void goBack() {
        encounterController.goBackToBattleMenu();
    }
}
