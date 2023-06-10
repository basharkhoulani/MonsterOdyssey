package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static de.uniks.stpmon.team_m.Constants.*;

public class IngameMessageCell extends ListCell<Message> {
    private final IngameController ingameController;
    @FXML
    public ImageView spriteImageView;
    @FXML
    public Label timestamp;
    @FXML
    public Label name;
    @FXML
    public Label messageContent;
    private FXMLLoader loader;

        public IngameMessageCell(IngameController ingameController) {
            this.ingameController = ingameController;
        }

        @Override
        protected void updateItem(Message message, boolean empty) {
            super.updateItem(message, empty);
            setStyle(LIST_VIEW_STYLE_NO_INDEXING);
            if (empty || message == null) {
                setText(null);
                setGraphic(null);
            } else {
                loadFXML();
                Trainer trainer = ingameController.getTrainer(message.sender());
                final String trainerName = trainer.name();
                final String dateTime = formatTimeString(message.createdAt());
                //final Image sprite = trainer.sprite();
                messageContent.setText(message.body());
                name.setText(trainerName);
                timestamp.setText(dateTime);
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
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.parse(dateTime), ZoneId.of(ZONE_ID_EUROPE_BERLIN));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return localDateTime.format(outputFormatter);
    }
}
