package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import java.io.IOException;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

    @FXML
    public VBox leftSideVBox;
    @FXML
    public VBox rightSideVBox;
    @FXML
    public Text friendsAndGroupText;
    @FXML
    public ScrollPane friendsAndGroupsScrollPane;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button newGroupButton;
    @FXML
    public Button mainMenuButton;
    @FXML
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    public VBox chatVBox;
    @FXML
    public TextArea messageTextArea;
    @FXML
    public Button sendButton;
    @FXML
    public Button settingsButton;
    @FXML
    public VBox friendsAndGroupsVBox;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<GroupController> groupControllerProvider;
    private final GroupStorage groupStorage;

    @Inject
    public MessagesController(GroupStorage groupStorage) {

        this.groupStorage = groupStorage;
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        //final Parent parent = FXMLLoader.load(Main.class.getResource("view/Monster.fxml"))
        Parent parent = super.render();
        createFriendNode();

        return parent;
    }

    @Override
    public int getHeight() {
        return MESSAGES_HEIGHT;
    }

    @Override
    public int getWidth() {
        return MESSAGES_WIDTH;
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToNewGroup() {
        groupStorage.set_id(EMPTY_STRING);
        app.show(groupControllerProvider.get());
    }

    public void createFriendNode() {
        // TODO:
        /*
         * I imagined this to be a new Pane that gets inserted in the #chatVBox.
         * The Pane should be a new fxml fragment that gets loaded into one of
         * the VBoxs' children.
         * */
        System.out.println(friendsAndGroupsVBox);
        HBox testHbox = new HBox();
        testHbox.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid none none none;");
        Text testText = new Text();
        testText.setText("HALLO");
        testText.setStyle("-fx-background-color: green");
        testHbox.getChildren().add(testText);

        for (int i = 0; i < 20; i++) {
            HBox testi = new HBox();
            testi.setPadding(new Insets(5));
            Text wombat = new Text();
            wombat.setText(Integer.toString(i));
            testi.setPrefHeight(50);
            Circle status = new Circle();
            status.setRadius(20);
            status.setFill(Color.LIGHTGREEN);
            status.setStroke(Color.BLACK);
            status.setStrokeWidth(1);
            testi.getChildren().add(status);
            testi.getChildren().add(wombat);
            testi.setMinHeight(50);
            testi.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid none none none;");
            friendsAndGroupsVBox.getChildren().add(testi);
            VBox.setVgrow(friendsAndGroupsScrollPane, Priority.ALWAYS);
        }

        System.out.println(friendsAndGroupsScrollPane.getContent());
    }

    public void creteMessageNode() {
        // TODO:
        /*
         * This will probably work similiar to the 'createFriendNode()' method.
         * */
    }

    public void sendMessage() {
        // TODO:
        // button fx:id: '#sendButton'
    }

    public void changeToSettings() {
        groupStorage.set_id(LOADING);
        app.show(groupControllerProvider.get());
    }
}
