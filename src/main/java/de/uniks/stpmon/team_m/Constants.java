package de.uniks.stpmon.team_m;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;

public class Constants {
    // SERVER
    public static final String API_URL = "https://stpmon.uniks.de/api/v1";
    public static final String WS_URL = "wss://stpmon.uniks.de/ws/v1";
    public static final String MESSAGE_NAMESPACE_GROUPS = "groups";
    public static final String MESSAGE_NAMESPACE_REGIONS = "regions";
    public static final String MESSAGE_NAMESPACE_GLOBAL = "global";

    // GENERAL
    public static final String GAME_NAME = "Monster Odyssey";
    public static final int STANDARD_HEIGHT = 480;
    public static final int STANDARD_WIDTH = 640;
    public static final String APP_ICON = "images/icon.png";
    public static final String TASKBAR_ICON = "images/icon.png";
    public static final String LOADING = "Loading...";
    public static final String EMPTY_STRING = "";
    public static final String FX_STYLE_BORDER_COLOR_BLACK = "-fx-border-color: black";
    public static final String LOADING_ANIMATION = "images/loading.gif";
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);
    public static final String USER_STATUS_OFFLINE = "offline";
    public static final String USER_STATUS_ONLINE = "online";

    // LOGIN & SIGNUP
    public static final String LOGIN_TITLE = "Sign Up & In";
    public static final String PASSWORD_LESS_THAN_8_CHARACTERS = "Password must have at least 8 characters.";
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final char BULLET = '\u25cf';
    public static final String REFRESH_TOKEN_PREF = "refreshToken";
    public static final String DELETE_SUCCESS = "Account successfully deleted";

    // MAIN MENU
    public static final String MAIN_MENU_TITLE = "Main Menu";
    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_OFFLINE = "offline";

    // MESSAGES
    public static final int MESSAGES_HEIGHT = 640;
    public static final int MESSAGES_WIDTH = 840;
    public static final String MESSAGES_TITLE = "Messages";
    public static final int MESSAGES_FRIEND_NODE_HEIGHT = 50;
    public static final Insets MESSAGES_FRIEND_NODE_PADDING = new Insets(9, 0, 11, 0);
    public static final int MESSAGES_FRIEND_NODE_STATUS_RADIUS = 15;

    // NEW FRIEND
    public static final String NEW_FRIEND_TITLE = "Add a new friend";
    public static final String FRIEND_ADDED = "Friend added. You can add more friends or go back to the main menu.";

    // GROUPS
    public static final String NEW_GROUP_TITLE = "New Group";
    public static final String EDIT_GROUP_TITLE = "Edit Group";
    public static final String CHANGE_GROUP = "Change Group Name";

    // ACCOUNT SETTINGS
    public static final String ACCOUNT_SETTINGS_TITLE = "Account Setting";
    public static final String USERNAME_SUCCESS_CHANGED = "Your username has been changed successfully.";
    public static final String PASSWORD_SUCCESS_CHANGED = "Your Password has been changed successfully.";

    // INGAME
    public static final String INGAME_TITLE = "Ingame";
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    public static final String HELP_LABEL = "Click 'p' on your keyboard for pause menu.";
    public static final String RESUME_BUTTON_LABEL = "Resume Game";
    public static final String SAVE_GAME_AND_LEAVE_BUTTON_LABEL = "Save Game & Leave";
    public static final String PAUSE_MENU_TITLE = "Pause Menu";
    public static final String PAUSE_MENU_LABEL = "What do you want to do?";
}
