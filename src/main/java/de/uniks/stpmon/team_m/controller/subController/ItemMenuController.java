package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.scene.Parent;

import javax.inject.Inject;

public class ItemMenuController extends Controller {

    IngameController ingameController;

    @Inject
    public ItemMenuController(){}

    public void init(IngameController ingameController) {
        super.init();
        this.ingameController = ingameController;
    }

    @Override
    public Parent render() {
        return super.render();
    }



}
