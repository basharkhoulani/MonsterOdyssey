package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import impl.org.controlsfx.skin.AutoCompletePopup;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditGroupControllerTest extends ApplicationTest {
    @Mock
    Provider<MessagesController> messagesControllerProvider;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Mock
    Provider<EventListener> eventListenerProvider;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Mock
    GroupService groupService;
    @Mock
    UsersService usersService;
    @Spy
    App app = new App(null);
    @InjectMocks
    GroupController groupController;

    @Override
    public void start(Stage stage) {
        /*
        GroupStorage groupStorage = mock(GroupStorage.class);
        Mockito.when(groupStorageProvider.get()).thenReturn(groupStorage);
        Mockito.when(groupStorageProvider.get().get_id()).thenReturn("645f8d731c386bcd2204da39");
        Mockito.when(eventListenerProvider.get()).thenReturn(mock(EventListener.class));
        Mockito.when(eventListenerProvider.get().listen(any(), any())).thenReturn(Observable.empty());
        Mockito.when(userStorageProvider.get()).thenReturn(mock(UserStorage.class));
        when(usersService.getUsers(any(), any())).thenReturn(Observable.just(List.of(
                new User("645e86427a1d4677f60df159", "Friend", "online", null, List.of("645e86668b3e7de4bbd8a97f", "645e866b602ff2930dfbf7ce")),
                new User("645e86668b3e7de4bbd8a97f", "LOL", "online", null, List.of("645e866b602ff2930dfbf7ce")))));
        when(groupStorageProvider.get().getName()).thenReturn("TestGroup");


        app.start(stage);
        app.show(groupController);
        stage.requestFocus();
         */
    }

    @Test
    void deleteGroupTest() {
        /*
        //TODO: fix test
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
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());

         */
    }

    @Test
    void errorAlertTest() {
        /*
        // TODO: fix test
        when(groupService.delete(any())).thenReturn(Observable.error(new Exception("Test")));
        clickOn("Delete group");
        clickOn(ButtonType.YES.getText());
        clickOn(GENERIC_ERROR);
        clickOn(ButtonType.OK.getText());
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());
        when(groupService.delete(any())).thenReturn(Observable.error(new Exception("HTTP 403")));
        clickOn("Delete group");
        clickOn(ButtonType.YES.getText());
        clickOn(HTTP_403_MESSAGE);
        clickOn(ButtonType.OK.getText());
        assertEquals("Monster Odyssey - Edit Group", app.getStage().getTitle());

         */
    }

    @Test
    void editGroup() {
        /*
        clickOn(ButtonType.OK.getText());
        when(userStorageProvider.get().getFriends()).thenReturn(List.of("645e86427a1d4677f60df159"));
        when(groupService.update(any(), any(), any())).thenReturn(Observable.just(new Group("645f8d731c386bcd2204da39",
                "TestGroupNOT", List.of("645e86668b3e7de4bbd8a97f"))));
        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);


        final TextField groupNameTextField = lookup("#groupNameInput").query();
        final String groupName = groupNameTextField.getText();
        assertEquals("TestGroup", groupName);
        clickOn("#searchFieldGroupMembers");
        write("LOL");

        clickOn(CHECK_MARK);
        clickOn("#saveGroupButton");
        clickOn(groupNameTextField);
        write("NOT");
        clickOn("#saveGroupButton");
        clickOn("#backToMessagesButton");
        /*
        HBox buttonHBox = (HBox) hBox.getChildren().get(2);
        Button button = (Button) buttonHBox.getChildren().get(0);
        final String buttonText = button.getText();
        assertEquals(CHECK_MARK, buttonText);
        clickOn(button);
        clickOn(button);
        assertEquals(ADD_MARK, button.getText());



         */
    }
}