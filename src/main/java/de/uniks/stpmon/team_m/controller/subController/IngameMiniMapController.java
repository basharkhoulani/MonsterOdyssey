package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.scene.Parent;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameMiniMapController extends Controller {
    @Inject
    Provider<IngameController> ingameControllerProvider;

    @Inject
    public IngameMiniMapController() {
    }

    public Parent render() {
        return super.render();
    }
}
