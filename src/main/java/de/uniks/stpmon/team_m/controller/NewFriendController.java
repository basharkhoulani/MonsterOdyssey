package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;

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
    Provider<UserStorage> userStorage;

    @Inject
    public NewFriendController() {
    }

    @Override
    public String getTitle() {
        return NEW_FRIEND_TITLE;
    }

    @Override
    public Parent render() {
        return super.render();
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void addAsAFriend() {
        if (searchTextField.getText().equals("")) {
            return;
        }
        if (allUsers.isEmpty()) {
            return;
        }
        searchTextField.clear();
        for (User user : allUsers) {
            if (user.name().equals(searchTextField.getText())) {
                final String newFriend = user._id();
                userStorage.get().addFriend(newFriend);
                disposables.add(usersService.updateUser(null, null, null, userStorage.get().getFriends(), null).subscribe());
                searchTextField.setPromptText(FRIEND_ADDED);
            } else {
                searchTextField.setPromptText(FRIEND_NOT_FOUND);
            }
        }
    }

    public void sendMessage() {
    }

    public void clickSearchField() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER).subscribe(users -> {
            allUsers = users;
            List<String> names = new ArrayList<>();
            for (User user : allUsers) {
                names.add(user.name());
            }
            AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(searchTextField, names);
            autoCompletionBinding.setPrefWidth(searchTextField.getPrefWidth());
        }));
    }
}
