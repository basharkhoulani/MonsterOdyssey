package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.AVATAR_1;
import static de.uniks.stpmon.team_m.Constants.smallHandyImage;

public class IngameNotificationCell extends ListCell<String> {

    @FXML
    public HBox rootNotificationHBox;
    @FXML
    public ImageView avatar;
    @FXML
    public TextFlow notificationTextFlow;

    private Text notificationText;
    private FXMLLoader loader;

    public IngameNotificationCell(NotificationListHandyController notificationListHandyController) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/uniks/stpmon/team_m/views/IngameNotificationCell.fxml"));
            loader.setController(this);
            rootNotificationHBox = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file", e);
        }
    }

    @Override
    protected void updateItem(String notificationString, boolean empty) {
        super.updateItem(notificationString, empty);

        if (empty || notificationString == null) {
            setGraphic(null);
        } else {
            loadFXML();
            notificationText.setText(notificationString);

            if(!GraphicsEnvironment.isHeadless()){
                avatar.setImage(new Image(Objects.requireNonNull(App.class.getResource(AVATAR_1)).toString()));
            }

            setGraphic(rootNotificationHBox);
        }
    }

    private void loadFXML() {
        if (loader == null) {
            notificationText = new Text();
            notificationText.setId("notificationText");
            notificationText.setFont(Font.font(13));

            loader = new FXMLLoader(Main.class.getResource("views/IngameNotificationCell.fxml"));
            loader.setControllerFactory(c -> this);
            try {
                loader.load();

                notificationTextFlow.getChildren().add(notificationText);
                notificationTextFlow.setPadding(new Insets(10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
