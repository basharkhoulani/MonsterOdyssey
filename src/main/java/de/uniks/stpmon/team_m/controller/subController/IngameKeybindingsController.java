package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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
        Parent parent = super.render();
        walkUpButton.setText(preferences.get("walkUp","W"));
        walkDownButton.setText(preferences.get("walkDown","S"));
        walkRightButton.setText(preferences.get("walkRight","D"));
        walkLeftButton.setText(preferences.get("walkLeft","A"));
        interactionButton.setText(preferences.get("interaction","E"));
        pauseMenuButton.setText(preferences.get("pauseMenu","ESC"));
        return parent;
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
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkLeftButton.setText("");
                    walkLeftButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkLeftButton.setText("A");
                }
            }
            walkLeftButton.removeEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
            walkUpButton.setDisable(false);
            walkRightButton.setDisable(false);
            walkDownButton.setDisable(false);
            pauseMenuButton.setDisable(false);
            interactionButton.setDisable(false);
        };
        walkUpButton.setDisable(true);
        walkRightButton.setDisable(true);
        walkDownButton.setDisable(true);
        pauseMenuButton.setDisable(true);
        interactionButton.setDisable(true);
        walkLeftButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkLeftButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setPauseMenu() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    pauseMenuButton.setText("");
                    pauseMenuButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    pauseMenuButton.setText("ESC");
                }
            }
         pauseMenuButton.removeEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
         walkUpButton.setDisable(false);
         walkRightButton.setDisable(false);
         walkDownButton.setDisable(false);
         walkLeftButton.setDisable(false);
         interactionButton.setDisable(false);
        };
        walkUpButton.setDisable(true);
        walkRightButton.setDisable(true);
        walkDownButton.setDisable(true);
        walkLeftButton.setDisable(true);
        interactionButton.setDisable(true);
        pauseMenuButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        pauseMenuButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkRight() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkRightButton.setText("");
                    walkRightButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkRightButton.setText("D");
                }
            }
            walkRightButton.removeEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
            walkUpButton.setDisable(false);
            pauseMenuButton.setDisable(false);
            walkDownButton.setDisable(false);
            walkLeftButton.setDisable(false);
            interactionButton.setDisable(false);
        };
        walkUpButton.setDisable(true);
        walkLeftButton.setDisable(true);
        walkDownButton.setDisable(true);
        pauseMenuButton.setDisable(true);
        interactionButton.setDisable(true);
        walkRightButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkRightButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setInteraction() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    interactionButton.setText("");
                    interactionButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    interactionButton.setText("E");
                }
            }
            interactionButton.removeEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
            walkUpButton.setDisable(false);
            pauseMenuButton.setDisable(false);
            walkDownButton.setDisable(false);
            walkLeftButton.setDisable(false);
            walkRightButton.setDisable(false);
        };
        walkUpButton.setDisable(true);
        walkLeftButton.setDisable(true);
        walkDownButton.setDisable(true);
        pauseMenuButton.setDisable(true);
        walkRightButton.setDisable(true);
        interactionButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        interactionButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkDown() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkDownButton.setText("");
                    walkDownButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkDownButton.setText("S");
                }
            }
            walkDownButton.removeEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
            walkUpButton.setDisable(false);
            pauseMenuButton.setDisable(false);
            interactionButton.setDisable(false);
            walkLeftButton.setDisable(false);
            walkRightButton.setDisable(false);
        };
        walkUpButton.setDisable(true);
        walkRightButton.setDisable(true);
        walkLeftButton.setDisable(true);
        pauseMenuButton.setDisable(true);
        interactionButton.setDisable(true);
        walkDownButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkDownButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkUp() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkUpButton.setText("");
                    walkUpButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkUpButton.setText("W");
                }
            }
            walkUpButton.removeEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
            walkDownButton.setDisable(false);
            pauseMenuButton.setDisable(false);
            interactionButton.setDisable(false);
            walkLeftButton.setDisable(false);
            walkRightButton.setDisable(false);
        };
        walkDownButton.setDisable(true);
        walkRightButton.setDisable(true);
        walkLeftButton.setDisable(true);
        pauseMenuButton.setDisable(true);
        interactionButton.setDisable(true);
        walkUpButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkUpButton.addEventHandler(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }
}
