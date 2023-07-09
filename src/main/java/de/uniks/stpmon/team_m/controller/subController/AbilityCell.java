package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.ABILITYPALETTE;
import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;


public class AbilityCell extends ListCell<AbilityDto> {

    public PresetsService presetsService;
    public IngameController ingameController;
    public MonstersDetailController monstersDetailController;
    private final ResourceBundle resources;
    private FXMLLoader loader;
    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);
    private String typeColor;
    private String typeImagePath;
    private Image typeImage;

    @FXML
    public VBox typeIcon;
    @FXML
    public ImageView typeImageView;
    @FXML
    public Label abilityName;
    @FXML
    public Text abilityDescription;
    @FXML
    public HBox rootAbilityBox;
    @FXML
    public Label damageLabel;
    @FXML
    public Label accuracyLabel;
    @FXML
    public Label usesLabel;
    public AbilityCell(ResourceBundle resources, PresetsService presetsService, MonstersDetailController monstersDetailController, IngameController ingameController) {
        this.ingameController = ingameController;
        this.resources = resources;
        this.presetsService = presetsService;
        this.monstersDetailController = monstersDetailController;
    }

    @Override
    protected void updateItem(AbilityDto abilityDto, boolean empty) {
        super.updateItem(abilityDto, empty);
        if (abilityDto == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #D6E8FE;");
        } else {
            loadFXML();
            disposables.add(presetsService.getAbility(abilityDto.id()).observeOn(FX_SCHEDULER)
                    .subscribe(ability -> {
                        typeColor = TYPESCOLORPALETTE.get(ability.type());
                        String style = "-fx-background-color: " + typeColor + ";";
                        typeIcon.setStyle(style);

                        typeImagePath = ABILITYPALETTE.get(abilityDto.type());
                        URL resource = Main.class.getResource("images/" + typeImagePath);

                        typeImage = new Image(resource.toString());
                        typeImageView.setImage(typeImage);
                        typeImageView.setFitHeight(45);
                        typeImageView.setFitWidth(45);

                        abilityName.setText(abilityDto.name());
                        abilityDescription.setText(abilityDto.description());

                        damageLabel.setText(abilityDto.power() + " DMG");
                        accuracyLabel.setText((abilityDto.accuracy() * 100) + " %");
                        usesLabel.setText(abilityDto.maxUses() + "/"+ abilityDto.maxUses());
                    }, error -> monstersDetailController.showError(error.getMessage())));
            setGraphic(rootAbilityBox);
            setText(null);
        }
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/AbilityCell.fxml"));
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
