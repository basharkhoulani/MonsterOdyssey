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
            if (event.getCode() == KeyCode.ESCAPE) {
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                walkLeftButton.setText("");
                walkLeftButton.setText("ESC");
            } else {
                if (event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))) {
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkLeftButton.setText("");
                    walkLeftButton.setText(event.getText().toUpperCase());
                } else {
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkLeftButton.setText("A");
                }
            }
            walkLeftButton.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedHandler);
            buttonsDisable(false, walkLeftButton);
        };
        buttonsDisable(true, walkLeftButton);
        walkLeftButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkLeftButton.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setPauseMenu() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                pauseMenuButton.setText("");
                pauseMenuButton.setText("ESC");
            }else{
                if(event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    pauseMenuButton.setText("");
                    pauseMenuButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    pauseMenuButton.setText("ESC");
                }
            }
         pauseMenuButton.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
         buttonsDisable(false, pauseMenuButton);
        };
        buttonsDisable(true, pauseMenuButton);
        pauseMenuButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        pauseMenuButton.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkRight() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                walkRightButton.setText("");
                walkRightButton.setText("ESC");
            }else{
                if(event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkRightButton.setText("");
                    walkRightButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkRightButton.setText("D");
                }
            }
            walkRightButton.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
            buttonsDisable(false, walkRightButton);
        };
        buttonsDisable(true, walkRightButton);
        walkRightButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkRightButton.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setInteraction() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                interactionButton.setText("");
                interactionButton.setText("ESC");
            }else{
                if(event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    interactionButton.setText("");
                    interactionButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    interactionButton.setText("E");
                }
            }
            interactionButton.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
            buttonsDisable(false, interactionButton);
        };
        buttonsDisable(true, interactionButton);
        interactionButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        interactionButton.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkDown() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                walkDownButton.setText("");
                walkDownButton.setText("ESC");
            }else{
                if(event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkDownButton.setText("");
                    walkDownButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkDownButton.setText("S");
                }
            }
            walkDownButton.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
            buttonsDisable(false, walkDownButton);
        };
        buttonsDisable(true, walkDownButton);
        walkDownButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkDownButton.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void setWalkUp() {
        keyPressedHandler = event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                walkUpButton.setText("");
                walkUpButton.setText("ESC");
            }else{
                if(event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))){
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    walkUpButton.setText("");
                    walkUpButton.setText(event.getText().toUpperCase());
                }else{
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    walkUpButton.setText("W");
                }
            }
            walkUpButton.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
            buttonsDisable(false, walkUpButton);
        };
        buttonsDisable(true, walkUpButton);
        walkUpButton.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        walkUpButton.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
    }

    public void buttonsDisable(boolean set, Button currentbutton){
        walkUpButton.setDisable(set);
        walkRightButton.setDisable(set);
        walkLeftButton.setDisable(set);
        walkDownButton.setDisable(set);
        pauseMenuButton.setDisable(set);
        interactionButton.setDisable(set);
        if(set){
            currentbutton.setDisable(false);
        }
    }

    public void keyAlreadyUsed(Button currentButton){

    }
}
