package de.uniks.stpmon.team_m;

import javafx.geometry.Insets;

public class Constants {
    // SERVER
    public static final String API_URL = "https://stpmon.uniks.de/api/v3";
    public static final String WS_URL = "wss://stpmon.uniks.de/ws/v3";
    public static final String UDP_URL = "stpmon.uniks.de";
    public static final int SERVER_PORT = 30013;
    public static final String MESSAGE_NAMESPACE_GROUPS = "groups";
    public static final String MESSAGE_NAMESPACE_REGIONS = "regions";
    public static final String MESSAGE_NAMESPACE_GLOBAL = "global";
    // GENERAL
    public static final String GAME_NAME = "Monster Odyssey";
    public static final int STANDARD_HEIGHT = 768;
    public static final int STANDARD_WIDTH = 1024;
    public static final int MINIMUM_HEIGHT = 600;
    public static final int MINIMUM_WIDTH = 800;
    public static final int OFFSET_WIDTH_HEIGHT = 50;
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
    public static final String DATE_TIME_FORMAT = "HH:mm, dd.MM.yy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final int DURATION_OF_LOADING_SCREEN = 3;

    // LOGIN & SIGNUP
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final char BULLET = '\u25cf';
    public static final String REFRESH_TOKEN_PREF = "refreshToken";


    // NEW GROUP
    public static final String CHECK_MARK = "\u2713";
    public static final String ADD_MARK = "+";
    public static final int BUTTON_PREF_SIZE = 42;
    public static final int MAX_SUGGESTIONS_NEW_GROUP = 20;


    // MESSAGES
    public static final String PENCIL = "\u270F";

    // AVATAR
    public static final String AVATAR_1 = "images/monster.png";
    public static final String AVATAR_2 = "images/rabbit.png";
    public static final String AVATAR_3 = "images/sheep_icon.png";
    public static final String AVATAR_4 = "images/cat.jpeg";
    public static final int MAX_BASE64_LENGTH = 16000;
    public static final String IMAGE_PROCESSING_ERROR = "Image could not be processed. Please try again.";
    public static final int AVATAR_SIZE = 30;

    // INGAME
    public static int TILE_SIZE = 16;
    public static double ZERO = 0;
    public static double ONE = 1;

        // NPC DIALOG
    public static int dialogVBoxWidth = 700;
    public static int getDialogVBoxHeight = 150;
    public static int spacerToBottomOfScreen = 150;
    public static Insets dialogTextFlowInsets = new Insets(20, 40, 20, 40);
    public static Insets helpLabelInsets = new Insets(0, 40, 20, 0);
    public static int helpLabelFontSize = 12;
    public enum DialogSpecialInteractions {
        nurseYes,
        nurseNo,
        starterSelection0,
        starterSelection1,
        starterSelection2
    }

    // Error messages
    public static final String HTTP_400 = "400";
    public static final String HTTP_401 = "401";
    public static final String HTTP_403 = "403";
    public static final String HTTP_404 = "404";
    public static final String HTTP_409 = "409";
    public static final String HTTP_429 = "429";

    // Welcome Scene
    public static final int MESSAGEBOX_HEIGHT = 45;
    public static final int MESSAGEBOX_WIDTH = 170;

    // Characters
    public static final String[] PREMADE_CHARACTERS = {
            "Premade_Character_01.png", "Premade_Character_02.png", "Premade_Character_03.png", "Premade_Character_04.png",
            "Premade_Character_05.png", "Premade_Character_06.png", "Premade_Character_07.png", "Premade_Character_08.png",
            "Premade_Character_09.png", "Premade_Character_10.png", "Premade_Character_11.png", "Premade_Character_12.png",
            "Premade_Character_13.png", "Premade_Character_14.png", "Premade_Character_15.png", "Premade_Character_16.png",
            "Premade_Character_17.png", "Premade_Character_18.png", "Premade_Character_19.png", "Premade_Character_20.png"
    };
}