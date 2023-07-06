package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.service.PresetsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

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
    PresetsService presetsService;
    private Monster monster;


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
                        abilityButtons.get(i).setText(ability.name() + " " + entry.getValue() + "/" + ability.maxUses());
                        // Change Color

                        // setOnAction
                        i++;
                    }
                    while(i<4){
                        abilityButtons.get(i).setVisible(false);
                        i++;
                    }
                }
        ));
    }

    public void goBackBattleMenu(ActionEvent actionEvent) {
    }

    public void useAbility1(ActionEvent actionEvent) {
    }

    public void useAbility2(ActionEvent actionEvent) {
    }

    public void useAbility3(ActionEvent actionEvent) {
    }

    public void useAbility4(ActionEvent actionEvent) {
    }
}
