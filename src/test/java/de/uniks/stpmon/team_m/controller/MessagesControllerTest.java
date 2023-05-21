package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagesControllerTest extends ApplicationTest {

    @Mock
    Provider<UserStorage> userStorageProvider;
    @Mock
    Provider<GroupStorage> groupStorageProvider;

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;

    @Mock
    Provider<NewFriendController> newFriendControllerProvider;

    @Mock
    Provider<GroupController> groupControllerProvider;

    @Mock
    UsersService usersService;
    @Mock
    GroupService groupService;
    @Mock
    MessageService messageService;
    @Mock
    Provider<UsersService> usersServiceProvider;
    @Mock
    Provider<GroupService> groupServiceProvider;
    @Mock
    Provider<MessageService> messageServiceProvider;
    @Mock
    Preferences preferences;

    @Spy
    App app = new App(null);

    @InjectMocks
    MessagesController messagesController;
    @Mock
    Provider<EventListener> eventListenerProvider;


    @Override
    public void start(Stage stage) {
        UserStorage mockUserStorage = mock(UserStorage.class);
        Mockito.when(userStorageProvider.get()).thenReturn(mockUserStorage);

        GroupStorage mockGroupStorage = mock(GroupStorage.class);
        Mockito.when(groupStorageProvider.get()).thenReturn(mockGroupStorage);

        final User Rick = new User("645cd04c11b590456276e9d9", "Rick", Constants.USER_STATUS_ONLINE, null, null);

        when(usersService.getUsers(null, null))
                .thenReturn(Observable.just(List.of(Rick,
                        new User("645cd086f249626b1eefa92e", "Morty", Constants.USER_STATUS_OFFLINE, null, null),
                        new User("645cd0a34389d5c06620fe64", "Garbage Goober", Constants.USER_STATUS_OFFLINE, null, null))));
        lenient().when(usersService.getUser("645cd04c11b590456276e9d9")).thenReturn(new Observable<User>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super User> observer) {
                observer.onNext(Rick);
            }
        });
        when(userStorageProvider.get().getFriends()).thenReturn(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"));
        when(userStorageProvider.get().get_id()).thenReturn("64610e7b82ca062bfa5b7231");
        when(userStorageProvider.get().getName()).thenReturn("Morty");

        when(groupStorageProvider.get().get_id()).thenReturn("64610ec8420b3d786212aea8");

        when(groupService.getGroups(any())).thenReturn(Observable.just(
                List.of(new Group("64610ec8420b3d786212aea8", "", List.of("64610e7b82ca062bfa5b7231", Rick._id())))));
        lenient().when(messageService.getGroupMessages("64610ec8420b3d786212aea8")).thenReturn(Observable.just(List.of(
                new Message("2023-05-15T09:30:00-05:00", null, "6461e15399e24fc86fa58097", "645cd04c11b590456276e9d9", "Get in the damn car Morty!"),
                new Message("2023-05-15T09:35:00-05:00", null, "6461e15399e24fc86fa58096", "64610e7b82ca062bfa5b7231", "Oh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez Rick"),
                new Message("2023-05-15T09:35:00-05:00", null, "6461e15399e24fc86fa58096", "64610e7b82ca062bfa5b7231", "Oh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez Rick"),
                new Message("2023-05-15T09:35:00-05:00", null, "6461e15399e24fc86fa58096", "64610e7b82ca062bfa5b7231", "Oh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez Rick")
        )));

        Mockito.when(eventListenerProvider.get()).thenReturn(mock(EventListener.class));
        Mockito.when(eventListenerProvider.get().listen(any(), any())).thenReturn(Observable.empty());

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
        GroupStorage mockedGroupStorage = mock(GroupStorage.class);
        Mockito.when(groupStorageProvider.get()).thenReturn(mockedGroupStorage);
        Mockito.doNothing().when(mockedGroupStorage).set_id(Mockito.anyString());
        
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
        ListView<User> friendsAndGroups = lookup("#friends").query();

        assertEquals(3, friendsAndGroups.getItems().size());

        User rick = friendsAndGroups.getItems().get(0);
        assertEquals("Rick", rick.name());
    }

    @Test
    void displayMessages() {
        VBox friendsListViewVBox = lookup("#friendsListViewVBox").query();
        ListView<User> listView = lookup("#friends").query();

        User user = listView.getItems().get(0);
        assertEquals("Rick", user.name());

        clickOn("Rick");

    }
}
