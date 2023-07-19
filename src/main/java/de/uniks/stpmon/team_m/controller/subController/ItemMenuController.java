package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.fxml.FXML;
import javafx.scene.Parent;

import javax.inject.Inject;
import javax.swing.text.html.ListView;

public class ItemMenuController extends Controller {

    IngameController ingameController;

    @Inject
    public ItemMenuController(){}

    public void init(IngameController ingameController) {
        super.init();
        this.ingameController = ingameController;
    }

    public void initItemList() {

    }
    @Override
    public Parent render() {
        return super.render();
    }



}
