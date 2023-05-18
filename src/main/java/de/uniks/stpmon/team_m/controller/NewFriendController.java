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
        groupStorageProvider.get().set_id(null);
        groupStorageProvider.get().set_id(null);
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
                createPrivateGroup(user);
                searchTextField.setPromptText(FRIEND_ADDED);
                break;
            }
        }
        searchTextField.clear();
    }

    public void sendMessage() {
        for (User user : allUsers) {
            System.out.println(user.name());
            if (!user.name().equals(searchTextField.getText())) {
                searchTextField.setPromptText(FRIEND_NOT_FOUND);
            }
            if (user.name().equals(searchTextField.getText())) {
                createPrivateGroup(user);
                app.show(messageControllerProvider.get());
                break;
            }
        }
        searchTextField.clear();
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
    private void createPrivateGroup(User user) {
        Group privateGroup = new Group(null, null, List.of(user._id(), userStorageProvider.get().get_id()));
        disposables.add(groupServiceProvider.get().getGroups(privateGroup.membersToString()).observeOn(FX_SCHEDULER).subscribe(groups -> {
            for (Group group : groups) {
                if (group.members() == null) {
                    break;
                }
                if (group.members().size() == 2 && group.name() == null) {
                    groupStorageProvider.get().set_id(user._id());
                    groupStorageProvider.get().setName(user.name());
                    break;
                }
            }
            if (groupStorageProvider.get().get_id() == null) {
                disposables.add(groupServiceProvider.get().create(null, privateGroup.members())
                        .observeOn(FX_SCHEDULER).subscribe(group -> System.out.println(group._id())));
            }
        }));
    }
}
