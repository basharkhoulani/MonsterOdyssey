package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ChangeAudioController extends Controller {
    @Inject
    public ChangeAudioController() {
    }

    @FXML
    private Button closeButton;

    @FXML
    private Slider audioSlider;

    private VBox changeAudioVBox;

    @Inject
    IngameController ingameController;

    @Override
    public Parent render() {
        return super.render();
    }


    public void init(IngameController ingameController, VBox changeAudioVBox) {
        this.ingameController = ingameController;
        this.changeAudioVBox = changeAudioVBox;
    }

    public void onCloseButtonClick() {
        ingameController.root.getChildren().remove(changeAudioVBox);
        ingameController.showSettings();
    }

    public void getSliderValue() {
        audioSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue <?extends Number>observable, Number oldValue, Number newValue){

            }
        });
    }
}
