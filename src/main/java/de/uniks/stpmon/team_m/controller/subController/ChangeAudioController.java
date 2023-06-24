package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.service.AudioService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.inject.Inject;

public class ChangeAudioController extends Controller {
    @Inject
    public ChangeAudioController() {
    }

    @FXML
    private Button closeButton;

    @FXML
    private Slider audioSlider;

    @Override
    public Parent render() {
        return super.render();
    }

    public void onCloseButtonClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        AnchorPane anchorPane = (AnchorPane) stage.getScene().getRoot().lookup("#anchorPane");
        anchorPane.getChildren().remove(anchorPane.getChildren().size()-1);
    }

    public void getSliderValue() {
        audioSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue <?extends Number>observable, Number oldValue, Number newValue){
                System.out.println(newValue);
                AudioService.getInstance().setVolume(newValue.doubleValue() / 100);
            }
        });
    }
}
