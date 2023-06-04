package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;
import static io.reactivex.rxjava3.core.Observable.error;
import static io.reactivex.rxjava3.core.Observable.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewFriendControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    Provider<MessagesController> messageControllerProvider;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Spy
    GroupStorage groupStorage;
    @Spy
    UserStorage userStorage;
    @Mock
    UsersService usersService;
    @Mock
    GroupService groupService;
    @Spy
    App app = new App(null);
    @InjectMocks
    NewFriendController newFriendController;

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(newFriendController);
        stage.requestFocus();
    }

    @Test
    void getTitleTest() {
        assertEquals("Add a new friend", newFriendController.getTitle());
    }

    @Test
    void changeToMainMenu() {
        MainMenuController mainMenuController = mock(MainMenuController.class);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);
        clickOn("#mainMenuButton");
        verify(app, times(1)).show(mainMenuController);
    }

    @Test
    void addAsAFriendEmptyTextField() {
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn("#addFriendButton");
    }

    @Test
    void addAsAFriendEmptyUsers() {
        when(usersService.getUsers(any(), any())).thenReturn(just(new ArrayList<>())).thenReturn(error(new Exception(HTTP_409)));
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("test");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
    }

    @Test
    void addAsAFriendSelfAdd() {
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        when(userStorageProvider.get()).thenReturn(userStorage);
        userStorage.setName("TestFriend 1");
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 1");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
        verify(userStorage, times(1)).getName();
        assertEquals("You can't add yourself as a friend.", textField.getPromptText());
    }

    @Test
    void addAFriendAlreadyAdded() {
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        userStorage.setName("TestFriend 1");
        userStorage.setFriends(List.of("6475e6259cb7e1e7606c0dc6"));
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 2");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
        verify(userStorage, times(1)).getName();
        verify(userStorage, times(1)).getFriends();
        assertEquals("You are already friends with this user.", textField.getPromptText());
        assertEquals("", textField.getText());
    }

    @Test
    void addAFriendNoCurrentGroup() {
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(groupService.getGroups(any())).thenReturn(just(List.of()));
        when(usersService.updateUser(any(), any(), any(), any(), any())).thenReturn(just(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, Arrays.asList("6475e6259cb7e1e7606c0dc6", "64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9"))
        ));
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        when(groupService.create(any(), any())).thenReturn(just(
                new Group("64610ec8420b3d786212aea8", "CreatedGroup", List.of("6475e6121a0f21b9cd9fa708", "6475e6259cb7e1e7606c0dc6")
                )));
        userStorage.setName("TestFriend 1");
        userStorage.setFriends(new ArrayList<>(Arrays.asList("64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9")));
        userStorage.set_id("6475e6121a0f21b9cd9fa708");
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 2");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
        assertTrue(userStorage.getFriends().contains("6475e6259cb7e1e7606c0dc6"));
    }

    @Test
    void addAFriendWithCurrentGroup() {
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(groupService.getGroups(any())).thenReturn(just(List.of(
                new Group("64610ec8420b3d786212aea8", "CreatedGroup", List.of("6475e6121a0f21b9cd9fa708", "6475e6259cb7e1e7606c0dc6"))
        )));
        when(usersService.updateUser(any(), any(), any(), any(), any())).thenReturn(just(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, Arrays.asList("6475e6259cb7e1e7606c0dc6", "64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9"))
        ));
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        when(groupService.create(any(), any())).thenReturn(just(
                new Group("6477558f10c6cfb437ce4693", null, List.of("6475e6121a0f21b9cd9fa708", "6475e6259cb7e1e7606c0dc6"))
        ));
        userStorage.setName("TestFriend 1");
        userStorage.setFriends(new ArrayList<>(Arrays.asList("64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9")));
        userStorage.set_id("6475e6121a0f21b9cd9fa708");
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 2");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
        assertTrue(userStorage.getFriends().contains("6475e6259cb7e1e7606c0dc6"));
    }

    @Test
    void sendMessageUserNotFound() {
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        userStorage.setName("TestFriend 1");
        userStorage.setFriends(new ArrayList<>(Arrays.asList("64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9")));
        userStorage.set_id("6475e6121a0f21b9cd9fa708");
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 3");
        clickOn("#messageButton");
        clickOn("#messageButton");
        assertEquals(FRIEND_NOT_FOUND, textField.getPromptText());
    }

    @Test
    void sendMessageNotFriend() {
        MessagesController messagesController = mock(MessagesController.class);
        when(messageControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(groupService.getGroups(any())).thenReturn(just(List.of(
                new Group("64610ec8420b3d786212aea8", "CreatedGroup", List.of("6475e6121a0f21b9cd9fa708", "6475e6259cb7e1e7606c0dc6"))
        )));
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        when(groupService.create(any(), any())).thenReturn(just(
                new Group("6477558f10c6cfb437ce4693", null, List.of("6475e6121a0f21b9cd9fa708", "6475e6259cb7e1e7606c0dc6"))
        ));
        userStorage.setName("TestFriend 1");
        userStorage.set_id("6475e6121a0f21b9cd9fa708");
        userStorage.setFriends(new ArrayList<>(Arrays.asList("64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9")));
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 2");
        clickOn("#messageButton");
        clickOn("#messageButton");
    }

    @Test
    void sendMessageFriend() {
        MessagesController messagesController = mock(MessagesController.class);
        when(messageControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(usersService.getUsers(any(), any())).thenReturn(just(Arrays.asList(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null)
        )));
        userStorage.setName("TestFriend 1");
        userStorage.set_id("6475e6121a0f21b9cd9fa708");
        userStorage.setFriends(new ArrayList<>(Arrays.asList("64766a77235906e874a3b1d2", "64766a7f81b222e09a57b9a9", "6475e6259cb7e1e7606c0dc6")));
        TextField textField = lookup("#searchTextField").query();
        assertEquals("", textField.getText());
        clickOn(textField);
        write("TestFriend 2");
        clickOn("#messageButton");
        clickOn("#messageButton");
    }
}