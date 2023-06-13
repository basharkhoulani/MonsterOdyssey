package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.subController.MonsterCell;
import de.uniks.stpmon.team_m.controller.subController.MonstersDetailController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;


public class MonstersListController extends Controller {

    public ListView<Monster> monsterListView;
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
    public MonstersListController() {
    }

    @Override
    public void init() {
        super.init();
        disposables.add(monstersService.getMonsters(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id()).observeOn(FX_SCHEDULER)
                .subscribe(monsters -> trainerStorageProvider.get().setMonsters(new ArrayList<>(monsters)), throwable -> showError(throwable.getMessage())));

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
        initMonsterList();
        return parent;
    }

    private void initMonsterList() {
        monsterListView.setCellFactory(param -> new MonsterCell(resources, presetsServiceProvider.get(), this));
        monsterListView.getItems().addAll(trainerStorageProvider.get().getMonsters());
        monsterListView.setFocusModel(null);
        monsterListView.setSelectionModel(null);
    }
}
