package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.views.RegionCell;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.FX_SCHEDULER;
import static de.uniks.stpmon.team_m.Constants.MAIN_MENU_TITLE;

public class MainMenuController extends Controller {


    @FXML
    public AnchorPane friendsListScrollPane;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button messagesButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Button startGameButton;
    @FXML
    public VBox regionRadioButtonList;

    @Inject
    Provider<LoginController> loginControllerProvider;

    @Inject
    Provider<IngameController> ingameControllerProvider;

    @Inject
    Provider<AccountSettingController> accountSettingControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<MessagesController> messagesControllerProvider;
    @Inject
    RegionsApiService regionsApiService;
    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    private ToggleGroup regionToggleGroup;


    @Override
    public void init() {
        disposables.add(regionsApiService.getRegions().observeOn(FX_SCHEDULER).subscribe(this.regions::setAll));
    }

    @Inject
    public MainMenuController() {
    }

    @Override
    public String getTitle() {
        return MAIN_MENU_TITLE;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initRadioButtons();
        return parent;
    }

    private void initRadioButtons() {
        ListView<Region> regionListView = new ListView<>();
        regionListView.setId("regionListView");
        regionToggleGroup = new ToggleGroup();
        regionListView.setCellFactory(param -> new RegionCell(regionToggleGroup));
        regionListView.setItems(regions);
        regionRadioButtonList.getChildren().add(regionListView);
        startGameButton.disableProperty().bind(regionToggleGroup.selectedToggleProperty().isNull());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToMessages() {
        app.show(messagesControllerProvider.get());
    }

    public void changeToLogin() {
        app.show(loginControllerProvider.get());
    }

    public void changeToSettings() {
        app.show(accountSettingControllerProvider.get());
    }

    public void changeToIngame() {
        Region selectedRegion = (Region) regionToggleGroup.getSelectedToggle().getUserData();
        System.out.println(selectedRegion);
        app.show(ingameControllerProvider.get());
    }
}
