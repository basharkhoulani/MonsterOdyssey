package de.uniks.stpmon.team_m;

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
    public static final String FX_STYLE_BLACK = "-fx-border-color: black";
    public static final String USER_STATUS_OFFLINE = "offline";
    public static final String USER_STATUS_ONLINE = "online";

    // LOGIN & SIGNUP
    public static final String LOGIN_TITLE = "Sign Up & In";
    public static final String PASSWORD_LESS_THAN_8_CHARACTERS = "Password must have at least 8 characters.";
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final char BULLET = '\u25cf';
    public static final String REFRESH_TOKEN_PREF = "refreshToken";

    // MAIN MENU
    public static final String MAIN_MENU_TITLE = "Main Menu";

    // MESSAGES
    public static final int MESSAGES_HEIGHT = 640;
    public static final int MESSAGES_WIDTH = 840;
    public static final String MESSAGES_TITLE = "Messages";
    public static final int MESSAGES_FRIEND_NODE_HEIGHT = 50;
    public static final Insets MESSAGES_FRIEND_NODE_PADDING = new Insets(9, 0, 11, 0);
    public static final int MESSAGES_FRIEND_NODE_STATUS_RADIUS = 15;

    // NEW FRIEND
    public static final String NEW_FRIEND_TITLE = "Add a new friend";

    // GROUPS
    public static final String NEW_GROUP_TITLE = "New Group";
    public static final String EDIT_GROUP_TITLE = "Edit Group";
    public static final String CHANGE_GROUP = "Change Group Name";

    // ACCOUNT SETTINGS
    public static final String ACCOUNT_SETTINGS_TITLE = "Account Setting";

    // INGAME
    public static final String INGAME_TITLE = "Ingame";
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    public static final String HELP_LABEL = "Click 'p' on your keyboard for pause menu.";
    public static final String RESUME_BUTTON_LABEL = "Resume Game";
    public static final String SAVE_GAME_AND_LEAVE_BUTTON_LABEL = "Save Game & Leave";
    public static final String PAUSE_MENU_TITLE = "Pause Menu";
    public static final String PAUSE_MENU_LABEL = "What do you want to do?";
}
