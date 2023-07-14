package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.EMPTY_STRING;
import static de.uniks.stpmon.team_m.Constants.HTTP_403;

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
    UsersService usersService;
    @Inject
    GroupService groupService;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<MessagesController> messageControllerProvider;

    /**
     * NewFriendController handles the adding of new friends.
     */

    @Inject
    public NewFriendController() {
    }

    /**
     * This method sets the title of the controller.
     *
     * @return Title of the controller.
     */

    @Override
    public String getTitle() {
        return resources.getString("NEW.FRIEND.TITLE");
    }

    /**
     * This method enables the user to navigate to the main menu.
     */

    public void changeToMainMenu() {
        groupStorageProvider.get().set_id(null);
        app.show(mainMenuControllerProvider.get());
    }

    /**
     * This method adds the functionality to the button to add friends and handles edge cases.
     */

    public void addAsAFriend() {
        if (searchTextField.getText().equals(EMPTY_STRING)) {
            return;
        }
        if (allUsers.isEmpty()) {
            return;
        }
        if (searchTextField.getText().equals(userStorageProvider.get().getName())) {
            searchTextField.setPromptText(resources.getString("YOURSELF"));
            return;
        }
        for (User user : allUsers) {
            if (user.name().equals(searchTextField.getText())) {
                if (userStorageProvider.get().getFriends().contains(user._id())) {
                    searchTextField.setPromptText(resources.getString("FRIEND.ALREADY.ADDED"));
                    searchTextField.clear();
                    return;
                }
                userStorageProvider.get().addFriend(user._id());
                disposables.add(usersService.updateUser(null, null, null, userStorageProvider.get().getFriends(), null).observeOn(FX_SCHEDULER).subscribe(user1 -> {
                    createPrivateGroup(user, false);
                    searchTextField.setPromptText(resources.getString("FRIEND.ADDED"));
                }, error -> showError(error.getMessage())));
                break;
            }
        }
        searchTextField.clear();
    }

    /**
     * This method adds the functionality to the button to send a message to the found user.
     */

    public void sendMessage() {
        for (User user : allUsers) {
            if (!user.name().equals(searchTextField.getText())) {
                searchTextField.setPromptText(resources.getString("FRIEND.NOT.FOUND"));
            }
            if (user.name().equals(searchTextField.getText())) {
                createPrivateGroup(user, true);
                break;
            }
        }
        searchTextField.clear();
    }

    /**
     * This method adds the AutoCompletion functionality to the search field.
     */

    public void clickSearchField() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER).subscribe(users -> {
            allUsers = users;
            List<String> names = new ArrayList<>();
            for (User user : allUsers) {
                names.add(user.name());
            }
            AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(searchTextField, names);
            autoCompletionBinding.prefWidthProperty().bind(searchTextField.widthProperty());
        }, error -> showError(error.getMessage())));
    }

    /**
     * This method creates a private group between the user and the found user. If the user was a friend, no new group is created.
     * If the user was not a friend, a new group is created if sendMessage method was called. At the end it switches to the message screen.
     *
     * @param user         User to create a private group with.
     * @param switchScreen Boolean to switch to the message screen.
     */

    private void createPrivateGroup(User user, boolean switchScreen) {
        groupStorageProvider.get().set_id(null);
        if (switchScreen) {
            if (userStorageProvider.get().getFriends().contains(user._id())) {
                groupStorageProvider.get().setName(user.name());
                groupStorageProvider.get().set_id(user._id());
                MessagesController messagesController = messageControllerProvider.get();
                messagesController.setUserChosenFromNewFriend(true);
                app.show(messagesController);
                return;
            }
        }
        Group privateGroup = new Group(null, null, List.of(user._id(), userStorageProvider.get().get_id()));
        disposables.add(groupService.getGroups(privateGroup.membersToString()).observeOn(FX_SCHEDULER).subscribe(groups -> {
            if (groups.isEmpty()) throw new RuntimeException(HTTP_403);
            for (Group group : groups) {
                if (group.members().contains(user._id()) && group.members().contains(userStorageProvider.get().get_id())
                        && group.name() == null) {
                    setGroupIDAndSwitchScreen(user, switchScreen, group);
                    return;
                }
            }
            throw new RuntimeException(HTTP_403);
        }, error -> {
            if (error.getMessage().contains(HTTP_403)) {
                disposables.add(groupService.create(privateGroup.name(), privateGroup.members())
                        .observeOn(FX_SCHEDULER)
                        .subscribe(group -> setGroupIDAndSwitchScreen(user, switchScreen, group),
                                error1 -> showError(error1.getMessage())));
            }
        }));
    }

    /**
     * This method sets the group ID and switches to the message screen.
     *
     * @param user         User to create a private group with.
     * @param switchScreen Boolean to switch to the message screen.
     * @param group        Group to set the ID.
     */

    private void setGroupIDAndSwitchScreen(User user, boolean switchScreen, Group group) {
        if (switchScreen) {
            groupStorageProvider.get().setName(user.name());
            groupStorageProvider.get().set_id(group._id());
            MessagesController messagesController = messageControllerProvider.get();
            messagesController.setUserChosenFromNewFriend(true);
            app.show(messagesController);
        }
    }
}
