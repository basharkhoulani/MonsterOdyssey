package de.uniks.stpmon.team_m;


public class Constants {
    // SERVER
    public static final String API_URL = "https://stpmon.uniks.de/api/v2";
    public static final String WS_URL = "wss://stpmon.uniks.de/ws/v2";
    public static final String MESSAGE_NAMESPACE_GROUPS = "groups";
    public static final String MESSAGE_NAMESPACE_REGIONS = "regions";
    public static final String MESSAGE_NAMESPACE_GLOBAL = "global";
    public static final String GENERIC_ERROR = """
            Something went wrong!
            Please try again later!""";
    public static final String ERROR = "Error";

    // GENERAL
    public static final String GAME_NAME = "Monster Odyssey";
    public static final int STANDARD_HEIGHT = 600;
    public static final int STANDARD_WIDTH = 800;
    public static final int MINIMUM_HEIGHT = 590; // 540
    public static final int MINIMUM_WIDTH = 750; // 700
    public static final String APP_ICON = "images/Monster-durchsichtig.png";
    public static final String TASKBAR_ICON = "images/Monster-durchsichtig.png";
    public static final String SURE = "Are you sure?";
    public static final String EMPTY_STRING = "";
    public static final String FX_STYLE_BORDER_COLOR_BLACK = "-fx-border-color: black";
    public static final String LOADING_ANIMATION = "images/loading.gif";
    public static final String ONLINE_IMG = "images/online.png";
    public static final String OFFLINE_IMG = "images/offline.png";
    public static final String ONLINE_STAR = "images/online_star.png";
    public static final String OFFLINE_STAR = "images/offline_star.png";
    public static final String USER_STATUS_OFFLINE = "offline";
    public static final String USER_STATUS_ONLINE = "online";
    public static final int HBOX_FRIENDS_SPACING = 15;
    public static final String THREE_DOTS = "\u2026";
    public static final String BUTTON_TRANSPARENT_STYLE = "-fx-background-color: transparent; -fx-border-color: transparent;";
    public static final String BUTTON_BORDER_STYLE = "-fx-border-color: black; -fx-border-width: 1px;";
    public static final String NO_FRIENDS_FOUND = "No friends found";
    public static final String NO_GROUPS_FOUND = "No groups found";
    public static final String NO_USERS_ADDED_TO_GROUP = "No foreign users added to group yet";
    public static final String NO_MESSAGES_YET = "\t\tNo messages yet\nor there is no user or group selected";
    public static final String BEST_FRIEND_PREF = "bestFriend";
    public static final String DELETE_FRIEND_WARNING = "Are you sure you want to delete this friend?";
    public static final String LIST_VIEW_STYLE_NO_INDEXING = "-fx-background-color: #F4F4F4;";
    public static final String OWN_MESSAGE_STYLE = "-fx-background-color: lightblue;";
    public static final String NOT_OWN_MESSAGE_STYLE = "-fx-background-color: lightgreen;";
    public static final String ROUNDED_CORNERS_STYLE = "-fx-border-radius: 10; -fx-background-radius: 10;";
    public static final String BORDER_COLOR_BLACK = "-fx-border-color: black;";
    public static final String ZONE_ID_EUROPE_BERLIN = "Europe/Berlin";
    public static final String DATE_TIME_FORMAT = "HH:mm, dd.MM.yy";
    public static final int DURATION_OF_LOADING_SCREEN = 3;
    public static final String CANCEL = "Cancel";
    public static final String OK = "OK";

    // LOGIN & SIGNUP
    public static final String LOGIN_TITLE = "Sign Up & In";
    public static final String PASSWORD_LESS_THAN_8_CHARACTERS = "Password must have at least 8 characters.";
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final char BULLET = '\u25cf';
    public static final String REFRESH_TOKEN_PREF = "refreshToken";
    public static final String DELETE_SUCCESS = "Account successfully deleted";
    public static final String CUSTOM_ERROR = "Something went terribly wrong!";
    public static final String SIGNIN_ERROR = "Wrong Password or Username! Try again!";
    public static final String USERNAME_TAKEN = "Username is already taken!";

    //Change Language Pop up
    public static final String CHANGE_LANGUAGE_TITLE = "Change Language";

    // MAIN MENU
    public static final String MAIN_MENU_TITLE = "Main Menu";

    // NEW GROUP
    public static final String CHECK_MARK = "\u2713";
    public static final String ADD_MARK = "+";
    public static final int BUTTON_PREF_SIZE = 30;
    public static final int MAX_SUGGESTIONS_NEW_GROUP = 20;


    // MESSAGES
    public static final String MESSAGES_TITLE = "Messages";
    public static final String PENCIL = "\u270F";
    public static final String EDIT_MESSAGE_TITLE = "Edit message";
    public static final String EDIT_MESSAGE_CONTENT = "Message:";
    public static final String DELETE_MESSAGE_TITLE = "Delete Message";
    public static final String DELETE_MESSAGE_CONTENT = "Are you sure you want to delete this message?";
    public static final String ALONE = "Alone";

