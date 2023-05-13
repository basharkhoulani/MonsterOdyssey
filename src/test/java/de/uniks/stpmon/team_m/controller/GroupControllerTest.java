package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest extends ApplicationTest {


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

    @Spy
    App app = new App(null);
    @InjectMocks
    GroupController groupController;
    @Override
    public void start(Stage stage) {
        when(groupStorageProvider.get()).thenReturn(mock(GroupStorage.class));
        when(groupStorageProvider.get().get_id()).thenReturn("");


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
    void deleteGroup() {

    }

    @Test
    void saveGroup() {
        when(groupService.create(any(), any())).thenReturn(Observable.just(new Group("645e82c2f29727b68379f3e5",
                "ThisIsMyHood", List.of("645e82c2f29727b68379f3e5", "645e82dbe8a15911ae2c5cf3"))));

        final MessagesController messagesController = mock(MessagesController.class);
        when(messagesControllerProvider.get()).thenReturn(messagesController);
        doNothing().when(app).show(messagesController);

        when(userStorageProvider.get()).thenReturn(mock(UserStorage.class));
        when(userStorageProvider.get().get_id()).thenReturn("645e82c2f29727b68379f3e5");

        clickOn("#saveGroupButton");
        verify(groupService).create(any(), any());
        verify(app).show(messagesController);
    }

    @Test
    void searchForGroupMembers() {
        when(usersService.getUsers(any(), any())).thenReturn(Observable.just(List.of(
                new User("645e86427a1d4677f60df159", "My", "online",
                        null, List.of("645e86668b3e7de4bbd8a97f", "645e866b602ff2930dfbf7ce")),
                new User("645e86668b3e7de4bbd8a97f", "Mo", "offline",
                        null, List.of("645e866b602ff2930dfbf7ce", "645e86427a1d4677f60df159")))));

        final TextField searchFieldGroupMembers = lookup("#searchFieldGroupMembers").query();
        assertNotNull(searchFieldGroupMembers);
        final VBox groupMembersVBox = lookup("#groupMembersVBox").query();
        assertNotNull(groupMembersVBox);

        clickOn(searchFieldGroupMembers);
        write("M");
        HBox hBox = lookup("#Mo").query();
        assertNotNull(hBox);
        Button button = (Button) hBox.getChildren().get(1);
        assertNotNull(button);
        String buttonText = button.getText();
        clickOn(button);
        assertNotEquals(buttonText, button.getText());
        verify(usersService).getUsers(any(), any());
        clickOn(groupMembersVBox);
        clickOn(searchFieldGroupMembers);
        write("a");
        hBox = lookup("#MoOther").query();
        button = (Button) hBox.getChildren().get(1);
        clickOn(button);
        clickOn(button);
        assertTrue(groupMembersVBox.getChildren().isEmpty());
        clickOn(searchFieldGroupMembers);
        type(KeyCode.BACK_SPACE);
        type(KeyCode.BACK_SPACE);
        write("M");
        hBox = lookup("#Mo").query();
        button = (Button) hBox.getChildren().get(1);
        clickOn(button);
        clickOn(groupMembersVBox);
        clickOn(searchFieldGroupMembers);
        type(KeyCode.BACK_SPACE);
        write("M");
        hBox = lookup("#Mo").query();
        button = (Button) hBox.getChildren().get(1);
        clickOn(button);
        clickOn(button);
        clickOn(groupMembersVBox);
        hBox = lookup("#MoOther").query();
        button = (Button) hBox.getChildren().get(1);
        clickOn(button);
    }
}
