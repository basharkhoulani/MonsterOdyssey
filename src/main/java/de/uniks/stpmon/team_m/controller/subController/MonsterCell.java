package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.*;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.*;

public class MonsterCell extends ListCell<Monster> {

    private final boolean other;
    private final Item item;
    private final Provider<MonsterStorage> monsterStorageProvider;
    @FXML
    public ImageView arrowUp;
    @FXML
    public ImageView arrowDown;
    @FXML
    public Button removeFromTeamButton;
    @FXML
    public Button viewDetailsButton;
    @FXML
    public VBox monsterVBox;
    @FXML
    Label monsterNameLevel;
    @FXML
    public FlowPane statusEffectsFlowPane;
    @FXML
    public HBox monsterTypesHBox;
    @FXML
    public VBox buttonsVBox;
    @FXML
    Label monsterHealth;
    @FXML
    ImageView monsterImageView;
    @FXML
    HBox rootmonsterHBox;

    private final ResourceBundle resources;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    public TrainersService trainersService;
    @Inject
    public UserStorage usersStorage;
    public final PresetsService presetsService;
    public final IngameController ingameController;
    public final EncounterController encounterController;

    @Inject
    Provider<TrainersService> trainersServiceProvider;
    final MonstersListController monstersListController;
    final ChangeMonsterListController changeMonsterListController;
    private FXMLLoader loader;

    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;

    public MonsterCell(
            ResourceBundle resources,
            PresetsService presetsService,
            MonstersListController monstersListController,
            ChangeMonsterListController changeMonsterListController,
            EncounterController encounterController,
            IngameController ingameController,
            boolean other,
            Item item,
            Provider<MonsterStorage> monsterStorageProvider
    ) {
        this.resources = resources;
        this.presetsService = presetsService;
        this.monstersListController = monstersListController;
        this.changeMonsterListController = changeMonsterListController;
        this.encounterController = encounterController;
        this.ingameController = ingameController;
        this.other = other;
        this.item = item;
        this.monsterStorageProvider = monsterStorageProvider;
    }

    @Override
    protected void updateItem(Monster monster, boolean empty) {
        super.updateItem(monster, empty);
        StringBuilder type = new StringBuilder();
        if (monster == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #CFE9DB;");
        } else {
            loadFXML();
            viewDetailsButton.prefWidthProperty().bind(removeFromTeamButton.widthProperty());
            monsterHealth.setText(monster.currentAttributes().health() + " / " + monster.attributes().health() + " HP");
            MonsterData monsterData = monsterStorageProvider.get().getMonsterData(monster._id());
            if (monsterData == null) {
                monsterStorageProvider.get().addMonsterData(monster, null, null);
            }
            monsterTypeDto = monsterStorageProvider.get().getMonsterData(monster._id()).monsterTypeDto();
            if (monsterTypeDto != null) {
                monsterNameLevel.setText(monsterTypeDto.name() + " (" + resources.getString("LEVEL").substring(0, resources.getString("LEVEL").length() - 1) + " " + monster.level() + ")");
                for (String s : monsterTypeDto.type()) {
                    type.append(s);
                }
                if (!GraphicsEnvironment.isHeadless()) {
                    renderMonsterTypes(monsterTypeDto, monsterTypesHBox.getChildren());
                }
            } else {
                disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER)
                        .subscribe(monsterTypeDto -> {
                            monsterNameLevel.setText(monsterTypeDto.name() + " (" + resources.getString("LEVEL").substring(0, resources.getString("LEVEL").length() - 1) + " " + monster.level() + ")");
                            for (String s : monsterTypeDto.type()) {
                                type.append(s);
                            }
                            monsterStorageProvider.get().addMonsterData(monster, monsterTypeDto, null);
                            if (!GraphicsEnvironment.isHeadless()) {
                                renderMonsterTypes(monsterTypeDto, monsterTypesHBox.getChildren());
                            }
                        }));
            }
            if (!GraphicsEnvironment.isHeadless()) {
                URL resourseArrowUp = Main.class.getResource("images/monster-arrange-up.png");
                assert resourseArrowUp != null;
                Image arrowUpImage = new Image(resourseArrowUp.toString());
                arrowUp.setImage(arrowUpImage);
                arrowDown.setImage(arrowUpImage);
            }
            if (monsterStorageProvider.get().getMonsterData(monster._id()).monsterImage() != null) {
                monsterImageView.setImage(monsterStorageProvider.get().getMonsterData(monster._id()).monsterImage());
                this.monsterImage = monsterStorageProvider.get().getMonsterData(monster._id()).monsterImage();
            } else {
                if (!GraphicsEnvironment.isHeadless()) {
                    this.monsterImage = monsterStorageProvider.get().getMonsterImage(monster.type());
                    monsterStorageProvider.get().updateMonsterData(monster, null, this.monsterImage);
                    monsterImageView.setImage(this.monsterImage);
                }
            }
            viewDetailsButton.setOnAction(event -> showDetails(monster));
            if (encounterController == null) {
                arrowUp.setOnMouseClicked(event -> monstersListController.changeOrderUp(monster._id()));
                arrowDown.setOnMouseClicked(event -> monstersListController.changeOrderDown(monster._id()));
                removeFromTeamButton.setOnAction(event -> monstersListController.removeFromTeam(monster));
                if (other) {
                    removeFromTeamButton.setStyle("-fx-background-color: #FFF2CC; -fx-border-width: 1px; -fx-border-color: gray;");
                    removeFromTeamButton.setText(resources.getString("ADD.MONSTER"));
                    arrowUp.setVisible(false);
                    arrowDown.setVisible(false);
                    removeFromTeamButton.setOnAction(event -> monstersListController.addToTeam(monster));
                }
            }

