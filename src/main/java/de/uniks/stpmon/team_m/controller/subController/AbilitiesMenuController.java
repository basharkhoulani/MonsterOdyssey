package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.AbilityMove;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.Result;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;

public class AbilitiesMenuController extends Controller {
    @FXML
    public Button goBackButton;
    @FXML
    public Button ability1;
    @FXML
    public Button ability2;
    @FXML
    public Button ability3;
    @FXML
    public Button ability4;

    @Inject
    Provider<EncounterController> encounterControllerProvider;
    @Inject
    Provider<EncounterOpponentStorage> encounterOpponentStorageProvider;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    PresetsService presetsService;
    private Monster monster;
    private List<Result> results;


    @Inject
    public AbilitiesMenuController() {
    }

    public void init(Monster monster, PresetsService presetsService, ResourceBundle resources){
        super.init();
        this.monster = monster;
        this.presetsService = presetsService;
        this.resources = resources;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        initButtons();

        return parent;

    }

    private void initButtons() {
        // Abilities
        List<Button> abilityButtons = new ArrayList<>(Arrays.asList(ability1, ability2, ability3, ability4));

        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(
                abilities -> {
                    int i = 0;
                    for (Map.Entry<String, Integer> entry: monster.abilities().entrySet()) {
                        AbilityDto ability = abilities.get(Integer.parseInt(entry.getKey()));
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
                        abilityButton.setOnAction(actionEvent -> {
                            useAbility(ability, abilityButton);
                        });
                        i++;
                    }
                    while(i<4){
                        abilityButtons.get(i).setVisible(false);
                        i++;
                    }
                }
        ));
    }

    private void useAbility(AbilityDto ability, Button abilityButton) {

        String target = encounterOpponentStorageProvider.get().getEnemyOpponent()._id();
        int abilityId = ability.id();
        String regionId = encounterOpponentStorageProvider.get().getRegionId();
        String encounterId = encounterOpponentStorageProvider.get().getEncounterId();
        String opponentId = encounterOpponentStorageProvider.get().getSelfOpponent()._id();
        String selfIdmonsterId = encounterOpponentStorageProvider.get().getSelfOpponent().monster();
        AbilityMove abilityMove = new AbilityMove("ability", abilityId, target);


        disposables.add(encounterOpponentsService.updateOpponent(regionId, encounterId, opponentId, selfIdmonsterId, abilityMove)
                .observeOn(FX_SCHEDULER).subscribe( encounterOpponent -> {
                    results = encounterOpponent.results();
                    updateButton(ability, abilityButton);
                }
        ));

    }

    private void updateButton(AbilityDto ability, Button abilityButton) {
        for (Map.Entry<String, Integer> entry: monster.abilities().entrySet()) {
            if(entry.getKey().equals(String.valueOf(ability.id()))){
                entry.setValue(entry.getValue()-1);
                abilityButton.setText(ability.name() + " " + entry.getValue() + "/" + ability.maxUses());
                if(entry.getValue() == 0){
                    abilityButton.setDisable(true);
                }
            }
        };
    }


    public void goBackBattleMenu(ActionEvent actionEvent) {
    }

}
