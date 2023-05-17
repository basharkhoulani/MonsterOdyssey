package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.MessagesUserCell;
import de.uniks.stpmon.team_m.controller.subController.UserCell;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

    @FXML
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected
    @FXML
    public Label friendsAndGroupText;
    @FXML
    public VBox friendsListViewVBox;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button newGroupButton;
    @FXML
    public Button mainMenuButton;
    @FXML
    public Button settingsButton;
    @FXML
    public TextArea messageTextArea;
    @FXML
    public Button sendButton;
    @FXML
    public VBox chatViewVBox;
    @FXML
    public ScrollPane chatScrollPane;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<GroupController> groupControllerProvider;

    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    UsersService usersService;
    @Inject
    MessageService messageService;
    @Inject
    GroupService groupService;
    @Inject
    Preferences preferences;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private ListView<User> listView;

    @Inject
    public MessagesController() {
    }

    @Override
    public void init() {
        listView = new ListView<>(friends);
        listView.setId("friendsAndGroups");
        listView.setCellFactory(param -> new MessagesUserCell(
                preferences,
                chatViewVBox,
                currentFriendOrGroupText,
                chatScrollPane,
                userStorageProvider,
                usersService,
                messageService,
                groupService,
                disposables
        ));
        disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null)
                .observeOn(FX_SCHEDULER).subscribe(friends::setAll));
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        friendsListViewVBox.getChildren().add(listView);
        return parent;
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToNewGroup() {
        groupStorageProvider.get().set_id(EMPTY_STRING);
        app.show(groupControllerProvider.get());
    }

    public void sendMessage() {
        // TODO:
        // button fx:id: '#sendButton'
    }

    public void changeToSettings() {
        groupStorageProvider.get().set_id(LOADING);
        app.show(groupControllerProvider.get());
    }
}
