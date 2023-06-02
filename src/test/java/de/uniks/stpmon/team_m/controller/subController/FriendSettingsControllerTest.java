package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
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
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.BEST_FRIEND_PREF;
import static de.uniks.stpmon.team_m.Constants.HTTP_403;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FriendSettingsControllerTest extends ApplicationTest {

    @Mock
    Preferences preferences;
    @Spy
    UserStorage userStorage;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Mock
    UsersService usersService;
    @InjectMocks
    FriendSettingsController friendSettingsController;
    @Spy
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(friendSettingsController);
        stage.requestFocus();
    }

    @Test
    void removeBestFriendAction() {
        when(preferences.get(any(), any())).thenReturn("6477bc8f27adf9b5b978401e").thenReturn(null);
        final User user = new User("6477bc8f27adf9b5b978401e", "Testuser1", "online", null, List.of("6477bcb4634144ca9efec424"));
        final ListView<User> friendsList = new ListView<>();
        friendSettingsController.setFriendsListView(friendsList);
        friendSettingsController.setUser(user);
        clickOn("#bestFriendButton");
        verify(preferences, times(1)).put(BEST_FRIEND_PREF, "");
        assertNull(preferences.get(BEST_FRIEND_PREF, null));
    }
    @Test
    void bestFriendAction() {
        when(preferences.get(any(), any())).thenReturn("notWantedUserID").thenReturn("6477bc8f27adf9b5b978401e");
        final User user = new User("6477bc8f27adf9b5b978401e", "Testuser1", "online", null, List.of("6477bcb4634144ca9efec424"));
        final ListView<User> friendsList = new ListView<>();
        friendSettingsController.setFriendsListView(friendsList);
        friendSettingsController.setUser(user);
        clickOn("#bestFriendButton");
        verify(preferences, times(1)).put(BEST_FRIEND_PREF, "6477bc8f27adf9b5b978401e");
        assertEquals("6477bc8f27adf9b5b978401e", preferences.get(BEST_FRIEND_PREF, null));
    }

    @Test
    void deleteFriendAction() {
        when(userStorageProvider.get()).thenReturn(userStorage);
        userStorage.setFriends(Arrays.asList("6477bc8f27adf9b5b978401d", "6477bc8f27adf9b5b978401f"));
        doNothing().when(userStorage).deleteFriend(any());
        final User user1 = new User("6477bc8f27adf9b5b978401d", "Testuser2", "online", null, List.of("6477bc8f27adf9b5b978401e"));
        final User user2 = new User("6477bc8f27adf9b5b978401f", "Testuser3", "online", null, List.of("6477bc8f27adf9b5b978401e"));
        when(usersService.updateUser(any(), any(), any(), any(), any())).thenReturn(Observable.just(user1));
        final ListView<User> friendsList = new ListView<>();
        friendsList.getItems().add(user1);
        friendsList.getItems().add(user2);
        friendSettingsController.setFriendsListView(friendsList);
        friendSettingsController.setUser(user1);
        assertEquals(2, friendsList.getItems().size());
        clickOn("#deleteFriendButton");
        clickOn(ButtonType.YES.getText());
        verify(userStorage, times(1)).deleteFriend("6477bc8f27adf9b5b978401d");
        verify(usersService, times(1)).updateUser(any(), any(), any(), any(), any());
        assertEquals(1, friendsList.getItems().size());
    }

    @Test
    void deleteFriendActionError() {
        when(userStorageProvider.get()).thenReturn(userStorage);
        userStorage.setFriends(Arrays.asList("6477bc8f27adf9b5b978401d", "6477bc8f27adf9b5b978401f"));
        doNothing().when(userStorage).deleteFriend(any());
        final User user1 = new User("6477bc8f27adf9b5b978401d", "Testuser2", "online", null, List.of("6477bc8f27adf9b5b978401e"));
        final User user2 = new User("6477bc8f27adf9b5b978401f", "Testuser3", "online", null, List.of("6477bc8f27adf9b5b978401e"));
        when(usersService.updateUser(any(), any(), any(), any(), any())).thenReturn(Observable.error(new Exception(HTTP_403)));
        final ListView<User> friendsList = new ListView<>();
        friendsList.getItems().add(user1);
        friendsList.getItems().add(user2);
        friendSettingsController.setFriendsListView(friendsList);
        friendSettingsController.setUser(user1);
        assertEquals(2, friendsList.getItems().size());
        clickOn("#deleteFriendButton");
        clickOn(ButtonType.YES.getText());
        clickOn(ButtonType.OK.getText());
    }

    @Test
    void noDeleteFriendAction() {
        clickOn("#deleteFriendButton");
        clickOn(ButtonType.NO.getText());
    }
}