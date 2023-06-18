package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javax.inject.Provider;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.TIME_FORMAT;

public class IngameMessageCell extends ListCell<Message> {
    private final Provider<ResourceBundle> resourceBundleProvider;
    private final IngameController ingameController;
    @FXML
    public ImageView spriteImageView;
    @FXML
    public Label timestamp;
    @FXML
    public Label name;
    @FXML
    public Label messageContent;
    @FXML
    public HBox rootHBox;
    private FXMLLoader loader;

    public IngameMessageCell(Provider<ResourceBundle> resourceBundleProvider, IngameController ingameController) {
        this.resourceBundleProvider = resourceBundleProvider;
        this.ingameController = ingameController;
    }

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if (empty || message == null) {
            setText(null);
            setGraphic(null);
        } else {
            loadFXML();
            Trainer trainer = ingameController.getTrainer(message.sender());
            final String trainerName = trainer.name() + ":";
            final String dateTime = formatTimeString(message.createdAt());
            ingameController.setTrainerSpriteImageView(trainer, spriteImageView);
            messageContent.setText(message.body());
            name.setText(trainerName);
            timestamp.setText(dateTime);
            setGraphic(rootHBox);
            setPadding(new javafx.geometry.Insets(0));
        }
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/IngameMessageCell.fxml"));
            loader.setControllerFactory(c -> this);
            try {
                loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatTimeString(String dateTime) {
        return getZoneID(dateTime, resourceBundleProvider, TIME_FORMAT);
    }

    static String getZoneID(String dateTime, Provider<ResourceBundle> resourceBundleProvider, String timeFormat) {
        String zone = String.valueOf(resourceBundleProvider.get().getLocale());
        String zoneId = switch (zone) {
            case "de" -> "Europe/Berlin";
            case "en" -> "America/New_York";
            default -> "Asia/Shanghai";
        };
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.parse(dateTime), ZoneId.of(zoneId));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(timeFormat);
        return localDateTime.format(outputFormatter);
    }

}
