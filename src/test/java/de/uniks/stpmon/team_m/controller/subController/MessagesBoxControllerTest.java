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
import io.reactivex.rxjava3.core.Observable;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        when(groupStorageProvider.get()).thenReturn(groupStorage);
        userStorage.set_id("6477bc8f27adf9b5b978401e");
        groupStorage.set_id("6477bcb4634144ca9efec424");
        messagesBoxController.setGroup(new Group("6477bcb4634144ca9efec424", "TestGroup", List.of("6477bc8f27adf9b5b978401e")));
        messagesBoxController.setUser(null);
        when(groupService.getGroups(any())).thenReturn(Observable.just(List.of(
                new Group("6477bcb4634144ca9efec424", "TestGroup", List.of("6477bc8f27adf9b5b978401e"))
        )));
        when(messageService.getGroupMessages(any())).thenReturn(Observable.just(List.of()));
        EventListener eventListener = mock(EventListener.class);
        when(eventListenerProvider.get()).thenReturn(eventListener);
        when(eventListener.listen(any(), any())).thenReturn(Observable.just(new Event<>("",
                new Message("2023-05-17T09:35:00-05:00", null, "6461e15399e24fc86fa58090", "64610e7b82ca062bfa5b7231", "This is new message")
        )));
        messagesBoxController.setAllUsers(List.of(
                new User("6477bc8f27adf9b5b978401e", "Rick Sanchez", "online", null, List.of())
        ));
        app.start(stage);
        app.show(messagesBoxController);
        stage.requestFocus();
    }

    @Test
    void editMessage() {

    }

    @Test
    void deleteMessage() {

    }

}