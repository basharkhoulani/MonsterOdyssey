package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Event;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
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

import static de.uniks.stpmon.team_m.Constants.HTTP_403;
import static de.uniks.stpmon.team_m.Constants.HTTP_409;
import static io.reactivex.rxjava3.core.Observable.error;
import static io.reactivex.rxjava3.core.Observable.just;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
class MessagesBoxControllerTest extends ApplicationTest {

    @Mock
    MessageService messageService;
    @Mock
    GroupService groupService;
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @Spy
    GroupStorage groupStorage;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Spy
    UserStorage userStorage;
    @Mock
    Provider<EventListener> eventListenerProvider;
    @InjectMocks
    MessagesBoxController messagesBoxController;
    @Spy
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        messagesBoxController.setValues(bundle,null,null,messagesBoxController,app);
        EventListener eventListener = mock(EventListener.class);
        Message updatedMessage = new Message("2023-05-30T12:02:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d863",
                "6477bc8f27adf9b5b978401e", "Test1Test2");
        Message message = new Message("2023-05-30T12:01:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d863",
                "6477bc8f27adf9b5b978401e", "Test1");
        Message foreignMessage = new Message("2023-05-30T12:03:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d868",
                "6477bc8f27adf9b5b978401f", "Test3");
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        when(userStorageProvider.get()).thenReturn(userStorage);
        when(groupService.getGroups(any())).thenReturn(just(List.of(
                new Group("6477bcb4634144ca9efec424", "TestGroup", List.of("6477bc8f27adf9b5b978401e"))
        )));
        when(messageService.getGroupMessages(any())).thenReturn(just(List.of()));
        when(eventListenerProvider.get()).thenReturn(eventListener);
        when(eventListener.listen(any(), any())).thenReturn(just(
                new Event<>("group.*.messages.*.nothappening", null)
        )).thenReturn(just(
                new Event<>("group.*.messages.*.created", message)
        )).thenReturn(just(
                new Event<>("group.*.messages.*.updated", updatedMessage)
        )).thenReturn(just(
                new Event<>("group.*.messages.*.deleted", updatedMessage)
        )).thenReturn(just(
                new Event<>("group.*.messages.*.created", foreignMessage)
        )).thenReturn(error(
                new Exception(HTTP_403)
        )).thenReturn(just(
                new Event<>("group.*.messages.*.created", message)
        ));
        userStorage.set_id("6477bc8f27adf9b5b978401e");
        groupStorage.set_id("6477bcb4634144ca9efec424");
        messagesBoxController.setGroup(null);
        messagesBoxController.setUser(new User("6477bc8f27adf9b5b978401e", "Rick Sanchez", "online", null, List.of()));
        messagesBoxController.setAllUsers(List.of(
                new User("6477bc8f27adf9b5b978401e", "Rick Sanchez", "online", null, List.of()),
                new User("6477bc8f27adf9b5b978401f", "Rick", "online", null, List.of())
        ));
        app.start(stage);
        app.show(messagesBoxController);
        stage.requestFocus();
    }

    @Test
    void messages() {
        Message updatedMessage = new Message("2023-05-30T12:02:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d863",
                "6477bc8f27adf9b5b978401e", "Test1Test2");
        when(messageService.updateMessage(any(), any(), any(), any())).thenReturn(just(updatedMessage)).thenReturn(error(new Exception(HTTP_403)));
        when(messageService.deleteMessage(any(), any(), any())).thenReturn(just(updatedMessage)).thenReturn(error(new Exception(HTTP_409)));
        ObservableList<Message> messageList = messagesBoxController.getMessages();
        // New Message incoming
        messagesBoxController.listenToMessages(messageList, groupStorage.get_id());
        waitForFxEvents();
        clickOn("Test1");
        clickOn("#editMessage");
        clickOn("#messageArea");
        write("Test2");
        clickOn("OK");
        verify(messageService, times(1)).updateMessage(any(), any(), any(), any());
        // Message updated
        messagesBoxController.listenToMessages(messageList, groupStorage.get_id());
        waitForFxEvents();
        assertNotNull(lookup("Test1Test2").query());
        clickOn("Test1Test2");
        clickOn("#deleteMessage");
        clickOn("OK");
        verify(messageService, times(1)).deleteMessage(any(), any(), any());
        // Message deleted
        messagesBoxController.listenToMessages(messageList, groupStorage.get_id());
        waitForFxEvents();
        assertNull(lookup("Test1Test2").tryQueryAs(Label.class).orElse(null));
        // Foreign message
        messagesBoxController.listenToMessages(messageList, groupStorage.get_id());
        waitForFxEvents();
        assertNotNull(lookup("Test3").query());
        // Error
        messagesBoxController.listenToMessages(messageList, groupStorage.get_id());
        waitForFxEvents();
        clickOn("OK");
        // New Message incoming
        messagesBoxController.listenToMessages(messageList, groupStorage.get_id());
        waitForFxEvents();
        clickOn("Test1");
        clickOn("Test3");
        // Errors
        clickOn("Test1");
        clickOn("#editMessage");
        clickOn("#messageArea");
        write("Test2");
        clickOn("OK");
        clickOn("OK");
        clickOn("Test1");
        clickOn("#deleteMessage");
        clickOn("OK");
        clickOn("OK");
    }

}