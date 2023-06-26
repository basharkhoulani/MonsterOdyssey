package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.Objects;
import java.util.ResourceBundle;

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

    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<PresetsService> presetsServiceProvider;
    @Inject
    Provider<EventListener> eventListener;
    private BattleMenuController battleMenuController;

    @Inject
    public EncounterController() {
    }

    public void init(){
        super.init();
        battleMenuController = new BattleMenuController();
        battleMenuController.init();
    }

    public String getTitle() {return resources.getString("ENCOUNTER"); }

    public Parent render() {
        final Parent parent = super.render();
        if(!GraphicsEnvironment.isHeadless()){
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../styles.css")).toExternalForm());
        }
        battleMenu.getChildren().add(battleMenuController.render());
        return parent;
    }

    public void listenToOpponents(ObservableList<Opponent> opponents, String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + "opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Opponent opponent = event.data();
                    switch (event.suffix()) {
                        case "created" -> opponents.add(opponent);
                        case "updated" -> opponents.add(opponent);
                        case "deleted" -> opponents.removeIf(o -> o._id().equals(opponent._id()));
                    }
                }, error -> showError(error.getMessage())));
    }

}
    
