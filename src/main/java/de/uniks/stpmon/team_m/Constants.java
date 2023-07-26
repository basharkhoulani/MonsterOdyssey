package de.uniks.stpmon.team_m;

import javafx.geometry.Insets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class Constants {
    // SERVER
    public static final String API_URL = "https://stpmon.uniks.de/api/v4";
    public static final String WS_URL = "wss://stpmon.uniks.de/ws/v4";
    public static final String UDP_URL = "stpmon.uniks.de";
    public static final int SERVER_PORT = 30014;
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
    public static final String TIME_FORMAT = "HH:mm";
    public static final int DURATION_OF_LOADING_SCREEN = 3;

    // LOGIN & SIGNUP
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final char BULLET = '\u25cf';
    public static final String REFRESH_TOKEN_PREF = "refreshToken";

    // Account Settings
    public static final String MONSTER1_WITHOUT = "images/Monster1-without.png";

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
    public static final int TRANSITION_DURATION = 196;
    public static final int SCALE_FACTOR = 2;
    public static final int DELAY = 100;
    public static final int DELAY_LONG = 500;
    public static final double ZERO = 0;
    public static final double ONE = 1;
    public static final int TRAINER_DIRECTION_RIGHT = 0;
    public static final int TRAINER_DIRECTION_UP = 1;
    public static final int TRAINER_DIRECTION_LEFT = 2;
    public static final int TRAINER_DIRECTION_DOWN = 3;


    public static final String SETTINGSYMBOL2 = "images/SettingSymbol2.png";
    public static final String GOBACKSYMBOL = "images/GoBackSymbol.png";
    public static final String PLAYSYMBOL = "images/PlaySymbol.png";
    public static final String AUDIOSYMBOL = "images/AudioSymbol.png";
    public static final String KEYBINDINGSSYMBOL = "images/KeybindingsSymbol.png";
    public static final String CHECKSYMBOL = "images/checkSymbol.png";
    public static final String WARNING = "images/Warning.png";
    public static final String PENCIL2SYMBOL = "images/Pencil2.png";
    public static final String ARROWLEFTSYMBOL = "images/arrowLeft.png";
    public static final String ARROWRIGHTSYMBOL = "images/arrowRight.png";
    public static final String MAPSYMBOL = "images/MapSymbol.png";
    public static final String MONSTER_WITH_COLOR = "images/Monster_mit_Farbe.png";
    public static final String COIN = "images/coin.png";

    // NPC DIALOG
    public static final int dialogVBoxWidth = 700;
    public static final int getDialogVBoxHeight = 150;
    public static final int spacerToBottomOfScreen = 150;
    public static final Insets dialogTextFlowInsets = new Insets(20, 40, 20, 40);
    public static final Insets helpLabelInsets = new Insets(0, 40, 20, 0);
    public static final int helpLabelFontSize = 12;


    public enum DialogSpecialInteractions {
        nurseYes,
        nurseNo,
        nurseNoMons,
        starterSelection0,
        starterSelection1,
        starterSelection2,
        clerkOpenShop,
        clerkCancelShop
    }

    public enum ContinueDialogReturnValues {
        dialogNotFinished,
        dialogFinishedTalkToTrainer,
        dialogFinishedNoTalkToTrainer,
        albertDialogFinished0,
        albertDialogFinished1,
        albertDialogFinished2,
        spokenToNurse,
        encounterOnTalk,
        spokenToClerk

    }

    // Nurse Popup
    public static final int popupHeight = 170;
    public static final int popupWidth = 300;
    public static final int textFieldHeight = 120;
    public static final int buttonsHBoxHeight = 50;
    public static final int buttonsHBoxSpacing = 30;
    public static final int nurseButtonHeight = 40;
    public static final int nurseButtonWidth = 80;

    // Clerk Popup
    public static final int clerkPopupHeight = 250;
    public static final int clerkQuestionHeight = 75;
    public static final int clerkButtonsVBoxHeight = 225;
    public static final int clerkButtonVBoxSpacing = 10;
    public static final int clerkButtonWidth = 250;
    public static final int clerkButtonHeight = 40;


    // Ingame Help
    public static final int spaceBetweenPhoneAndWindowEdge = 25;
    public static final int offsetToNotShowPhoneInScreen = 30;
    public static final String smallHandyImage = "images/SmallHandy.png";
    public static final String notificationBellImage = "images/NotificationBell.png";

    // Encounter
    public static final int fleePopupWidth = 300;
    public static final int fleePopupHeight = 200;
    public static final int fleeTextHeight = 150;
    public static final Insets fleeTextInsets = new Insets(20, 10, 0, 10);
    public static final int fleeButtonsHBoxHeight = 50;
    public static final Insets fleeButtonsHBoxInsets = new Insets(0, 10, 10, 10);
    public static final int fleeButtonWidth = 100;
    public static final int fleeButtonHeight = 40;

    // Encounter - images
    public static final String STAR_ICON = "images/star.png";
    public static final String HEART_ICON = "images/heart.png";

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
    public static final String MONSTER_2_COLOR = "images/Monster2-color.png";
    public static final String MONSTER_1_COLOR = "images/Monster1-color.png";

    // Change Audio Settings
    public static final String MUTE_ICON = "images/mute-icon.png";
    public static final String HIGH_VOLUME_ICON = "images/high-volume-icon.png";

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
    public static final HashMap<String, String> TYPESCOLORPALETTE = new HashMap<>(Map.ofEntries(
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
    public static final HashMap<String, String> ABILITYPALETTE = new HashMap<>(Map.ofEntries(
            entry("normal", "ability-normal.png"),
            entry("fire", "ability-fire.png"),
            entry("water", "ability-water.png"),
            entry("electric", "ability-electic.png"),
            entry("grass", "ability-grass.png"),
            entry("flying", "ability-flying.png"),
            entry("bug", "ability-bug.png"),
            entry("poison", "ability-poison.png"),
            entry("rock", "ability-rock.png"),
            entry("ground", "ability-ground.png"),
            entry("fighting", "ability-fighting.png"),
            entry("ice", "ability-ice.png"),
            entry("psychic", "ability-psychic.png"),
            entry("ghost", "ability-ghost.png"),
            entry("dragon", "ability-dragon.png"),
            entry("dark", "ability-dragon.png"),
            entry("steel", "ability-steel.png"),
            entry("fairy", "ability-fairy.png")
    ));

    public static final List<String> ATTRIBUTE_IMAGES = Arrays.asList(
            "star.png",
            "attack.png",
            "heart.png",
            "speed.png",
            "defense.png"
    );

    public static final String PARALYSED = "paralysed";
    public static final String ASLEEP = "asleep";
    public static final String POISONED = "poisoned";
    public static final String BURNED = "burned";
    public static final String FROZEN = "frozen";
    public static final String CONFUSED = "confused";

    public static final List<String> STATUS_EFFECTS = Arrays.asList(PARALYSED, ASLEEP, POISONED, BURNED, FROZEN, CONFUSED);

    public static final HashMap<String, String> STATUS_EFFECTS_IMAGES = new HashMap<>(Map.ofEntries(
            entry(PARALYSED, "images/paralysed.png"),
            entry(ASLEEP, "images/asleep.png"),
            entry(POISONED, "images/poisoned.png"),
            entry(BURNED, "images/burned.png"),
            entry(FROZEN, "images/frozen.png"),
            entry(CONFUSED, "images/confused.png")
    ));


    public enum itemType {
        ball,
        effect,
        monsterBox,
        itemBox
    }

    public static final String ITEM_ACTION_USE_ITEM = "use";
    public static final String ITEM_USAGE_EFFECT = "effect";
}