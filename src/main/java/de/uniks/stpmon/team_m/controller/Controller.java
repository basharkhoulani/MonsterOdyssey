package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;

import static de.uniks.stpmon.team_m.Constants.*;

public abstract class Controller {

    @Inject
    protected App app;
    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);

    public void init() {
    }

    public String getTitle() {
        return EMPTY_STRING;
    }

    public void destroy() {
        disposables.dispose();
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
        return STANDARD_HEIGHT != app.getStage().getHeight() ? (int) app.getStage().getHeight() : STANDARD_HEIGHT;
    }

    public int getWidth() {
        return STANDARD_WIDTH != app.getStage().getWidth() ? (int) app.getStage().getWidth() : STANDARD_WIDTH;
    }
}
