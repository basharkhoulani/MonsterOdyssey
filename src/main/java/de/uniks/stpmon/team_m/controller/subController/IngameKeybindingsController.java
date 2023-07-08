package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.event.EventHandler;
import de.uniks.stpmon.team_m.App;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class IngameKeybindingsController extends Controller {

    @FXML
    public Button walkUpButton;
    @FXML
    public Button walkDownButton;
    @FXML
    public Button walkRightButton;
    @FXML
    public Button walkLeftButton;
    @FXML
    public Button interactionButton;
    @FXML
    public Button pauseMenuButton;
    @FXML
    public Button goBackButton;
    @FXML
    public Button defaultButton;
    @FXML
    public Button checkButton;
    @FXML
    public Label informationLabel;
    @Inject
    IngameController ingameController;
    private VBox ingameVbox;
    private EventHandler<KeyEvent> keyPressedHandler;

    @Inject
    public IngameKeybindingsController() {
    }

    public Parent render() {
        return super.render();
    }

    public void init(IngameController ingameController, VBox ingameVbox) {
        this.ingameController = ingameController;
        this.ingameVbox = ingameVbox;
    }

    public void goBack() {
        ingameController.root.getChildren().remove(ingameVbox);
    }

    public void setDefault() {
    }

    public void check() {
    }

    public void setWalkLeft() {
        keyPressedHandler = event -> {
            if(Character.isLetterOrDigit(event.getText().charAt(0))){
                informationLabel.setText("Walk left keybinding successfully changed!");
                walkLeftButton.setText("");
                walkLeftButton.setText(event.getText().toUpperCase());
            }else{
                informationLabel.setText("Ungültige Eingabe, versuche es erneut");
            }
        };
        walkLeftButton.setText("...");
        informationLabel.setText("Waiting for input...");
        walkLeftButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setPauseMenu() {
        keyPressedHandler = event -> {
            if(Character.isLetterOrDigit(event.getText().charAt(0))){
                informationLabel.setText("Pause Menu keybinding successfully changed!");
                pauseMenuButton.setText("");
                pauseMenuButton.setText(event.getText().toUpperCase());
            }else{
                informationLabel.setText("Ungültige Eingabe, versuche es erneut");
            }
        };
        pauseMenuButton.setText("...");
        informationLabel.setText("Waiting for input...");
        pauseMenuButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkRight() {
        keyPressedHandler = event -> {
            if(Character.isLetterOrDigit(event.getText().charAt(0))){
                informationLabel.setText("Walk right keybinding successfully changed!");
                walkRightButton.setText("");
                walkRightButton.setText(event.getText().toUpperCase());
            }else{
                informationLabel.setText("Ungültige Eingabe, versuche es erneut");
            }
        };
        walkRightButton.setText("...");
        informationLabel.setText("Waiting for input...");
        walkRightButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setInteraction() {
        keyPressedHandler = event -> {
            if(Character.isLetterOrDigit(event.getText().charAt(0))){
                informationLabel.setText("Interaction keybinding successfully changed!");
                interactionButton.setText("");
                interactionButton.setText(event.getText().toUpperCase());
            }else{
                informationLabel.setText("Ungültige Eingabe, versuche es erneut");
            }
        };
        interactionButton.setText("...");
        informationLabel.setText("Waiting for input...");
        interactionButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkDown() {
        keyPressedHandler = event -> {
            if(Character.isLetterOrDigit(event.getText().charAt(0))){
                informationLabel.setText("Walk down keybinding successfully changed!");
                walkDownButton.setText("");
                walkDownButton.setText(event.getText().toUpperCase());
            }else{
                informationLabel.setText("Ungültige Eingabe, versuche es erneut");
            }
        };
        walkDownButton.setText("...");
        informationLabel.setText("Waiting for input...");
        walkDownButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkUp() {
        keyPressedHandler = event -> {
            if(Character.isLetterOrDigit(event.getText().charAt(0))){
                informationLabel.setText("Walk up keybinding successfully changed!");
                walkUpButton.setText("");
                walkUpButton.setText(event.getText().toUpperCase());
            }else{
                informationLabel.setText("Ungültige Eingabe, versuche es erneut");
            }
        };
        walkUpButton.setText("...");
        informationLabel.setText("Waiting for input...");
        walkUpButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }
}
