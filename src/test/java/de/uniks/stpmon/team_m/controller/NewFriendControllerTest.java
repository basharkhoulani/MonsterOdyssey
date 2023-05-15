package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
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
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.FRIEND_ADDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewFriendControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    UsersService usersService;
    @Mock
    GroupService groupService;
    @Spy
    App app = new App(null);
    @Mock
    Provider<UserStorage> userStorageProvider;
    @InjectMocks
    NewFriendController newFriendController;

    @Override
    public void start(Stage stage) throws Exception {
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

        clickOn("#mainMenuButton");

        verify(app).show(mainMenuController);
    }

    @Test
    void addAsAFriendTest() {
        // define mock
        when(usersService.getUsers(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Observable.just(Arrays.asList(
                        new User("1", "11", "1", "1", null),
                        new User("2", "22", "2", "2", null),
                        new User("3", "33", "3", "3", null)
                )));
        when(usersService.updateUser(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Observable.just(new User(null, null, null, null, null)));

        UserStorage userStorage = mock(UserStorage.class);
        Mockito.when(userStorage.getFriends()).thenReturn(new ArrayList<>());
        Mockito.when(userStorageProvider.get()).thenReturn(userStorage);

        clickOn("#addFriendButton");
        // click on searchbar
        final TextField searchTextField = lookup("#searchTextField").query();
        clickOn(searchTextField);
        write("11");
        clickOn("#addFriendButton");
        clickOn("#addFriendButton");
        assertEquals(FRIEND_ADDED, searchTextField.getPromptText());
    }

    @Test
    void sendMessageTest() {
        // define mock
        when(usersService.getUsers(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Observable.just(Arrays.asList(
                        new User("1", "11", "1", "1", null),
                        new User("2", "22", "2", "2", null),
                        new User("3", "33", "3", "3", null)
                )));
        UserStorage userStorage = mock(UserStorage.class);
        Mockito.when(userStorage.getFriends()).thenReturn(new ArrayList<>());
        Mockito.when(userStorageProvider.get()).thenReturn(userStorage);

        when(groupService.getGroups(any())).thenReturn(Observable.just(Arrays.asList(
                new Group("112345", "best", Arrays.asList("1", "2", "3")),
                new Group("1124", null, Arrays.asList("1", "2")),
                new Group("1151", "1", Arrays.asList("5","3","6")))));
    }
}