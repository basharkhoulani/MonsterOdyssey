package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;

import javax.inject.Inject;
import javax.inject.Provider;

public class NewGroupController extends Controller{

    @Inject
    Provider<MessagesController> messagesControllerProvider;

    @Inject
    public NewGroupController() {

    }

    @Override
    public String getTitle() {
        return "New Group";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }
    
}
