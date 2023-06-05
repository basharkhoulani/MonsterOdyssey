package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.*;
import static io.reactivex.rxjava3.core.Observable.error;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class NewGroupControllerTest extends ApplicationTest {

    @Mock
    Provider<MessagesController> messagesControllerProvider;
    @Mock
    GroupService groupService;
    @Mock
    UsersService usersService;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Mock
    Provider<EventListener> eventListenerProvider;
    @Spy
    App app = new App(null);
    @InjectMocks
    GroupController groupController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        groupController.setValues(bundle,null,null,groupController,app);
        when(groupStorageProvider.get()).thenReturn(mock(GroupStorage.class));
        when(groupStorageProvider.get().get_id()).thenReturn("");
        when(userStorageProvider.get()).thenReturn(mock(UserStorage.class));
        when(userStorageProvider.get().get_id()).thenReturn("645f8d731c386bcd2204da39");
        when(userStorageProvider.get().getFriends()).thenReturn(List.of("645e86427a1d4677f60df159"));
        when(usersService.getUsers(eq(List.of("645e86427a1d4677f60df159")), any())).thenReturn(Observable.just(List.of(
                new User("645e86427a1d4677f60df159", "Friend", "online",
                        null, List.of("645e86668b3e7de4bbd8a97f", "645e866b602ff2930dfbf7ce"))
        )));
        when(usersService.getUsers(isNull(), any())).thenReturn(Observable.just(List.of(
                new User("645e86427a1d4677f60df159", "Friend", "online",
                        null, List.of("645e86668b3e7de4bbd8a97f", "645e866b602ff2930dfbf7ce")),
                new User("645f8d731c386bcd2204da39", "NotFriend", "online",
                        null, List.of("645e86668b3e7de4bbd8a97f", "645e866b602ff2930dfbf7ce"))
        )));

        Mockito.when(eventListenerProvider.get()).thenReturn(mock(EventListener.class));
        Mockito.when(eventListenerProvider.get().listen(any(), any())).thenReturn(Observable.empty());

        app.start(stage);
        app.show(groupController);
        stage.requestFocus();
    }

    @Test
    void changeToMessages() {
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        clickOn("#backToMessagesButton");
        verify(app).show(messagesController);
    }

    @Test
    void saveGroup() {
        when(groupService.create(any(), any())).thenReturn(Observable.just(new Group("645e82c2f29727b68379f3e5",
                "ThisIsMyHood", List.of("645e82c2f29727b68379f3e5", "645e82dbe8a15911ae2c5cf3")))).thenReturn(error(new Exception(HTTP_404)));
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        clickOn("#saveGroupButton");
        verify(groupService).create(any(), any());
        verify(app).show(messagesController);
        clickOn("#saveGroupButton");
        clickOn("OK");
    }

    @Test
    void clickOnFriendButton() {
        final ListView<User> friendsList = lookup("#friendsListView").query();
        assertNotNull(friendsList);
        assertEquals(1, friendsList.getItems().size());
        assertEquals("Friend", friendsList.getItems().get(0).name());
        Button button = lookup("#FriendAddOrRemoveButton").query();
        assertNotNull(button);
        assertEquals(ADD_MARK, button.getText());
        clickOn(button);
        assertEquals(CHECK_MARK, button.getText());
    }

    @Test
    void searchForGroupMembers() {
        TextField searchFieldGroupMembers = lookup("#searchFieldGroupMembers").query();
        assertNotNull(searchFieldGroupMembers);
        clickOn(searchFieldGroupMembers);
        write("NotFriend");
        Button button = lookup("#NotFriendAddOrRemoveButton").query();
        assertNotNull(button);
        assertEquals("+", button.getText());
        clickOn(button);
    }

}
