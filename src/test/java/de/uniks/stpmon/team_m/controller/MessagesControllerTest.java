package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.UsersApiService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.List;
import java.util.concurrent.TimeoutException;

@ExtendWith(MockitoExtension.class)
public class MessagesControllerTest extends ApplicationTest {

    @Mock
    Provider<UserStorage> userStorageProvider;

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;

    @Mock
    Provider<NewFriendController> newFriendControllerProvider;

    @Mock
    Provider<GroupController> groupControllerProvider;

    @Mock
    Provider<UsersService> usersServiceProvider;

    @Mock
    UsersApiService usersApiService;

    @Spy
    App app = new App(null);

    @InjectMocks
    MessagesController messagesController;


    @Override
    public void start(Stage stage) {
        UserStorage mockUserStorage = mock(UserStorage.class);
        Mockito.when(userStorageProvider.get()).thenReturn(mockUserStorage);

        UsersService mockUsersService = mock(UsersService.class);
        Mockito.when(usersServiceProvider.get()).thenReturn(mockUsersService);

        when(mockUsersService.getUsers(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"), null))
                .thenReturn(Observable.just(List.of(
                        new User("645cd04c11b590456276e9d9", "Rick", Constants.USER_STATUS_ONLINE, null, null),
                        new User("645cd086f249626b1eefa92e", "Morty", Constants.USER_STATUS_OFFLINE, null, null),
                        new User("645cd0a34389d5c06620fe64", "Garbage Goober", Constants.USER_STATUS_OFFLINE, null, null))));
        when(userStorageProvider.get().getFriends())
                .thenReturn(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"));

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
        final GroupController groupController = mock(GroupController.class);
        when(groupControllerProvider.get()).thenReturn(groupController);
        doNothing().when(app).show(groupController);
        clickOn("#newGroupButton");
        verify(app).show(groupController);
    }

    @Test
    void changeToSettings() {
        // TODO, first needs to be correctly implemented in the MessagesController.java
    }

    @Test
    void displayFriends() {
        VBox friendsAndGroupsVBox = lookup("#friendsAndGroupsVBox").query();
        ObservableList<Node> friendsAndGroupNodes = friendsAndGroupsVBox.getChildren();

        assertEquals(3, friendsAndGroupNodes.size());

        HBox firstFriend = (HBox) friendsAndGroupNodes.get(0);
        Text nameText = (Text) firstFriend.getChildren().get(1);

        assertEquals("Rick", nameText.getText());

        clickOn("Morty");
    }
}