    // NEW FRIEND
    public static final String NEW_FRIEND_TITLE = "Add a new friend";
    public static final String FRIEND_ADDED = "Friend added. You can add more friends or go back to the main menu.";
    public static final String FRIEND_NOT_FOUND = "Friend not found. Please try again.";
    public static final String YOURSELF = "You can't add yourself as a friend.";
    public static final String FRIEND_ALREADY_ADDED = "You are already friends with this user.";

    // GROUPS
    public static final String NEW_GROUP_TITLE = "New Group";
    public static final String EDIT_GROUP_TITLE = "Edit Group";
    public static final String CHANGE_GROUP = "Change Group Name";
    public static final String DELETE_WARNING = "Are you sure you want to delete this group?";


    // ACCOUNT SETTINGS
    public static final String ACCOUNT_SETTINGS_TITLE = "Account Setting";
    public static final String USERNAME_SUCCESS_CHANGED = "Your username has been changed successfully.";
    public static final String PASSWORD_SUCCESS_CHANGED = "Your Password has been changed successfully.";
    public static final String SELECT_AVATAR_PICTURE = "Select Avatar Picture";
    public static final String IMAGE = "Image";
    public static final String AVATAR_SUCCESS_CHANGED = "Your Avatar has been changed successfully.";

    // AVATAR
    public static final String AVATAR_1 = "images/Monster1-color.png";
    public static final String AVATAR_2 = "images/Monster2-color.png";
    public static final String AVATAR_3 = "images/rabbit.png";
    public static final String AVATAR_4 = "images/sheep_icon.png";
    public static final int MAX_BASE64_LENGTH = 16000;
    public static final String IMAGE_PROCESSING_ONGOING = "Image is being processed. Please wait.";
    public static final String IMAGE_PROCESSING_ERROR = "Image could not be processed. Please try again.";

    // INGAME
    public static final String INGAME_TITLE = "Ingame";
    public static final String FIRST_HELP = "Click 'p' on your keyboard for pause menu.\n";
    public static final String SECOND_HELP = "     \u2191 \n \u2190 \u2193 \u2192  to move your character. \n";
    public static final String THIRD_HELP = "Click Space for interact and attack.";
    public static final String HELP_LABEL = FIRST_HELP + SECOND_HELP + THIRD_HELP;
    public static final String RESUME_BUTTON_LABEL = "Resume Game";
    public static final String SAVE_GAME_AND_LEAVE_BUTTON_LABEL = "Save Game & Leave";
    public static final String PAUSE_MENU_TITLE = "Pause Menu";
    public static final String PAUSE_MENU_LABEL = "What do you want to do?";

    // Error messages
    public static final String HTTP_400 = "400";
    public static final String HTTP_400_MESSAGE = "Bad request. Please try again later.";
    public static final String HTTP_401 = "401";
    public static final String HTTP_401_MESSAGE = "Unauthorized. Please try again later.";
    public static final String HTTP_403 = "403";
    public static final String HTTP_403_MESSAGE = "Forbidden. Please try again later.";
    public static final String HTTP_404 = "404";
    public static final String HTTP_404_MESSAGE = "Not found. Please try again later.";
    public static final String HTTP_409 = "409";
    public static final String HTTP_409_MESSAGE = "Conflict. Please try again later.";
    public static final String HTTP_429 = "429";
    public static final String HTTP_429_MESSAGE = "Too many requests. Please try again later.";
    public static final String UNKNOWN_USER = "Unknown";

    // Welcome Scene
    public static final String FIRST_MESSAGE = "Welcome to Monster Odyssey!";
    public static final String SECOND_MESSAGE = "Welcome Aboard.";
    public static final String THIRD_MESSAGE = "We are the crew of this ship.";
    public static final String FOURTH_MESSAGE = "My name is James.";
    public static final String FIFTH_MESSAGE = "And my name is Henry!";
    public static final String SIXTH_MESSAGE = "Now then, we'll need to look up your application.";
    public static final String SEVENTH_MESSAGE = "Can we have your name?";
    public static final String EIGHTH_MESSAGE = "Hello. Next, we 'd like to take a picture of you";
    public static final String NINTH_MESSAGE = "Take as much time as you want to get dressed up for it.";
    public static final String DELETE_TRAINER_ALERT = "Delete Trainer";
    public static final String WELCOME_MESSAGE_STYLE = "welcomeMessage";
    public static final String WELCOME_SCENE_BUTTON = "welcomeSceneButton";
    public static final String WHITE_BUTTON = "whiteButton";
    public static final String ALERT_DIALOG_NAME = "alertDialogName";
    public static final String NAME_ALERT_TITLE = "Name?";

    public static final int MESSAGEBOX_HEIGHT = 45;
    public static final int MESSAGEBOX_WIDTH = 170;

}