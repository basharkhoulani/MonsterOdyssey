package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.subController.MessagesBoxController;
import de.uniks.stpmon.team_m.dto.Event;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.*;
import static io.reactivex.rxjava3.core.Observable.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class MessagesControllerTest extends ApplicationTest {

    @Mock
    Provider<UserStorage> userStorageProvider;
    @Spy
    UserStorage userStorage;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Spy
    GroupStorage groupStorage;
    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    Provider<NewFriendController> newFriendControllerProvider;
    @Mock
    Provider<MessagesBoxController> messagesBoxControllerProvider;
    @Mock
    Provider<GroupController> groupControllerProvider;
    @Mock
    Provider<EventListener> eventListenerProvider;
    @Mock
    UsersService usersService;
    @Mock
    GroupService groupService;
    @Mock
    MessageService messageService;
    @InjectMocks
    MessagesController messagesController;
    @Spy
    App app = new App(null);


    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        messagesController.setValues(bundle,null,null,messagesController,app);
        Group newGroup = new Group("6478e132cd8fd7a6174991a9", "newGroup", Arrays.asList("64610e7b82ca062bfa5b7231", "645cd04c11b590456276e9d9"));
        Group changedGroup = new Group("6478e132cd8fd7a6174991a9", "changedNameGroup", Arrays.asList("64610e7b82ca062bfa5b7231", "645cd04c11b590456276e9d9"));
        User changedUser = new User("645cd0a34389d5c06620fe64", "Garbage", Constants.USER_STATUS_OFFLINE, null, null);
        MessagesBoxController messagesBoxController = mock(MessagesBoxController.class);
        EventListener eventListener = mock(EventListener.class);
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(usersService.getUsers(any(), any())).thenReturn(just(List.of(
                new User("645cd04c11b590456276e9d9", "Rick", Constants.USER_STATUS_ONLINE, null, null),
                new User("645cd086f249626b1eefa92e", "Morty", Constants.USER_STATUS_OFFLINE, null, null),
                new User("645cd0a34389d5c06620fe64", "Garbage Goober", Constants.USER_STATUS_OFFLINE, null, null)
        )));
        when(groupService.getGroups(any())).thenReturn(just(List.of(
                new Group("64610ec8420b3d786212aea8", "best Group", Arrays.asList("64610e7b82ca062bfa5b7231", "645cd04c11b590456276e9d9")),
                new Group("64610ec8420b3d786212aef8", null, List.of("645cd04c11b590456276e9d9", "64610e7b82ca062bfa5b7231")),
                new Group("64610ec8420b3d786212aef5", null, List.of("64610e7b82ca062bfa5b7231")),
                new Group("645cd04c11b590456276e9d9", null, List.of("64610e7b82ca062bfa5b7231", "645cd04c11b590456276e9d9")),
                new Group("64610ec8420b3d786212aea3", null, List.of("64610e7b82ca062bfa5b7231", "645cd0a34389d5c06620fe64"))
        )));
        when(eventListenerProvider.get()).thenReturn(eventListener);
        when(eventListener.listen(any(), any())).thenReturn(just(
                new Event<>("groups.*.nothappening", null)
        )).thenReturn(just(
                new Event<>("groups.*.nothappening", null)
                )).thenReturn(just(
                        new Event<>("groups.*.created", newGroup)
                )).thenReturn(just(
                        new Event<>("groups.*.updated", changedGroup)
                )).thenReturn(just(
                        new Event<>("groups.*.deleted", changedGroup)
                )).thenReturn(just(
                        new Event<>("users.*.updated", changedUser)
                )).thenReturn(just(
                        new Event<>("users.*.deleted", changedUser)
                )).thenReturn(error(new Exception(HTTP_400)))
                .thenReturn(error(new Exception(HTTP_403)));
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(messagesBoxControllerProvider.get()).thenReturn(messagesBoxController);
        doNothing().when(messagesBoxController).setUser(any());
        doNothing().when(messagesBoxController).setGroup(any());
        doNothing().when(messagesBoxController).setAllUsers(any());
        userStorage.setFriends(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e"));
        userStorage.set_id("64610e7b82ca062bfa5b7231");
        userStorage.setName("Morty");
        messagesController.setUserChosenFromMainMenu(true);
        messagesController.setUserChosenFromNewFriend(true);
        groupStorage.set_id("645cd04c11b590456276e9d9");

        app.start(stage);
        app.show(messagesController);
        stage.requestFocus();
    }

    @Test
    void changeToMainMenu() {
        final MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);
        clickOn("#mainMenuButton");
        verify(app).show(mainMenuController);
    }

    @Test
    void changeToFindNewFriends() {
        final NewFriendController newFriendController = mock(NewFriendController.class);
        when(newFriendControllerProvider.get()).thenReturn(newFriendController);
        doNothing().when(app).show(newFriendController);
        clickOn("#findNewFriendsButton");
        verify(app).show(newFriendController);
    }

    @Test
    void changeToNewGroup() {
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        groupStorage.set_id(null);
        final GroupController groupController = mock(GroupController.class);
        when(groupControllerProvider.get()).thenReturn(groupController);
        doNothing().when(app).show(groupController);
        clickOn("#newGroupButton");
        verify(app).show(groupController);
    }

    @Test
    void changeToSettingsAndSendMessage() {
        when(groupControllerProvider.get()).thenReturn(mock(GroupController.class));
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(messageService.newMessage(any(), any(), any())).thenReturn(empty())
                .thenReturn(empty())
                .thenReturn(error(new Exception(HTTP_401)));
        doNothing().when(app).show(any());
        groupStorage.set_id("64610ec8420b3d786212aea8");
        clickOn("best Group");
        assertTrue(lookup("#settingsButton").query().isVisible());
        clickOn("Alone");
        clickOn("Rick");
        clickOn("Morty");
        clickOn("Rick");
        clickOn("best Group");
        TextArea messageTextArea = lookup("#messageTextArea").query();
        clickOn(messageTextArea);
        write("Hello");
        type(KeyCode.ENTER);
        verify(messageService).newMessage(any(), any(), any());
        clickOn(messageTextArea);
        write("Hello");
        clickOn("#sendButton");
        verify(messageService, times(2)).newMessage(any(), any(), any());
        clickOn(messageTextArea);
        write("Hello");
        type(KeyCode.SHIFT);
        clickOn("#sendButton");
        clickOn("OK");
        verify(messageService, times(3)).newMessage(any(), any(), any());
        assertEquals("", messageTextArea.getText());
        clickOn("#settingsButton");
        verify(app).show(any(GroupController.class));
    }

    @Test
    void eventChanges() {
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        messagesController.listenToGroupChanges();
        waitForFxEvents();
        assertNotNull(lookup("newGroup").query());
        clickOn("newGroup");
        messagesController.listenToGroupChanges();
        waitForFxEvents();
        assertNotNull(lookup("changedNameGroup").query());
        clickOn("changedNameGroup");
        messagesController.listenToGroupChanges();
        waitForFxEvents();
        assertNull(lookup("changedNameGroup").tryQueryAs(Label.class).orElse(null));

        ListView<User> usersListView = lookup("#friends").query();
        messagesController.listenToUserUpdate(usersListView.getItems(), usersListView);
        waitForFxEvents();
        assertNotNull(lookup("Garbage").query());
        clickOn("Garbage");
        messagesController.listenToUserUpdate(usersListView.getItems(), usersListView);
        waitForFxEvents();
        assertNull(lookup("Garbage").tryQueryAs(Label.class).orElse(null));

        messagesController.listenToUserUpdate(usersListView.getItems(), usersListView);
        waitForFxEvents();
        clickOn("OK");
        messagesController.listenToGroupChanges();
        waitForFxEvents();
        clickOn("OK");
    }

}
