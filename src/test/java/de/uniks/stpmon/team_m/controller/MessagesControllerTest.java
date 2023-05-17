package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.*;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import javafx.scene.control.ListView;
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
    Provider<UsersService> usersServiceProvider;
    @Mock
    Provider<GroupService> groupServiceProvider;
    @Mock
    Provider<MessageService> messageServiceProvider;

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

        GroupService mockGroupService = mock(GroupService.class);
        Mockito.when(groupServiceProvider.get()).thenReturn(mockGroupService);

        MessageService mockMessageService = mock(MessageService.class);
        Mockito.when(messageServiceProvider.get()).thenReturn(mockMessageService);

        final User Rick = new User("645cd04c11b590456276e9d9", "Rick", Constants.USER_STATUS_ONLINE, null, null);

        when(mockUsersService.getUsers(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"), null))
                .thenReturn(Observable.just(List.of(
                        Rick,
                        new User("645cd086f249626b1eefa92e", "Morty", Constants.USER_STATUS_OFFLINE, null, null),
                        new User("645cd0a34389d5c06620fe64", "Garbage Goober", Constants.USER_STATUS_OFFLINE, null, null))));
        when(mockUsersService.getUser("645cd04c11b590456276e9d9")).thenReturn(new Observable<User>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super User> observer) {
                observer.onNext(Rick);
            }
        });
        when(userStorageProvider.get().getFriends())
                .thenReturn(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"));
        when(userStorageProvider.get().get_id())
                .thenReturn("64610e7b82ca062bfa5b7231");
        when(userStorageProvider.get().getName())
                .thenReturn("Morty");

        when(groupServiceProvider.get().getGroups(Mockito.anyList())).thenReturn(new Observable<>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super List<Group>> observer) {
                observer.onNext(List.of(new Group("64610ec8420b3d786212aea7", "", List.of("64610e7b82ca062bfa5b7231", "645cd04c11b590456276e9d9"))));
            }
        });

        when(messageServiceProvider.get().getGroupMessages("64610ec8420b3d786212aea7")).thenReturn(new Observable<>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super List<Message>> observer) {
                observer.onNext(List.of(
                        new Message("2023-05-15T09:30:00-05:00", null, "6461e15399e24fc86fa58097", "645cd04c11b590456276e9d9", "Get in the damn car Morty!"),
                        new Message("2023-05-15T09:35:00-05:00", null, "6461e15399e24fc86fa58096", "64610e7b82ca062bfa5b7231", "Oh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez Rick"),
                        new Message("2023-05-15T09:35:00-05:00", null, "6461e15399e24fc86fa58096", "64610e7b82ca062bfa5b7231", "Oh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez Rick"),
                        new Message("2023-05-15T09:35:00-05:00", null, "6461e15399e24fc86fa58096", "64610e7b82ca062bfa5b7231", "Oh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez RickOh geez Rick")
                ));
            }
        });

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
        ListView<User> friendsAndGroups = lookup("#friendsAndGroups").query();

        assertEquals(3, friendsAndGroups.getItems().size());

        User rick = friendsAndGroups.getItems().get(0);
        assertEquals("Rick", rick.name());

        clickOn("Morty");
    }
}