            if (encounterController != null) {
                arrowUp.setVisible(false);
                arrowUp.setDisable(true);
                arrowDown.setVisible(false);
                arrowDown.setDisable(true);
                removeFromTeamButton.setStyle("-fx-background-color: #D6E8FE; -fx-border-color: #7EA5C7;");

                if (item == null) {
                    removeFromTeamButton.setText(resources.getString("CHANGE.MONSTER"));

                    removeFromTeamButton.setOnAction(event -> {
                        encounterController.changeMonster(monster);
                        changeMonsterListController.onCloseMonsterList();
                    });
                } else {
                    removeFromTeamButton.setText(resources.getString("USE"));
                    viewDetailsButton.setVisible(false);
                    viewDetailsButton.setDisable(true);

                    removeFromTeamButton.setOnAction(event -> {
                        encounterController.useItem(item, monster);
                        changeMonsterListController.onItemUsed.run();
                        changeMonsterListController.onCloseMonsterList();
                    });
                }
            }

            if (item != null && encounterController == null) {
                arrowUp.setVisible(false);
                arrowUp.setDisable(true);
                arrowDown.setVisible(false);
                arrowDown.setDisable(true);
                buttonsVBox.getChildren().remove(viewDetailsButton);
                removeFromTeamButton.setStyle("-fx-background-color: #D6E8FE; -fx-border-color: #7EA5C7; -fx-min-width: 150px; -fx-min-height: 50px;");
                removeFromTeamButton.setText(resources.getString("SELECT"));
                removeFromTeamButton.setOnAction(event -> {
                    ingameController.useItem(item, monster);
                    if (monstersListController != null) {
                        monstersListController.onItemUsed.run();
                        monstersListController.onCloseMonsterList();
                    }
                });
            }
            statusEffectsFlowPane.getChildren().clear();
            monster.status().forEach(statusEffect -> {
                ImageView statusEffectImageView = new ImageView(String.valueOf(Main.class.getResource(STATUS_EFFECTS_IMAGES.get(statusEffect))));
                statusEffectImageView.setFitHeight(24);
                statusEffectImageView.setFitWidth(24);
                statusEffectsFlowPane.getChildren().add(statusEffectImageView);
            });
            arrowDown.setId("arrowDown" + monster._id());
            arrowUp.setId("arrowUp" + monster._id());
            removeFromTeamButton.setId("removeFromTeamButton" + monster._id());
            viewDetailsButton.setId("viewDetailsButton" + monster._id());
            setGraphic(rootmonsterHBox);
            setText(null);
            setStyle("-fx-background-color: #CFE9DB;  -fx-border-color: #1C701C; -fx-border-width: 2px");
        }
    }

    static void renderMonsterTypes(MonsterTypeDto monsterTypeDto, ObservableList<Node> children) {
        children.clear();
        monsterTypeDto.type().forEach(t -> {
            String typeColor = TYPESCOLORPALETTE.get(t);
            String style = "-fx-background-color: " + typeColor + "; -fx-border-color: black; -fx-border-width: 1px;";
            VBox typeIcon = new VBox();
            typeIcon.setStyle(style);
            String typeImagePath = ABILITYPALETTE.get(t);
            URL resourceType = Main.class.getResource("images/" + typeImagePath);
            assert resourceType != null;
            Image typeImage = new Image(resourceType.toString());
            ImageView typeImageView = new ImageView();
            typeImageView.setImage(typeImage);
            typeImageView.setFitHeight(45);
            typeImageView.setFitWidth(45);
            typeIcon.getChildren().add(typeImageView);
            children.add(typeIcon);
        });
    }


    private void showDetails(Monster monster) {
        if (encounterController != null) {
            this.encounterController.showMonsterDetails(monster, monsterTypeDto, monsterImage);
        } else {
            this.ingameController.showMonsterDetails(monster, monsterTypeDto, monsterImage, resources, presetsService);
        }
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/MonsterCell.fxml"));
            loader.setResources(resources);
            loader.setControllerFactory(c -> this);
            try {
                loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

