package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.*;
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

    @Spy
    App app = new App(null);

    @InjectMocks
    MainMenuController mainMenuController;

    @Override
    public void start (Stage stage) {
        app.start(stage);
        app.show(mainMenuController);
    }

    @Test
    void changeToFindNewFriends() {
        final NewFriendController newFriendController = mock(NewFriendController.class);
        when(newFriendControllerProvider.get()).thenReturn(newFriendController);
        doNothing().when(app).show(newFriendController);
        clickOn("#findNewFriendsButton");
        verify(app, times(1)).show(newFriendController);
    }

    @Test
    void changeToMessages() {
    }

    @Test
    void changeToLogin() {
    }

    @Test
    void changeToSettings() {
    }

    @Test
    void changeToIngame() {
    }
}