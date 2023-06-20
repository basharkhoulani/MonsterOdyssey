package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.subController.FriendSettingsController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.HTTP_429;
import static de.uniks.stpmon.team_m.Constants.USER_STATUS_OFFLINE;
import static io.reactivex.rxjava3.core.Observable.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuControllerTest extends ApplicationTest {

    @Mock
    Provider<LoginController> loginControllerProvider;
    @Mock
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;
    @Spy
    TrainerStorage trainerStorage;
    @Mock
    Provider<TrainersService> trainersServiceProvider;
    @Mock
    Provider<AccountSettingController> accountSettingControllerProvider;
    @Mock
    Provider<NewFriendController> newFriendControllerProvider;
    @Mock
    Provider<MessagesController> messagesControllerProvider;
    @Mock
    Provider<FriendSettingsController> friendSettingsControllerProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Mock
    Provider<Preferences> preferencesProvider;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Mock
    Provider<EventListener> eventListenerProvider;
    @Mock
    Provider<PresetsService> presetsServiceProvider;
    @Mock
    RegionsService regionsService;
    @Mock
    UsersService usersService;
    @Spy
    Preferences preferences;
    @Spy
    UserStorage userStorage;
    @Spy
    GroupStorage groupStorage;
    @Spy
    App app = new App(null);
    @InjectMocks
    MainMenuController mainMenuController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        mainMenuController.setValues(bundle, null, null, mainMenuController, app);
        when(regionsService.getRegions()).thenReturn(just(List.of(new Region(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bc3c0a9ac1b375fb41d93",
                "Albertina",
                new Spawn("646bc436cfee07c0e408466f", 1, 1),
                new Map(
                        -1,
                        true,
                        1,
                        1,
                        "orthogonal",
                        "right-down",
                        "1.6.1",
                        "map",
                        "1.6",
                        32,
                        32,
                        List.of(),
                        16,
                        16,
                        List.of(),
                        List.of())
        ))));
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(preferencesProvider.get()).thenReturn(preferences);
        when(usersService.getUsers(any(), any())).thenReturn(just(List.of(
                new User("645cd04c11b590456276e9d9", "Rick", Constants.USER_STATUS_ONLINE, null, null),
                new User("645cd086f249626b1eefa92e", "Morty", Constants.USER_STATUS_OFFLINE, null, null),
                new User("645cd0a34389d5c06620fe64", "Garbage Goober", Constants.USER_STATUS_OFFLINE, null, null))));
        when(eventListenerProvider.get()).thenReturn(mock(EventListener.class));
        when(eventListenerProvider.get().listen(any(), any())).thenReturn(empty());
        userStorage.setFriends(List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64"));
        groupStorage.set_id("645cd04c11b590456276e9d1");

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
        when(usersService.updateUser(isNull(), anyString(), isNull(), isNull(), isNull()))
                .thenReturn(just(new User(
                        "423f8d731c386bcd2204da39",
                        "UserPatch",
                        USER_STATUS_OFFLINE,
                        null,
                        null
                ))).thenReturn(error(new Exception(HTTP_429)));

        when(authenticationService.logout())
                .thenReturn(just("Successful")).thenReturn(error(new Exception(HTTP_429)));

        final LoginController loginController = mock(LoginController.class);
        when(loginControllerProvider.get()).thenReturn(loginController);
        doNothing().when(app).show(loginController);

        clickOn("#logoutButton");
        verify(usersService).updateUser(null, "offline", null, null, null);
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
        final PresetsService presetsService = mock(PresetsService.class);
        when(presetsServiceProvider.get()).thenReturn(presetsService);
        when(presetsServiceProvider.get().getCharacter(ArgumentMatchers.anyString())).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
        final WelcomeSceneController welcomeSceneController = mock(WelcomeSceneController.class);
        when(welcomeSceneControllerProvider.get()).thenReturn(welcomeSceneController);
        final IngameController ingameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(ingameController);
        doNothing().when(app).show(ingameController);
        Mockito.doNothing().when(trainerStorage).setRegion(ArgumentMatchers.any());
        final TrainersService trainersService = mock(TrainersService.class);
        when(trainersServiceProvider.get()).thenReturn(trainersService);
        when(trainersService.getTrainers(any(), any(), any())).thenReturn(Observable.just(List.of(new Trainer(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bac223b4804b87c0b8054",
                "646bab5cecf584e1be02598a",
                "646bac8c1a74032c70fffe24",
                "Hans",
                "Premade_Character_01.png",
                0,
                List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                List.of(1,2),
                "646bacc568933551792bf3d5",
                0,
                0,
                0,
                new NPCInfo(false, false, false, false, null, null)))));
        final ListView<Region> regionListView = lookup("#regionListView").query();
        clickOn(regionListView.getItems().get(0).name());
        clickOn("#startGameButton");
        verify(app).show(ingameController);
    }

    @Test
    void displayFriends() {
        ListView<User> friendListView = lookup("#friendsListView").query();
        assertEquals(3, friendListView.getItems().size());
        User user = friendListView.getItems().get(0);
        assertEquals("Rick", user.name());
    }

    @Test
    void switchToMessagesScreen() {
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        clickOn("Rick");
        verify(app).show(messagesController);
    }

    @Test
    void clickOnFriendSettings() {
        FriendSettingsController friendSettingsController = mock(FriendSettingsController.class);
        when(friendSettingsControllerProvider.get()).thenReturn(friendSettingsController);
        doNothing().when(friendSettingsController).setUser(any());
        doNothing().when(friendSettingsController).setFriendsListView(any());
        doReturn(new Label("test")).when(friendSettingsController).render();
        Button friendSettingsButton = lookup("#RickPopOverButton").query();
        clickOn(friendSettingsButton);
        verify(friendSettingsController).setUser(any());
        verify(friendSettingsController).setFriendsListView(any());
    }
}