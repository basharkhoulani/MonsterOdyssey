package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.MessagesController;
import de.uniks.stpmon.team_m.dto.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;
import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class MessageCell extends ListCell<Message> {

    @FXML
    public HBox rootMessageHBox;
    @FXML
    public Label messageContent;
    @FXML
    public Label senderTimeDate;
    @FXML
    public Button editMessage;
    @FXML
    public Button deleteMessage;
    @FXML
    public VBox messageVBox;
    private FXMLLoader loader;
    private final MessagesBoxController messagesBoxController;
    public String userID;

    public MessageCell(MessagesBoxController messagesBoxController, String userID) {
        this.messagesBoxController = messagesBoxController;
        this.userID = userID;
    }

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        setStyle(LIST_VIEW_STYLE_NO_INDEXING);
        if (message == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(Main.class.getResource("views/MessageCell.fxml"));
                loader.setControllerFactory(c -> this);
                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            final String senderName = messagesBoxController.getUsername(message.sender());
            messageContent.setText(message.body());
            if (!message.createdAt().equals(message.updatedAt())) {
                senderTimeDate.setText(senderName + ", " + formatTimeString(getItem().updatedAt()) + " (" + PENCIL + ")");
            } else {
                senderTimeDate.setText(senderName + ", " + formatTimeString(getItem().createdAt()));
            }
            if (message.sender().equals(userID)) {
                messageVBox.setStyle(messageVBox.getStyle() + OWN_MESSAGE_STYLE);
                setAlignment(CENTER_RIGHT);
            } else {
                messageVBox.setStyle(messageVBox.getStyle() + NOT_OWN_MESSAGE_STYLE);
                setAlignment(CENTER_LEFT);
            }
            messageVBox.setStyle(messageVBox.getStyle() + ROUNDED_CORNERS_STYLE + BORDER_COLOR_BLACK);
            setGraphic(rootMessageHBox);
            setUserData(message);
            setText(null);
        }
    }

    public void showEditAndDelete() {
        if (!getItem().sender().equals(userID)) {
            editMessage.setVisible(false);
            deleteMessage.setVisible(false);
        } else {
            editMessage.setVisible(true);
            deleteMessage.setVisible(true);
        }
    }

    public void hideEditAndDelete() {
        editMessage.setVisible(false);
        deleteMessage.setVisible(false);
    }

    private String formatTimeString(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.parse(dateTime), ZoneId.of("Europe/Berlin"));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");
        return localDateTime.format(outputFormatter);
    }

    public void editMessage() {
        if (!getItem().sender().equals(userID)) {
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(EDIT_MESSAGE_TITLE);
        dialog.setHeaderText(null);
        dialog.setContentText(EDIT_MESSAGE_CONTENT);

        TextArea messageArea = new TextArea();
        messageArea.setText(getItem().body());

        dialog.getDialogPane().setContent(messageArea);
        Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        ok.setOnAction(event -> messagesBoxController.editMessage(dialog, messageArea.getText(), getItem()));
        dialog.showAndWait();
    }

    public void deleteMessage() {
        if (!getItem().sender().equals(userID)) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(DELETE_MESSAGE_TITLE);
        alert.setHeaderText(DELETE_MESSAGE_TITLE);
        alert.setContentText(DELETE_MESSAGE_CONTENT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            messagesBoxController.deleteMessage(alert, getItem());
        }
    }
}