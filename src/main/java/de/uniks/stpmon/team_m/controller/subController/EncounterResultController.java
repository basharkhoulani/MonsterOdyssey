package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

public class EncounterResultController extends Controller {
    @FXML
    public VBox resultVBox;
    @FXML
    public Text informationText;
    @FXML
    public Button okButton;
    @Inject
    Provider<EncounterController> encounterController;
    @Inject
    Provider<IngameController> ingameControllerProvider;


    @Inject
    public EncounterResultController() {
    }

    public void init(App app) {
        this.app = app;
    }

    public void clickOK() {
        encounterController.get().destroy();
        ingameControllerProvider.get().setIsNewStart(false);
        app.show(ingameControllerProvider.get());

    }

    public void setInformationText(String text) {
        informationText.setText(text);
    }

}
