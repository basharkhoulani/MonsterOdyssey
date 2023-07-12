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
    public String currentButtonText;

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
        pauseMenuButton.setText(preferences.get("pauseMenu","ESCAPE"));
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
        preferences.put("walkUp",KeyCode.W.getChar());
        walkUpButton.setText("W");
        preferences.put("walkDown",KeyCode.S.getChar() );
        walkDownButton.setText("S");
        preferences.put("walkLeft",KeyCode.A.getChar());
        walkLeftButton.setText("A");
        preferences.put("walkRight",KeyCode.D.getChar());
        walkRightButton.setText("D");
        preferences.put("pauseMenu","ESCAPE");
        pauseMenuButton.setText("ESCAPE");
        preferences.put("interaction",KeyCode.E.getChar());
        interactionButton.setText("E");
        informationLabel.setText(resources.getString("KEYBINDINGS.DEFAULT"));
    }

    public void check() {
        preferences.put("walkUp",walkUpButton.getText());
        preferences.put("walkDown",walkDownButton.getText());
        preferences.put("walkLeft",walkLeftButton.getText());
        preferences.put("walkRight",walkRightButton.getText());
        preferences.put("pauseMenu",pauseMenuButton.getText());
        preferences.put("interaction",interactionButton.getText());
        informationLabel.setText(resources.getString("KEYBINDINGS.CHANGED"));
    }

    public void setWalkLeft() {
        setKeyPressedHandler(walkLeftButton);
    }

    public void setPauseMenu() {
        setKeyPressedHandler(pauseMenuButton);
    }

    public void setWalkRight() {
        setKeyPressedHandler(walkRightButton);
    }

    public void setInteraction() {
        setKeyPressedHandler(interactionButton);
    }

    public void setWalkDown() {
        setKeyPressedHandler(walkDownButton);
    }

    public void setWalkUp() {
        setKeyPressedHandler(walkUpButton);
    }

    private void setKeyPressedHandler(Button button){
        keyPressedHandler = event -> {
            if(event.getCode().toString().equals(walkDownButton.getText()) ||
                    event.getCode().toString().equals(walkUpButton.getText()) ||
                    event.getCode().toString().equals(walkRightButton.getText()) ||
                    event.getCode().toString().equals(walkLeftButton.getText()) ||
                    event.getCode().toString().equals(interactionButton.getText()) ||
                    event.getCode().toString().equals(pauseMenuButton.getText())) {
                informationLabel.setText(resources.getString("KEYBINDING.USED"));
                button.setText(currentButtonText);

            }
            else if(event.getCode() == KeyCode.SPACE){
                button.setText(currentButtonText);
                informationLabel.setText(resources.getString("WRONG.INPUT"));
                event.consume();
            }
            else if(event.getCode() == KeyCode.UP) {
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                button.setText("");
                button.setText("UP");
            }
            else if(event.getCode() == KeyCode.DOWN){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                button.setText("");
                button.setText("DOWN");
            }
            else if(event.getCode() == KeyCode.RIGHT){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                button.setText("");
                button.setText("RIGHT");
            }
            else if(event.getCode() == KeyCode.LEFT){
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                button.setText("");
                button.setText("LEFT");
            }
            else if (event.getCode() == KeyCode.ESCAPE) {
                informationLabel.setText(resources.getString("CLICK.CHECK"));
                button.setText("");
                button.setText("ESCAPE");
            } else {
                if (event.getText().length() != 0 && Character.isLetterOrDigit(event.getText().charAt(0))) {
                    informationLabel.setText(resources.getString("CLICK.CHECK"));
                    button.setText("");
                    button.setText(event.getText().toUpperCase());
                } else {
                    informationLabel.setText(resources.getString("WRONG.INPUT"));
                    button.setText("A");
                }
            }
            button.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedHandler);
            buttonsDisable(false, button);
        };
        buttonsDisable(true, button);
        currentButtonText = button.getText();
        button.setText("...");
        informationLabel.setText(resources.getString("WAITING.INPUT"));
        button.addEventFilter(KeyEvent.KEY_PRESSED,keyPressedHandler);
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
}
