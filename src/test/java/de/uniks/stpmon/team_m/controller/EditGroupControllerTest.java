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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.ADD_MARK;
import static de.uniks.stpmon.team_m.Constants.CHECK_MARK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditGroupControllerTest extends ApplicationTest {
    @Mock
    Provider<MessagesController> messagesControllerProvider;
    @Mock
    Provider<EventListener> eventListenerProvider;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Spy
    GroupStorage groupStorage;
    @Spy
    UserStorage userStorage;
    @Mock
    GroupService groupService;
    @Mock
    UsersService usersService;
    @Spy
    final
    App app = new App(null);
    @InjectMocks
    GroupController groupController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        groupController.setValues(bundle, null, null, groupController, app);
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(userStorageProvider.get()).thenReturn(userStorage);
        groupStorage.set_id("645f8d731c386bcd2204da39");
        groupStorage.setName("TestGroup");
        groupStorage.setMembers(List.of("6475e6121a0f21b9cd9fa708"));
        userStorage.set_id("6475e51abff65ded36a854ae");
        userStorage.setFriends(List.of("6475e6121a0f21b9cd9fa708"));
        when(usersService.getUsers(any(), any())).thenReturn(Observable.just(List.of(
                new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null),
                new User("6475e6325ec09749507c3848", "TestFriend 3", "offline", null, null),
                new User("6475e51abff65ded36a854ae", "TestUser", "online", null, null)
        )));
        when(eventListenerProvider.get()).thenReturn(mock(EventListener.class));
        when(eventListenerProvider.get().listen(any(), any())).thenReturn(Observable.empty());
        app.start(stage);
        app.show(groupController);
        stage.requestFocus();
    }

    @Test
    void deleteGroupTest() {
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        when(groupService.delete(any())).thenReturn(Observable.just(new Group("645f8d731c386bcd2204da39",
                "TestGroup", List.of("645f8d731c386bcd2204da40"))));
        clickOn("Delete group");
        clickOn(ButtonType.NO.getText());
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());
        clickOn("Delete group");
        clickOn(ButtonType.YES.getText());
        verify(groupService, times(1)).delete(any());
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());
    }

    @Test
    void errorAlertTest() {
        when(groupService.delete(any())).thenReturn(Observable.error(new Exception("Test")));
        groupStorage.setMembers(List.of());
        clickOn("Delete group");
        clickOn(ButtonType.YES.getText());
        clickOn("Something went wrong! Please try again later!");
        clickOn(ButtonType.OK.getText());
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());
        when(groupService.delete(any())).thenReturn(Observable.error(new Exception("HTTP 403")));
        clickOn("Delete group");
        clickOn(ButtonType.YES.getText());
        clickOn("Forbidden. Please try again later.");
        clickOn(ButtonType.OK.getText());
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());
    }

    @Test
    void editGroup() {
        when(groupService.update(any(), any(), any())).thenReturn(Observable.just(new Group("645f8d731c386bcd2204da39",
                "TestGroupNOT", List.of("645e86668b3e7de4bbd8a97f"))));
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);
        final TextField groupNameTextField = lookup("#groupNameInput").query();
        final String groupName = groupNameTextField.getText();
        assertEquals("TestGroup", groupName);
        clickOn(groupNameTextField);
        write("NOT");
        Button button = lookup("#TestUserAddOrRemoveButton").query();
        final String buttonText = button.getText();
        assertEquals(CHECK_MARK, buttonText);
        clickOn(button);
        assertEquals(ADD_MARK, button.getText());
        clickOn("#saveGroupButton");
        verify(groupService, times(1)).update(any(), any(), any());
    }
}