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
        Monster monster1 = new Monster("TestCreated", "TestUpdated", "123456789", "Me", 1, 2, 2, new LinkedHashMap<String, Integer>(), new MonsterAttributes(10, 5, 5, 5), new MonsterAttributes(10, 3, 3, 3));
        Monster monster2 = new Monster("TestCreated2", "TestUpdated2", "123456788", "Me", 1, 2, 2, new LinkedHashMap<String, Integer>(), new MonsterAttributes(10, 5, 5, 5), new MonsterAttributes(10, 3, 3, 3));
        MonsterTypeDto monsterTypeDto = new MonsterTypeDto(20,"Monster1","bild", new ArrayList<>(),"hallo");
        List<Monster> monsters = new ArrayList<>();
        monsters.add(monster1);
        monsters.add(monster2);
        monsterListView.setCellFactory(param -> new MonsterCell(resources));
        monsterListView.getItems().addAll(monsters);
    }


}
