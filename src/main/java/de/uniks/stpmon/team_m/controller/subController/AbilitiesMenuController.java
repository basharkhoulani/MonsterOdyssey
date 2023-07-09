package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.Result;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

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
    private HBox battleMenuHBox;
    private List<Result> results;
    private EncounterController encounterController;


    @Inject
    public AbilitiesMenuController() {
    }

    public void init(Monster monster, PresetsService presetsService, HBox battleMenuHBox, EncounterController encounterController) {
        super.init();
        this.monster = monster;
        this.presetsService = presetsService;
        this.battleMenuHBox = battleMenuHBox;
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
                        abilityButton.setOnAction(actionEvent -> {
                            useAbility(ability, abilityButton);
                        });
                        i++;
                    }
                    while(i<4){
                        abilityButtons.get(i).setVisible(false);
                        i++;
                    }
                }, Throwable::printStackTrace));
    }

    private void useAbility(AbilityDto ability, Button abilityButton) {

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
        }
    }


    public void goBack() {
        encounterController.goBackToBattleMenu();
    }
}
