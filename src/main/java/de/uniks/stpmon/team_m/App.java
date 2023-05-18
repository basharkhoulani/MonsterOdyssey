package de.uniks.stpmon.team_m;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;


public class App extends Application {
    private Stage stage;
    private Controller controller;
    private final MainComponent component;
    protected final CompositeDisposable disposables = new CompositeDisposable();

    public App() {
        component = DaggerMainComponent.builder().mainApp(this).build();
    }

    public App(MainComponent component) {
        this.component = component;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setWidth(STANDARD_WIDTH);
        stage.setHeight(STANDARD_HEIGHT);
        stage.setMinHeight(MINIMUM_HEIGHT);
        stage.setMinWidth(MINIMUM_WIDTH);
        stage.setTitle(GAME_NAME);

        stage.setScene(loadingscreen());
        setAppIcon(stage);
        setTaskbarIcon();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
                    if (component == null) {
                        return;
                    }

                    final AuthenticationService authenticationService = component.authenticationService();

                    if (authenticationService.isRememberMe()) {

                        disposables.add(authenticationService.refresh()
                                .observeOn(Schedulers.from(Platform::runLater))
                                .subscribe(lr -> show(component.mainMenuController()),
                                        err -> show(component.loginController())));
                    } else {
                        show(component.loginController());
                    }
                }
        );
        pause.play();
        stage.show();
    }

    private Scene loadingscreen() {
        final ImageView loading = new ImageView(new Image(Objects.requireNonNull(App.class.getResource(LOADING_ANIMATION)).toString()));
        loading.setFitHeight(250);
        loading.setPreserveRatio(true);
        final FlowPane loadingPane = new FlowPane(loading);
        loadingPane.setAlignment(javafx.geometry.Pos.CENTER);
        return new Scene(loadingPane);
    }

    @Override
    public void stop() {
        cleanup();
    }

    private void setAppIcon(Stage stage) {
        final Image image = new Image(Objects.requireNonNull(App.class.getResource(APP_ICON)).toString());
        stage.getIcons().clear();
        stage.getIcons().add(image);
    }

    private void setTaskbarIcon() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        try {
            final Taskbar taskbar = Taskbar.getTaskbar();
            final java.awt.Image image = ImageIO.read(Objects.requireNonNull(Main.class.getResource(TASKBAR_ICON)));
            taskbar.setIconImage(image);
        } catch (Exception ignored) {

        }
    }

    public void show(Controller controller) {
        this.controller = controller;
        initAndRender(controller);
    }

    private void initAndRender(Controller controller) {
        controller.init();
        stage.getScene().setRoot(controller.render());
        stage.setTitle(GAME_NAME + " - " + controller.getTitle());
        stage.setWidth(controller.getWidth());
        stage.setHeight(controller.getHeight());
    }

    private void cleanup() {
        component.loginController().userStatusUpdate(USER_STATUS_OFFLINE);
        disposables.dispose();
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
    }
}
