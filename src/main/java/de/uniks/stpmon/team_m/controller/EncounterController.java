package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.AbilitiesMenuController;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.dto.AbilityMove;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.Result;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class EncounterController extends Controller {
    @FXML
    public Label opponentLevel;
    @FXML
    public ProgressBar opponentHealthBar;
    @FXML
    public Label opponentMonsterName;
    @FXML
    public ImageView opponentTrainer;
    @FXML
    public ImageView opponentMonster;
    @FXML
    public ImageView myMonster;
    @FXML
    public ImageView mySprite;
    @FXML
    public ProgressBar myLevelBar;
    @FXML
    public Label myLevel;
    @FXML
    public ProgressBar myHealthBar;
    @FXML
    public Label myHealth;
    @FXML
    public Label myMonsterName;
    @FXML
    public HBox battleMenu;
    @FXML
    public Text battleDescription;
    @FXML
    public Button goBack;

    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    RegionEncountersService regionEncountersService;
    @Inject
    TrainersService trainersService;
    @Inject
    MonstersService monstersService;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<EventListener> eventListener;
    @Inject
    EncounterOpponentStorage encounterOpponentStorage;
    @Inject
    BattleMenuController battleMenuController;
    @Inject
    AbilitiesMenuController abilitiesMenuController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private String regionId;
    private String encounterId;
    private String trainerId;
    private Image myMonsterImage;
    private Image enemyMonsterImage;
    private List<Controller> subControllers = new ArrayList<>();

    @Inject
    public EncounterController() {
    }

    public void init() {
        super.init();
        regionId = encounterOpponentStorage.getRegionId();
        encounterId = encounterOpponentStorage.getEncounterId();
        trainerId = trainerStorageProvider.get().getTrainer()._id();
        listenToOpponents(encounterId);
        battleMenuController.init();
        subControllers.addAll(List.of(battleMenuController, abilitiesMenuController));
    }

    public String getTitle() {
        return resources.getString("ENCOUNTER");
    }

    public Parent render() {
        final Parent parent = super.render();
        disposables.add(regionEncountersService.getEncounter(regionId,encounterId)
                .observeOn(FX_SCHEDULER).subscribe(encounter -> {
                    encounterOpponentStorage.setWild(encounter.isWild());
                    // Sprite and all relevante Information
                    showTrainer();
                    showMonster();
                }, Throwable::printStackTrace));

        // render for subcontroller
        battleMenuController.init(this, battleMenu, encounterOpponentStorage, app);
        battleMenu.getChildren().add(battleMenuController.render());

        listenToOpponents(encounterOpponentStorage.getEncounterId());
        return parent;
    }

    private void showTrainer(){
        setTrainerSpriteImageView(trainerStorageProvider.get().getTrainer(), mySprite,1);
        if(!encounterOpponentStorage.isWild()){
            String enemyTrainerId = encounterOpponentStorage.getEnemyOpponent().trainer();
            battleMenuController.showFleeButton(false);
            disposables.add(trainersService.getTrainer(regionId, enemyTrainerId)
                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                        encounterOpponentStorage.setOpponentTrainer(trainer);
                        battleDescription.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + trainer.name());
                        setTrainerSpriteImageView(trainer, opponentTrainer,3);
                    }, Throwable::printStackTrace));
        } else {
            battleMenuController.showFleeButton(true);
        }
    }

    private void showMonster() {
        // self monster
        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorage.getSelfOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    myLevelBar.setProgress((double) monster.experience() / requiredExperience(monster.level() + 1));
                    myLevel.setText(monster.level() + " LVL");
                    myHealthBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
                    myHealth.setText(monster.currentAttributes().health() + "/" + monster.attributes().health() + " HP");
                    //write monster name
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                myMonsterName.setText(m.name());
                                encounterOpponentStorage.setCurrentTrainerMonsterType(m);
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                myMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                myMonster.setImage(myMonsterImage);
                            }, Throwable::printStackTrace));
                        }, Throwable::printStackTrace));

        // enemy monster
        disposables.add(monstersService.getMonster(regionId, encounterOpponentStorage.getEnemyOpponent().trainer(), encounterOpponentStorage.getEnemyOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentEnemyMonster(monster);
                    opponentLevel.setText(monster.level() + " LVL");
                    opponentHealthBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                opponentMonsterName.setText(m.name());
                                encounterOpponentStorage.setCurrentEnemyMonsterType(m);
                                if(encounterOpponentStorage.isWild()){
                                    battleDescription.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + m.name());
                                }
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                enemyMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                opponentMonster.setImage(enemyMonsterImage);
                            }, Throwable::printStackTrace));
                        }, Throwable::printStackTrace));
    }

    public int requiredExperience(int currentLevel) {
        return (int) (Math.pow(currentLevel, 3) - Math.pow(currentLevel - 1, 3));
    }


    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
    }

    public void listenToOpponents(String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + ".trainers.*.opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Opponent opponent = event.data();
                    if(event.suffix().contains("updated")){
                        System.out.println("Opponent updated: " + opponent);
                        //only considered the ability move, change monster move should also ask the server for the new monster
                        updateOpponent(opponent);
                    }
                }, error -> showError(error.getMessage())));
    }

    private void updateOpponent(Opponent opponent) {
        // For komplexer Situation for exsample with more opponents should be considered in the future
        if(opponent.trainer().equals(trainerStorageProvider.get().getTrainer()._id())){
            encounterOpponentStorage.setSelfOpponent(opponent);
            if(opponent.move() != null) {
                System.out.println("You used" + opponent.move());
            } else {
                if(opponent.results().size() != 0){
                    for(Result r: opponent.results()){
                        System.out.println("Your Result type: " + r.type() + " ability: " + r.ability() + "effectiveness: " + r.effectiveness());
                    }
                }
            }
        } else {
            encounterOpponentStorage.setEnemyOpponent(opponent);
            if(opponent.move() != null) {
                System.out.println("Enemy used" + opponent.move());
            } else {
                if(opponent.results().size() != 0){
                    for(Result r: opponent.results()){
                        System.out.println("Their Result type: " + r.type() + " ability: " + r.ability() + "effectiveness: " + r.effectiveness());
                    }
                }
            }
        }

    }

    public void showIngameController() {
        destroy();
        app.show(ingameControllerProvider.get());
    }

    public void showAbilities() {
        battleMenu.getChildren().clear();
        Monster monster = encounterOpponentStorage.getCurrentTrainerMonster();
        abilitiesMenuController.init(monster, presetsService, battleMenu, this);
        battleMenu.getChildren().add(abilitiesMenuController.render());
    }

    public void goBackToBattleMenu() {
        battleMenu.getChildren().clear();
        battleMenuController.init(this, battleMenu, encounterOpponentStorage, app);
        battleMenu.getChildren().add(battleMenuController.render());
    }

    public void updateDescription(String information, boolean isUpdated) {
        if(isUpdated){
            battleDescription.setText(information);
        } else {
            battleDescription.setText(battleDescription.getText() + "\n" + information);
        }
    }
}
    
