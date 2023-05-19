package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
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
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.USER_STATUS_OFFLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuControllerTest extends ApplicationTest {

    @Mock
    Provider<LoginController> loginControllerProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;
    @Mock
    Provider<AccountSettingController> accountSettingControllerProvider;
    @Mock
    Provider<NewFriendController> newFriendControllerProvider;
    @Mock
    Provider<MessagesController> messagesControllerProvider;
    @Mock
    RegionsApiService regionsApiService;
    @Mock
    UsersService usersService;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Mock
    Provider<Preferences> preferencesProvider;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Mock
    Provider<GroupService> groupServiceProvider;
    @Spy
    App app = new App(null);
    @InjectMocks
    MainMenuController mainMenuController;
    @Mock
    Provider<EventListener> eventListenerProvider;


    @Override
    public void start(Stage stage) {
        when(regionsApiService.getRegions()).thenReturn(Observable.just(List.of(new Region("TestRegion", "NamedRegion"))));
        UserStorage mockUserStorage = mock(UserStorage.class);
        Mockito.when(userStorageProvider.get()).thenReturn(mockUserStorage);
        Preferences preferences = mock(Preferences.class);
        Mockito.when(preferencesProvider.get()).thenReturn(preferences);
        when(usersService.getUsers(any(), any())).thenReturn(Observable.just(List.of(
                        new User("645cd04c11b590456276e9d9", "Rick", Constants.USER_STATUS_ONLINE, null, null),
                        new User("645cd086f249626b1eefa92e", "Morty", Constants.USER_STATUS_OFFLINE, null, null),
                        new User("645cd0a34389d5c06620fe64", "Garbage Goober", Constants.USER_STATUS_OFFLINE, null, null))));
        when(userStorageProvider.get().getFriends())
                .thenReturn(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"));
        mainMenuController.setInitialized(true);

        Mockito.when(eventListenerProvider.get()).thenReturn(mock(EventListener.class));
        Mockito.when(eventListenerProvider.get().listen(any(), any())).thenReturn(Observable.empty());

        app.start(stage);
        app.show(mainMenuController);
        stage.requestFocus();
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
    void changeToMessages() {
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        GroupStorage groupStorage = mock(GroupStorage.class);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        clickOn("#messagesButton");
        verify(app).show(messagesController);
    }

    @Test
    void changeToLogin() {
        when(usersService.updateUser(isNull(),anyString(),isNull(),isNull(),isNull()))
                .thenReturn(Observable.just(new User(
                        "423f8d731c386bcd2204da39",
                        "UserPatch",
                        USER_STATUS_OFFLINE,
                        null,
                        null
                )));

        when(authenticationService.logout())
                .thenReturn(Observable.just("Successful"));

        final LoginController loginController = mock(LoginController.class);
        when(loginControllerProvider.get()).thenReturn(loginController);
        doNothing().when(app).show(loginController);

        clickOn("#logoutButton");
        verify(usersService).updateUser(null,"offline", null, null,null);
        verify(authenticationService).logout();
        verify(app).show(loginController);
    }

    @Test
    void changeToSettings() {
        final AccountSettingController accountSettingController = mock(AccountSettingController.class);
        when(accountSettingControllerProvider.get()).thenReturn(accountSettingController);
        doNothing().when(app).show(accountSettingController);
        clickOn("#settingsButton");
        verify(app).show(accountSettingController);
    }

    @Test
    void changeToIngame() {
        final IngameController ingameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(ingameController);
        doNothing().when(app).show(ingameController);
        final ListView<Region> regionListView = lookup("#regionListView").query();
        clickOn(regionListView.getItems().get(0).name());
        clickOn("#startGameButton");
        verify(app).show(ingameController);
    }

    @Test
    void displayFriends() {
        GroupService groupService = mock(GroupService.class);
        when(groupServiceProvider.get()).thenReturn(groupService);
        when(groupService.getGroups(any())).thenReturn(Observable.just(
                        List.of(new Group("64610ec8420b3d786212aea8", "", List.of("64610e7b82ca062bfa5b7231", "64610e7b82ca062bfa5b7232")))
                )
        );
        ListView<User> friendListView = lookup("#friendsListView").query();
        assertEquals(3, friendListView.getItems().size());
        User user = friendListView.getItems().get(0);
        assertEquals("Garbage Goober", user.name());
        clickOn("Morty");
    }
}