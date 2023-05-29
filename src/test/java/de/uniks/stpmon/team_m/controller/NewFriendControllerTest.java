package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;

import static de.uniks.stpmon.team_m.Constants.FRIEND_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewFriendControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    Provider<MessagesController> messageControllerProvider;
    @Mock
    UsersService usersService;
    @Spy
    App app = new App(null);
    @Mock
    Provider<UserStorage> userStorageProvider;
    @InjectMocks
    NewFriendController newFriendController;
    @Mock
    GroupService groupService;
    @Mock
    Provider<GroupStorage> groupStorageProvider;

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
    void backTest() {
        //successfully switch to MainMenu
        final MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);
        final GroupStorage groupStorage = mock(GroupStorage.class);
        Mockito.when(groupStorageProvider.get()).thenReturn(groupStorage);

        clickOn("#mainMenuButton");

        verify(app).show(mainMenuController);
    }

    @Test
    void addAsAFriendTest() {
        when(usersService.getUsers(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Observable.just(Arrays.asList(
                        new User("1", "11", "1", "1", null),
                        new User("2", "22", "2", "2", null),
                        new User("3", "33", "3", "3", null)
                )));
        when(usersService.updateUser(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Observable.just(new User(null, null, null, null, null)));

        UserStorage userStorage = new UserStorage();
        userStorage.set_id("1");
        userStorage.setFriends(new ArrayList<>());
        Mockito.when(userStorageProvider.get()).thenReturn(userStorage);

        // empty textfield
        clickOn("#addFriendButton");
        // click on searchbar
        final TextField searchTextField = lookup("#searchTextField").query();
        clickOn(searchTextField);
        // write in searchbar
        write("11");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
    }

    @Test
    void sendMessageTest() {
        final GroupStorage groupStorage = mock(GroupStorage.class);
        lenient().when(groupStorageProvider.get()).thenReturn(groupStorage);

        final MessagesController messageController = mock(MessagesController.class);
        lenient().when(messageControllerProvider.get()).thenReturn(messageController);

        when(usersService.getUsers(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Observable.just(Arrays.asList(
                        new User("1", "11", "1", "1", null),
                        new User("2", "22", "2", "2", null),
                        new User("3", "33", "3", "3", null)
                )));

        UserStorage userStorage = mock(UserStorage.class);
        lenient().when(userStorageProvider.get()).thenReturn(userStorage);

        lenient().when(groupService.getGroups(any())).thenReturn(Observable.just(Arrays.asList(
                new Group("112345", "best", Arrays.asList("1", "2", "3")),
                new Group("1124", null, Arrays.asList("1", "2")),
                new Group("1151", "1", Arrays.asList("5", "3", "6")))));

        lenient().when(groupService.create(any(), any())).thenReturn(Observable.just(new Group("123456", "best", Arrays.asList("1", "2", "3"))));
        lenient().doNothing().when(app).show(messageController);

        final TextField searchTextField = lookup("#searchTextField").query();
        clickOn(searchTextField);

        write("1");
        clickOn("#messageButton");
        clickOn("#messageButton");
        assertEquals(FRIEND_NOT_FOUND, searchTextField.getPromptText());
        clickOn(searchTextField);
        write("1");
        clickOn("#messageButton");
        clickOn("#messageButton");
        assertEquals("Monster Odyssey - Add a new friend", app.getStage().getTitle());
    }
}