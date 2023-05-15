package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
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
import java.util.Arrays;
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
    Provider<UsersService> usersServiceProvider;
    @Inject
    Provider<GroupService> groupServiceProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    Provider<MessagesController> messageControllerProvider;

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
        if (searchTextField.getText().equals(userStorageProvider.get().getName())) {
            searchTextField.setPromptText(YOURSELF);
            return;
        }
        searchTextField.setPromptText(FRIEND_NOT_FOUND);
        for (User user : allUsers) {
            if (user.name().equals(searchTextField.getText())) {
                userStorageProvider.get().addFriend(user._id());
                disposables.add(usersServiceProvider.get().updateUser(null, null, null, userStorageProvider.get().getFriends(), null).observeOn(FX_SCHEDULER).subscribe());
                searchTextField.setPromptText(FRIEND_ADDED);
                break;
            }
        }
        searchTextField.clear();
    }

    public void sendMessage() {
        searchTextField.clear();
        for (User user : allUsers) {
            if (!user.name().equals(searchTextField.getText())) {
                searchTextField.setPromptText(FRIEND_NOT_FOUND);
                return;
            }
            List<String> privateGroup = Arrays.asList(user._id(), userStorageProvider.get().get_id());
            disposables.add(groupServiceProvider.get().getGroups(privateGroup).observeOn(FX_SCHEDULER).subscribe(groups -> {
                for (Group group : groups) {
                    if (group.members().size() == privateGroup.size() && group.name().isEmpty()) {
                        groupStorageProvider.get().set_id(group._id());
                    } else {
                        disposables.add(groupServiceProvider.get().create(null, privateGroup).observeOn(FX_SCHEDULER).subscribe(newGroup -> groupStorageProvider.get().set_id(newGroup._id())));
                    }
                    app.show(messageControllerProvider.get());
                }
            }));
        }
    }

    public void clickSearchField() {
        disposables.add(usersServiceProvider.get().getUsers(null, null).observeOn(FX_SCHEDULER).subscribe(users -> {
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
