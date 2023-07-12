package de.uniks.stpmon.team_m;

import javafx.geometry.Insets;

import java.util.*;

import static java.util.Map.entry;

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
    public static final String ZONE_ID_EUROPE_BERLIN = "Europe/Berlin";

    // AVATAR
    public static final String AVATAR_1 = "images/monster.png";
    public static final String AVATAR_2 = "images/rabbit.png";
    public static final String AVATAR_3 = "images/sheep_icon.png";
    public static final String AVATAR_4 = "images/cat.jpeg";
    public static final int MAX_BASE64_LENGTH = 16000;
    public static final String IMAGE_PROCESSING_ERROR = "Image could not be processed. Please try again.";
    public static final int AVATAR_SIZE = 30;

    // INGAME
    public static final int TILE_SIZE = 16;
    public static final int TRANSITION_DURATION = 300;
    public static final int SCALE_FACTOR = 2;
    public static final int DELAY = 100;
    public static final int DELAY_LONG = 500;
    public static final double ZERO = 0;
    public static final double ONE = 1;

    public static final String SETTINGSYMBOL2 = "images/SettingSymbol2.png";
    public static final String GOBACKSYMBOL = "images/GoBackSymbol.PNG";
    public static final String PLAYSYMBOL = "images/PlaySymbol.PNG";
    public static final String AUDIOSYMBOL = "images/AudioSymbol.png";
    public static final String KEYBINDINGSSYMBOL = "images/KeybindingsSymbol.png";
    public static final String CHECKSYMBOL = "images/checkSymbol.png";
    public static final String WARNING = "images/Warning.png";
    public static final String PENCIL2SYMBOL = "images/Pencil2.png";
    public static final String ARROWLEFTSYMBOL = "images/arrowLeft.png";
    public static final String ARROWRIGHTSYMBOL = "images/arrowRight.png";

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
        nurseNoMons,
        starterSelection0,
        starterSelection1,
        starterSelection2
    }

    public enum ContinueDialogReturnValues {
        dialogNotFinished,
        dialogFinishedTalkToTrainer,
        dialogFinishedNoTalkToTrainer,
        albertDialogFinished0,
        albertDialogFinished1,
        albertDialogFinished2,
        spokenToNurse,
        encounterOnTalk
    }

    // Nurse Popup
    public static int popupHeight = 170;
    public static int popupWidth = 300;
    public static int textFieldHeight = 120;
    public static int buttonsHBoxHeight = 50;
    public static int buttonsHBoxSpacing = 30;
    public static int nurseButtonHeight = 40;
    public static int nurseButtonWidth = 80;

    // Ingame Help
    public static int spaceBetweenPhoneAndWindowEdge = 25;
    public static int offsetToNotShowPhoneInScreen = 30;
    public static final String smallHandyImage = "images/SmallHandy.png";
    public static final String notificationBellImage = "images/NotificationBell.png";

    // Encounter
    public static int fleePopupWidth = 300;
    public static int fleePopupHeight = 200;
    public static int fleeTextHeight = 150;
    public static Insets fleeTextInsets = new Insets(20, 10, 0, 10);
    public static int fleeButtonsHBoxHeight = 50;
    public static Insets fleeButtonsHBoxInsets = new Insets(0, 10, 10, 10);
    public static int fleeButtonWidth = 100;
    public static int fleeButtonHeight = 40;

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

    // Change Audio Settings

    public static final int DIALOG_WIDTH = 450;
    public static final int DIALOG_HEIGHT = 300;

    // Characters
    public static final String[] PREMADE_CHARACTERS = {
            "Premade_Character_01.png", "Premade_Character_02.png", "Premade_Character_03.png", "Premade_Character_04.png",
            "Premade_Character_05.png", "Premade_Character_06.png", "Premade_Character_07.png", "Premade_Character_08.png",
            "Premade_Character_09.png", "Premade_Character_10.png", "Premade_Character_11.png", "Premade_Character_12.png",
            "Premade_Character_13.png", "Premade_Character_14.png", "Premade_Character_15.png", "Premade_Character_16.png",
            "Premade_Character_17.png", "Premade_Character_18.png", "Premade_Character_19.png", "Premade_Character_20.png"
    };

    // Sounds
    public static final String MENU_SOUND = "Nintendo_Style_Funny_Music_Loop_01.wav";
    public static final String WELCOME_SOUND = "Nintendo_Style_Quirky_Music_Loop_01.wav";
    public static final String CITY_SOUND = "Nintendo_Style_Upbeat_Music_Loop_03.wav";
    public static final String ROOMS_SOUND = "Nintendo_Style_Funny_Music_Loop_02.wav";
    public static final String ROUTE_SOUND = "Nintendo_Style_Upbeat_Music_Loop_05.wav";
    public static final String FIGHT_SOUND = "Nintendo_Style_Battle_Music_Loop_02.wav";

    // Colors
    public static HashMap<String, String> TYPESCOLORPALETTE = new HashMap<>(Map.ofEntries(
            entry("normal", "#BBBBAA"),
            entry("fire", "#FF421C"),
            entry("water", "#2C9BE3"),
            entry("electric", "#FFDC00"),
            entry("grass", "#62BC5A"),
            entry("flying", "#96CAFF"),
            entry("bug", "#92C12A"),
            entry("poison", "#9553CD"),
            entry("rock", "#BBAA66"),
            entry("ground", "#A67439"),
            entry("fighting", "#BB5544"),
            entry("ice", "#74CFC0"),
            entry("psychic", "#FF6380"),
            entry("ghost", "#6E4370"),
            entry("dragon", "#5670BE"),
            entry("dark", "#353225"),
            entry("steel", "#97A8AA"),
            entry("fairy", "#D15F5F")
    ));

    // Abilities
    public static HashMap<String, String> ABILITYPALETTE = new HashMap<>(Map.ofEntries(
            entry("normal", "ability-normal.png"),
            entry("fire", "ability-fire.png"),
            entry("water", "ability-water.png"),
            entry("electric", "ability-electic.png"),
            entry("grass", "ability-grass.png"),
            entry("flying", "ability-flying.png"),
            entry("bug", "ability-bug"),
            entry("poison", "ability-poison"),
            entry("rock", "ability-rock"),
            entry("ground", "ability-ground"),
            entry("fighting", "ability-fighting"),
            entry("ice", "ability-ice"),
            entry("psychic", "ability-psychic"),
            entry("ghost", "ability-ghost"),
            entry("dragon", "ability-dragon"),
            entry("dark", "ability-dragon"),
            entry("steel", "ability-steel"),
            entry("fairy", "ability-fairy")
    ));

    public static List<String> ATTRIBUTE_IMAGES = Arrays.asList(
            "star.png",
            "attack.png",
            "heart.png",
            "speed.png",
            "defense.png"
            );
}