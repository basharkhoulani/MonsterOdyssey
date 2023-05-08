package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

import org.controlsfx.control.textfield.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.NEW_FRIEND_TITLE;

public class NewFriendController extends Controller {

    @FXML
    public Button mainMenuButton;
    @FXML
    public Button addFriendButton;
    @FXML
    public Button messageButton;
    @FXML
    public TextField searchTextField;
    List<User> allUsers;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    UsersService usersService;
    @Inject
    UserStorage userStorage;

    @Inject
    public NewFriendController() {
    }

    @Override
    public String getTitle() {
        return NEW_FRIEND_TITLE;
    }
    @Override
    public void init() {
        super.init();
        disposables.add(usersService.getUsers(null, null).subscribe(users -> {
            allUsers = users;
        }));
    }


    @Override
    public Parent render() {
        final Parent parent = super.render();

        List<String> names = new ArrayList<>();
        for (User user : allUsers) {
            names.add(user.name());
        }
        AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(searchTextField, names);
        autoCompletionBinding.setPrefWidth(searchTextField.getPrefWidth());

        return parent;
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void addAsAFriend() {
        for (User user: allUsers) {
            if (user.name().equals(searchTextField.getText())) {
                userStorage.addFriend(user._id());
            }
        }

    }

    public void sendMessage() {
    }
}
