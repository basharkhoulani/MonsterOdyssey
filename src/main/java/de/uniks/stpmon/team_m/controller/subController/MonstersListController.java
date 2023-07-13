package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MonstersListController extends Controller {
    @FXML
    public Tab othersTab;
    @FXML
    public Tab activeTeamTab;
    @FXML
    public ListView monsterListViewOther;
    @FXML
    public Button closeButton;
    @Inject
    Provider<TrainersService> trainersServiceProvider;
    @Inject
    Provider<MonstersDetailController> monstersDetailControllerProvider;
    @Inject
    UsersService usersService;
    @Inject
    RegionsService regionsService;
    @Inject
    TrainersService trainersService;
    @Inject
    MonstersService monstersService;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    public UserStorage usersStorage;
    @Inject
    public Provider<PresetsService> presetsServiceProvider;
    @Inject
    IngameController ingameController;

    @FXML
    public ListView<Monster> monsterListViewActive;
    public VBox monsterListVBox;

    public List<Monster> activeMonstersList;

    @Inject
    public MonstersListController() {
    }

    public void init(IngameController ingameController, VBox monsterListVBox) {
        super.init();
        activeMonstersList = new ArrayList<>();
        this.ingameController = ingameController;
        this.monsterListVBox = monsterListVBox;
    }

    @Override
    public String getTitle() {
        return resources.getString("MONSTERS");
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        disposables.add(monstersService.getMonsters(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id()).observeOn(FX_SCHEDULER)
                .subscribe(list -> {
                    activeMonstersList = list.stream()
                            .filter(monster -> trainerStorageProvider.get().getTrainer().team().contains(monster._id()))
                            .collect(Collectors.toList());
                    initMonsterList(activeMonstersList);
                }, throwable -> showError(throwable.getMessage())));


        return parent;
    }

    private void initMonsterList(List<Monster> monsters) {
        monsterListViewActive.setCellFactory(param -> new MonsterCell(resources, presetsServiceProvider.get(), this, this.ingameController));
        monsterListViewActive.getItems().addAll(monsters);
        monsterListViewActive.setFocusModel(null);
        monsterListViewActive.setSelectionModel(null);
    }

    public void onCloseMonsterList() {
        ingameController.root.getChildren().remove(monsterListVBox);
        ingameController.buttonsDisable(false);
    }
}
