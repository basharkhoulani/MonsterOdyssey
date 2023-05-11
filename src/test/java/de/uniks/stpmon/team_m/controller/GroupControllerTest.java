package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;

import static de.uniks.stpmon.team_m.Constants.FRIEND_ADDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest extends ApplicationTest {
    @Mock
    GroupService groupService;

    @Spy
    App app = new App(null);
    @Mock
    Provider<GroupStorage> groupStorageProvider;
    @InjectMocks
    GroupController groupController;

    @Override
    public void start(Stage stage) throws Exception {
        app.start(stage);
        app.show(groupController);
        stage.requestFocus();
    }


}