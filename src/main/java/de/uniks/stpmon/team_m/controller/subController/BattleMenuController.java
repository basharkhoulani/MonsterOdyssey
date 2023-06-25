package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import java.awt.*;
import java.util.Objects;

public class BattleMenuController extends Controller {

    public Button abilitiesButton;
    public Button changeMonsterButton;
    public Button currentInfoButton;

    @Inject
    public BattleMenuController(){
    }

    public Parent render(){
        final Parent parent = super.render();
        if(!GraphicsEnvironment.isHeadless()){
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../styles.css")).toExternalForm());
        }
        return parent;
    }

}
