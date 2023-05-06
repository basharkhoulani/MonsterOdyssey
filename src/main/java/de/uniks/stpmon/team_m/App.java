package de.uniks.stpmon.team_m;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;


public class App extends Application {
    private Stage stage;
    private Controller controller;

    public App() {

    }

    public App(Controller controller) {
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setWidth(STANDARD_WIDTH);
        stage.setHeight(STANDARD_HEIGHT);
        stage.setTitle(GAME_NAME);

        final Scene scene = new Scene(new Label(LOADING));
        stage.setScene(scene);

        setAppIcon(stage);
        setTaskbarIcon();

        stage.show();

        if (controller != null) {
            initAndRender(controller);
            return;
        }
        final MainComponent component = DaggerMainComponent.builder().mainApp(this).build();
        final AuthenticationService authenticationService = component.authenticationService();

        if (authenticationService.isRememberMe()) {
            authenticationService.refresh().subscribe(lr -> {
                show(component.mainMenuController());
            }, err -> {
                show(component.loginController());
            });
        } else {
            show(component.loginController());
        }
    }

    @Override
    public void stop() {
        cleanup();
    }

    private void setAppIcon(Stage stage) {
        final Image image = new Image(Objects.requireNonNull(App.class.getResource(APP_ICON)).toString());
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
        cleanup();
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
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
    }
}
