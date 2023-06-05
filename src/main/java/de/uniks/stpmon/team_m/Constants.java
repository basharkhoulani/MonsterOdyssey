package de.uniks.stpmon.team_m;


import javafx.scene.input.KeyCode;

public class Constants {
    // SERVER
    public static final String API_URL = "https://stpmon.uniks.de/api/v2";
    public static final String WS_URL = "wss://stpmon.uniks.de/ws/v2";
    public static final String MESSAGE_NAMESPACE_GROUPS = "groups";
    public static final String MESSAGE_NAMESPACE_REGIONS = "regions";
    public static final String MESSAGE_NAMESPACE_GLOBAL = "global";

    // GENERAL
    public static final String GAME_NAME = "Monster Odyssey";
    public static final int STANDARD_HEIGHT = 600;
    public static final int STANDARD_WIDTH = 800;
    public static final int MINIMUM_HEIGHT = 590; // 540
    public static final int MINIMUM_WIDTH = 750; // 700
    public static final String APP_ICON = "images/Monster-durchsichtig.png";
    public static final String TASKBAR_ICON = "images/Monster-durchsichtig.png";
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
    public static final String BEST_FRIEND_PREF = "bestFriend";
    public static final String LIST_VIEW_STYLE_NO_INDEXING = "-fx-background-color: #F4F4F4;";
    public static final String OWN_MESSAGE_STYLE = "-fx-background-color: lightblue;";
    public static final String NOT_OWN_MESSAGE_STYLE = "-fx-background-color: lightgreen;";
    public static final String ROUNDED_CORNERS_STYLE = "-fx-border-radius: 10; -fx-background-radius: 10;";
    public static final String BORDER_COLOR_BLACK = "-fx-border-color: black;";
    public static final String ZONE_ID_EUROPE_BERLIN = "Europe/Berlin";
    public static final String DATE_TIME_FORMAT = "HH:mm, dd.MM.yy";
    public static final int DURATION_OF_LOADING_SCREEN = 3;
    public static final String OK = "OK";

    // LOGIN & SIGNUP
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final char BULLET = '\u25cf';
    public static final String REFRESH_TOKEN_PREF = "refreshToken";


    // NEW GROUP
    public static final String CHECK_MARK = "\u2713";
    public static final String ADD_MARK = "+";
    public static final int BUTTON_PREF_SIZE = 40;
    public static final int MAX_SUGGESTIONS_NEW_GROUP = 20;


    // MESSAGES
    public static final String PENCIL = "\u270F";
    public static final String EDIT_MESSAGE_TITLE = "Edit message";
    public static final String EDIT_MESSAGE_CONTENT = "Message:";
    public static final String DELETE_MESSAGE_TITLE = "Delete Message";
    public static final String DELETE_MESSAGE_CONTENT = "Are you sure you want to delete this message?";

    // NEW FRIEND
    public static final String FRIEND_NOT_FOUND = "Friend not found. Please try again.";


    // ACCOUNT SETTINGS
    public static final String USERNAME_SUCCESS_CHANGED = "Your username has been changed successfully.";
    public static final String PASSWORD_SUCCESS_CHANGED = "Your Password has been changed successfully.";

    // AVATAR
    public static final String AVATAR_1 = "images/monster.png";
    public static final String AVATAR_2 = "images/rabbit.png";
    public static final String AVATAR_3 = "images/sheep_icon.png";
    public static final String AVATAR_4 = "images/cat.jpeg";
    public static final int MAX_BASE64_LENGTH = 16000;
    public static final String IMAGE_PROCESSING_ONGOING = "Image is being processed. Please wait.";
    public static final String IMAGE_PROCESSING_ERROR = "Image could not be processed. Please try again.";
    public static final int AVATAR_SIZE = 30;

    // INGAME
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    public static final String INGAME_TITLE = "Ingame";
    public static final String FIRST_HELP = "Click 'p' on your keyboard for pause menu.\n";
    public static final String SECOND_HELP = "     \u2191 \n \u2190 \u2193 \u2192  to move your character. \n";
    public static final String THIRD_HELP = "Click Space for interact and attack.";
    public static final String HELP_LABEL = FIRST_HELP + SECOND_HELP + THIRD_HELP;
    public static final String RESUME_BUTTON_LABEL = "Resume Game";
    public static final String SAVE_GAME_AND_LEAVE_BUTTON_LABEL = "Save Game & Leave";
    public static final String PAUSE_MENU_TITLE = "Pause Menu";
    public static final String PAUSE_MENU_LABEL = "What do you want to do?";
    public static final String DELETE_TRAINER_TEXT = "Delete ";
    public static final String DELETE_TRAINER_SUCCESS = "Trainer Account delete successfully";

    // Error messages
    public static final String HTTP_400 = "400";
    public static final String HTTP_401 = "401";
    public static final String HTTP_403 = "403";
    public static final String HTTP_404 = "404";
    public static final String HTTP_409 = "409";
    public static final String HTTP_429 = "429";
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
    public static final String TENTH_MESSAGE = "Now it's time to start your Journey";
    public static final String ELEVENTH_MESSAGE = "See you next time!";


    public static final String DELETE_TRAINER_ALERT = "Delete Trainer";
    public static final String WELCOME_MESSAGE_STYLE = "welcomeMessage";
    public static final String WELCOME_SCENE_BUTTON = "welcomeSceneButton";
    public static final String WHITE_BUTTON = "whiteButton";
    public static final String ALERT_DIALOG_NAME = "alertDialogName";
    public static final String NAME_ALERT_TITLE = "Name?";

    public static final int MESSAGEBOX_HEIGHT = 45;
    public static final int MESSAGEBOX_WIDTH = 170;

    // Trainer

    public static final String PREMADE_CHARACTER_1 = "Premade_Character_01.png";
    public static final String PREMADE_CHARACTER_2 = "Premade_Character_04.png";
    public static final String CHARACTER_1 = "images/Character1.png";
    public static final String CHARACTER_2 = "images/Character4.png";

}