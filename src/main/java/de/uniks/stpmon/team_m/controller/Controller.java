package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;

import static de.uniks.stpmon.team_m.Constants.STANDARD_HEIGHT;
import static de.uniks.stpmon.team_m.Constants.STANDARD_WIDTH;

public abstract class Controller {

    @Inject
    protected App app;

    public void init() {
    }

    public String getTitle() {
        return "";
    }

    public void destroy() {
    }

    public Parent render() {
        return load(getClass().getSimpleName().replace("Controller", ""));
    }

    protected Parent load(String view) {
        final FXMLLoader loader = new FXMLLoader((Main.class.getResource("views/" + view + ".fxml")));
        loader.setControllerFactory(c -> this);
        // loader.setResources(resources);
        try {
            return loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int getHeight() {
        return STANDARD_HEIGHT;
    }

    public int getWidth() {
        return STANDARD_WIDTH;
    }
}
