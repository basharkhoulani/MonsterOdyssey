package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.Event;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;

import static de.uniks.stpmon.team_m.Constants.*;

public abstract class Controller {

    @Inject
    protected App app;
    @Inject
    Provider<EventListener> eventListenerProvider;
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

    public void listenToUserUpdate(ObservableList<User> friends, ListView<User> friendsListView) {
        disposables.add(eventListenerProvider.get().listen("users.*.*", User.class).observeOn(FX_SCHEDULER)
                .subscribe(event -> {
                    final User user = event.data();
                    switch (event.suffix()) {
                        case "updated" -> updateUser(friends, friendsListView, event);
                        case "deleted" -> friends.remove(user);
                    }
                }));
    }

    private void updateUser(ObservableList<User> friends, ListView<User> friendsListView, Event<User> user) {
        User userToUpdate = friends.stream()
                .filter(friend -> friend._id().equals(user.data()._id()))
                .findFirst()
                .orElse(null);
        if (userToUpdate != null) {
            friends.set(friends.indexOf(userToUpdate), user.data());
            FriendListUtils.sortListView(friendsListView);
        }
    }

    public void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR);
        alert.setHeaderText(null);
        if (error.contains(HTTP_429)) {
            alert.setContentText(HTTP_429_MESSAGE);
        } else if (error.contains(HTTP_409)) {
            alert.setContentText(HTTP_409_MESSAGE);
        } else if (error.contains(HTTP_404)) {
            alert.setContentText(HTTP_404_MESSAGE);
        } else if (error.contains(HTTP_403)) {
            alert.setContentText(HTTP_403_MESSAGE);
        } else if (error.contains(HTTP_401)) {
            alert.setContentText(HTTP_401_MESSAGE);
        } else if (error.contains(HTTP_400)) {
            alert.setContentText(HTTP_400_MESSAGE);
        } else {
            alert.setContentText(GENERIC_ERROR);
        }
        alert.showAndWait();
    }

}
