package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.AbilityMove;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.Move;
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

import static de.uniks.stpmon.team_m.Constants.EMPTY_STRING;
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


    @Inject
    public AbilitiesMenuController() {
    }

    public void init(Monster monster, PresetsService presetsService, EncounterController encounterController) {
        super.init();
        this.monster = monster;
        this.presetsService = presetsService;
        this.encounterController = encounterController;
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

        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(
                abilities -> {
                    int i = 0;
                    if(monster != null){
                        for (Map.Entry<String, Integer> entry: monster.abilities().entrySet()) {
                            AbilityDto ability = abilities.get(Integer.parseInt(entry.getKey())-1);
                            Button abilityButton = abilityButtons.get(i);
                            abilityButton.setText(ability.name() + " " + entry.getValue() + "/" + ability.maxUses());
                            // Disable Button if no uses left
                            if(entry.getValue() == 0){
                                abilityButton.setDisable(true);
                            }
                            // Change Color
                            if (TYPESCOLORPALETTE.containsKey(ability.type())) {
                                abilityButton.setStyle("-fx-background-color: " + TYPESCOLORPALETTE.get(ability.type()) + ";-fx-border-color: black");
                            }
                            // setOnAction
                            abilityButton.setOnAction(actionEvent -> useAbility(ability, abilityButton, entry.getValue()));
                            i++;
                        }
                    }
                    while(i<4){
                        abilityButtons.get(i).setVisible(false);
                        i++;
                    }
                }, Throwable::printStackTrace));
    }

    private void useAbility(AbilityDto ability, Button abilityButton, int currentUse) {
        String regionId = encounterOpponentStorageProvider.get().getRegionId();
        String encounterId = encounterOpponentStorageProvider.get().getEncounterId();
        String opponentId = encounterOpponentStorageProvider.get().getSelfOpponent()._id();
        String targetId = encounterOpponentStorageProvider.get().getEnemyOpponent().trainer();
        Move move = new AbilityMove("ability", ability.id(), targetId);

        disposables.add(encounterOpponentsService.updateOpponent(regionId, encounterId, opponentId, null, move).observeOn(FX_SCHEDULER).subscribe(
                opponent -> {
                    updateButton(ability, abilityButton, currentUse-1);
                    encounterController.updateDescription(resources.getString("YOU.USED") + " " + ability.name() + ". \n", true);
                    encounterController.resetOppoenentUpdate();
                    encounterController.resetRepeatedTimes();
                    encounterController.goBackToBattleMenu();
                }, Throwable::printStackTrace));
    }

    private void updateButton(AbilityDto ability, Button abilityButton, int currentUse) {
        abilityButton.setText(ability.name() + " " + currentUse + "/" + ability.maxUses());
        if(currentUse == 0){
            abilityButton.setDisable(true);
        }
    }


    public void goBack() {
        encounterController.goBackToBattleMenu();
    }
}
