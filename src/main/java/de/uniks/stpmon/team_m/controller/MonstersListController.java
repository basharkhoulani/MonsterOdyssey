package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.MonsterCell;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class MonstersListController extends Controller{

    @FXML
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
    public PresetsService presetsService;



    @Inject
    public MonstersListController() {
    }

    @Override
    public void init() {
        super.init();
        disposables.add(monstersService.getMonsters(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id())
                .subscribe(monsters -> {
                    trainerStorageProvider.get().setMonsters(new ArrayList<>(monsters));
                }, throwable -> {
                    showError(throwable.getMessage());
                    throwable.printStackTrace();
                }));
    }

    @Override
    public String getTitle() {
        return resources.getString("Monsters");
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initMonsterList();
        return parent;
    }

    private void initMonsterList() {
        monsterListView.setCellFactory(param -> new MonsterCell(resources));
        monsterListView.getItems().addAll(trainerStorageProvider.get().getMonsters());
    }


}
