package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;

import static de.uniks.stpmon.team_m.Constants.*;

public abstract class Controller {

    @Inject
    protected App app;
    @Inject
    Provider<EventListener> eventListener;
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

    public void sortListView(ListView<User> friendsListView) {
        friendsListView.getItems().sort(Controller::sortByOnline);
    }

    public static int sortByOnline(User o1, User o2) {
        if (o1.status().equals(USER_STATUS_ONLINE) && o2.status().equals(USER_STATUS_OFFLINE)) {
            return -1;
        } else if (o1.status().equals(USER_STATUS_OFFLINE) && o2.status().equals(USER_STATUS_ONLINE)) {
            return 1;
        } else {
            return o1.name().compareTo(o2.name());
        }
    }

    public void listenToStatusUpdate(ObservableList<User> friends, ListView<User> friendsListView) {
        disposables.add(eventListener.get().listen("users.*.updated", User.class).observeOn(FX_SCHEDULER)
                .subscribe(user -> {
                    User userToUpdate = friends.stream()
                            .filter(friend -> friend._id().equals(user.data()._id()))
                            .findFirst()
                            .orElse(null);
                    if (userToUpdate != null) {
                        friends.set(friends.indexOf(userToUpdate), user.data());
                        sortListView(friendsListView);
                        friendsListView.refresh();
                    }
                }));
    }
}
