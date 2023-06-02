package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
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

    /**
     * This method is called when the controller is initialized. It is called before the render() method.
     */

    public void init() {
    }

    /**
     * This method is called when the controller title is needed and to be set.
     */

    public String getTitle() {
        return EMPTY_STRING;
    }

    /**
     * This method is called when the controller is destroyed to clean up.
     */

    public void destroy() {
        disposables.dispose();
    }

    /**
     * This method is called when the controller is rendered.
     * It loads the fxml file and its elements and sets the controller factory.
     *
     * @return Parent object
     */

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

    /**
     * This method sets the height and minimum height of the stage.
     *
     * @return height
     */

    public int getHeight() {
        return STANDARD_HEIGHT != app.getStage().getHeight() ? (int) app.getStage().getHeight() : STANDARD_HEIGHT;
    }

    /**
     * This method sets the width and minimum width of the stage.
     *
     * @return width
     */

    public int getWidth() {
        return STANDARD_WIDTH != app.getStage().getWidth() ? (int) app.getStage().getWidth() : STANDARD_WIDTH;
    }

    /**
     * This method is called when there are user changes and updates the user list.
     *
     * @param friends         List of friends to be updated
     * @param friendsListView ListView of friends list
     */

    public void listenToUserUpdate(ObservableList<User> friends, ListView<User> friendsListView) {
        disposables.add(eventListenerProvider.get().listen("users.*.*", User.class).observeOn(FX_SCHEDULER)
                .subscribe(event -> {
                    final User user = event.data();
                    switch (event.suffix()) {
                        case "updated" -> updateUser(friends, friendsListView, user);
                        case "deleted" -> friends.remove(user);
                    }
                }, error -> showError(error.getMessage())));
    }

    /**
     * This method is called when the friend user has had a change. Its user cell will be updated with
     * the new information.
     *
     * @param friends         List of friends to be updated
     * @param friendsListView ListView of friends list
     * @param user            User to be updated
     */

    private void updateUser(ObservableList<User> friends, ListView<User> friendsListView, User user) {
        User userToUpdate = friends.stream()
                .filter(friend -> friend._id().equals(user._id()))
                .findFirst()
                .orElse(null);
        if (userToUpdate != null) {
            friends.set(friends.indexOf(userToUpdate), user);
            FriendListUtils.sortListView(friendsListView);
        }
    }

    /**
     * This method is called when there is an error and shows an alert with the error message.
     *
     * @param error Error message
     */

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
