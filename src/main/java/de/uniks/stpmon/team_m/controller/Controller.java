package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.ChangeLanguageController;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public abstract class Controller {

    @Inject
    protected ResourceBundle resources;
    @Inject
    protected Preferences preferences;
    @Inject
    protected Provider<ResourceBundle> resourceBundleProvider;
    @Inject
    protected PresetsService presetsService;
    protected Controller toReload;
    protected App app;
    @Inject
    Provider<EventListener> eventListenerProvider;
    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);

    private ChangeLanguageController changeLanguageController;

    /**
     * This method is called when the controller is initialized. It is called before the render() method.
     */

    public void init() {
        changeLanguageController = new ChangeLanguageController();
        changeLanguageController.init();
    }

    public void setValues(ResourceBundle resources, Preferences preferences, Provider<ResourceBundle> resourceBundleProvider, Controller toReload, App app) {
        this.resources = resources;
        this.preferences = preferences;
        this.resourceBundleProvider = resourceBundleProvider;
        this.toReload = toReload;
        this.app = app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void reload(Controller controller) {
        resources = resourceBundleProvider.get();
        app.show(controller);
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
        loader.setResources(resources);
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
        alert.setTitle(resources.getString("ERROR"));
        alert.setHeaderText(null);
        if (error.contains(HTTP_429)) {
            alert.setContentText(resources.getString("HTTP.429.MESSAGE"));
        } else if (error.contains(HTTP_409)) {
            alert.setContentText(resources.getString("HTTP.409.MESSAGE"));
        } else if (error.contains(HTTP_404)) {
            alert.setContentText(resources.getString("HTTP.404.MESSAGE"));
        } else if (error.contains(HTTP_403)) {
            alert.setContentText(resources.getString("HTTP.403.MESSAGE"));
        } else if (error.contains(HTTP_401)) {
            alert.setContentText(resources.getString("HTTP.401.MESSAGE"));
        } else if (error.contains(HTTP_400)) {
            alert.setContentText(resources.getString("HTTP.400.MESSAGE"));
        } else {
            alert.setContentText(resources.getString("GENERIC.ERROR"));
        }
        alert.showAndWait();
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setTrainerSpriteImageView(Trainer trainer, ImageView imageView, int direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            disposables.add(presetsService.getCharacter(trainer.image()).observeOn(FX_SCHEDULER).subscribe(responseBody -> {
                        javafx.scene.image.Image trainerSprite = ImageProcessor.resonseBodyToJavaFXImage(responseBody);
                        Image[] character = ImageProcessor.cropTrainerImages(trainerSprite, direction, false);
                        imageView.setImage(character[0]);
                    }, error -> showError(error.getMessage())
            ));
        }
    }

    /**
     * This method is used to open the Change Language Pop up
     */
    public void changeLanguage() {
        javafx.scene.control.Dialog<?> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        dialog.setTitle(resources.getString("CHOOSE.LANGUAGE"));
        changeLanguageController.setValues(resources, preferences, resourceBundleProvider, this, app);
        dialog.getDialogPane().setContent(changeLanguageController.render());
        dialog.showAndWait();
    }

}
