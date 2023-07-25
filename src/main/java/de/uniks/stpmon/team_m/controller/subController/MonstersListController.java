package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static de.uniks.stpmon.team_m.Constants.*;


public class MonstersListController extends Controller {
    @FXML
    public Tab othersTab;
    @FXML
    public Tab activeTeamTab;
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
    @FXML
    public ListView<Monster> monsterListViewOther;
    public VBox monsterListVBox;

    public List<Monster> activeMonstersList;
    public List<Monster> otherMonstersList;

    @Inject
    public MonstersListController() {
    }

    public void init(IngameController ingameController, VBox monsterListVBox) {
        super.init();
        activeMonstersList = new ArrayList<>();
        otherMonstersList = new ArrayList<>();
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
                    sortActiveMonstersList();

                    otherMonstersList = list.stream()
                            .filter(monster -> !trainerStorageProvider.get().getTrainer().team().contains(monster._id()))
                            .collect(Collectors.toList());
                    initOtherMonsterList(otherMonstersList);
                    initMonsterList(activeMonstersList);
                }, throwable -> showError(throwable.getMessage())));

        return parent;
    }

    private void sortActiveMonstersList() {
        List<Monster> sortedList = new ArrayList<>();
        for (String id : trainerStorageProvider.get().getTrainer().team()) {
            for (Monster monster : activeMonstersList) {
                if (monster._id().equals(id)) {
                    sortedList.add(monster);
                    break;
                }
            }
        }
        activeMonstersList = sortedList;
    }

    public void changeOrderUp(String id) {
        int index = 0;
        for (Monster monster : activeMonstersList) {
            if (monster._id().equals(id)) {
                break;
            }
            index++;
        }
        if (index > 0) {
            Monster monster = activeMonstersList.get(index);
            activeMonstersList.set(index, activeMonstersList.get(index - 1));
            activeMonstersList.set(index - 1, monster);
            updateTeam();
        }
    }

    public void changeOrderDown(String id) {
        int index = 0;
        for (Monster monster : activeMonstersList) {
            if (monster._id().equals(id)) {
                break;
            }
            index++;
        }
        if (index < activeMonstersList.size() - 1) {
            Monster monster = activeMonstersList.get(index);
            activeMonstersList.set(index, activeMonstersList.get(index + 1));
            activeMonstersList.set(index + 1, monster);
            updateTeam();
        }
    }

    public void updateTeam() {
        Trainer trainer = trainerStorageProvider.get().getTrainer();
        monsterListViewActive.getItems().clear();
        monsterListViewActive.getItems().addAll(activeMonstersList);
        List<String> team = new ArrayList<>();
        for (Monster monster1 : activeMonstersList) {
            team.add(monster1._id());
        }
        disposables.add(trainersService.updateTrainer(trainer.region(), trainer._id(), null, null, team)
                .observeOn(FX_SCHEDULER)
                .subscribe(t -> trainerStorageProvider.get().setTrainer(t), throwable -> showError(throwable.getMessage())));
    }

    private void initMonsterList(List<Monster> monsters) {
        monsterListViewActive.setCellFactory(param -> new MonsterCell(resources, presetsServiceProvider.get(), this, this.ingameController, false, false));
        monsterListViewActive.getItems().addAll(monsters);
        monsterListViewActive.setFocusModel(null);
        monsterListViewActive.setSelectionModel(null);
    }

    private void initOtherMonsterList(List<Monster> monsters) {
        monsterListViewOther.setCellFactory(param -> new MonsterCell(resources, presetsServiceProvider.get(), this, this.ingameController, false, true));
        monsterListViewOther.getItems().addAll(monsters);
        monsterListViewOther.setFocusModel(null);
        monsterListViewOther.setSelectionModel(null);
    }

    public void onCloseMonsterList() {
        ingameController.getRoot().getChildren().remove(monsterListVBox);
        ingameController.buttonsDisable(false);
    }

    public void removeFromTeam(Monster monster) {
        List<String> team = trainerStorageProvider.get().getTrainer().team();
        team.remove(monster._id());
        updateBothLists(monsterListViewOther, otherMonstersList, monsterListViewActive, activeMonstersList, monster, team);
    }

    public void addToTeam(Monster monster) {
        if (trainerStorageProvider.get().getTrainer().team().size() >= 6) {
            createLimitPopUp();
            return;
        }
        List<String> team = trainerStorageProvider.get().getTrainer().team();
        team.add(monster._id());
        updateBothLists(monsterListViewActive, activeMonstersList, monsterListViewOther, otherMonstersList, monster, team);
    }

    private void createLimitPopUp() {
        VBox limitVBox = new VBox();
        limitVBox.setId("limitVBox");
        limitVBox.setMaxHeight(popupHeight);
        limitVBox.setMaxWidth(popupWidth);
        limitVBox.getStyleClass().add("dialogTextFlow");

        // text field
        TextFlow message = new TextFlow(new Text(resources.getString("LIMIT.MESSAGE")));
        message.setPrefWidth(popupWidth);
        message.setPrefHeight(textFieldHeight);
        message.setPadding(dialogTextFlowInsets);
        message.setTextAlignment(TextAlignment.CENTER);

        Button ok = new Button(resources.getString("OK"));
        ok.getStyleClass().add("buttonsYellow");
        ok.setOnAction(event -> ingameController.getRoot().getChildren().remove(limitVBox));

        limitVBox.getChildren().addAll(message, ok);
        ingameController.getRoot().getChildren().add(limitVBox);
    }

    private void updateBothLists(ListView<Monster> listViewAdd, List<Monster> listAdd, ListView<Monster> listViewRemove, List<Monster> listRemove, Monster monster, List<String> team) {
        disposables.add(trainersService.updateTrainer(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id(), null, null, team)
                .observeOn(FX_SCHEDULER)
                .subscribe(t -> trainerStorageProvider.get().setTrainer(t), throwable -> showError(throwable.getMessage())));
        listRemove.removeIf(remove -> remove._id().equals(monster._id()));
        listViewRemove.getItems().clear();
        listViewRemove.getItems().addAll(listRemove);
        listAdd.add(monster);
        listViewAdd.getItems().clear();
        listViewAdd.getItems().addAll(listAdd);
    }
}
