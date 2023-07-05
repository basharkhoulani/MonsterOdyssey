package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MonstersListController extends Controller {
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

    public List<Monster> monsterList;
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
        if (!GraphicsEnvironment.isHeadless()) {
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../styles.css")).toExternalForm());
        }
        disposables.add(monstersService.getMonsters(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id()).observeOn(FX_SCHEDULER)
                .subscribe(list -> {
                    monsterList = list;
                }, throwable -> showError(throwable.getMessage())));

        disposables.add(trainersService.getTrainer(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id()).observeOn(FX_SCHEDULER)
                .subscribe(trainer -> {
                    activeMonstersList = monsterList.stream()
                            .filter(monster -> trainer.team().contains(monster._id()))
                            .collect(Collectors.toList());
                    initMonsterList(activeMonstersList);
                }));
        return parent;
    }

    private void initMonsterList(List<Monster> monsters) {
        monsterListViewActive.setCellFactory(param -> new MonsterCell(resources, presetsServiceProvider.get(), this));
        monsterListViewActive.getItems().addAll(monsters);
        monsterListViewActive.setFocusModel(null);
        monsterListViewActive.setSelectionModel(null);
    }

    public void onCloseMonsterList() {
        ingameController.root.getChildren().remove(monsterListVBox);
        ingameController.buttonsDisable(false);
    }
}
