package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

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


    /**
     * MessageCell is used to handle the message cells.
     * Every message cell has a rootMessageHBox which contains the content of the message, the sender and the time and
     * date of the message. The rootMessageHBox has the id of the message. The message cell has a messageVBox which
     * contains the rootMessageHBox. The message cell has a editMessage button and a deleteMessage button.
     *
     * @param messagesBoxController The {@link MessagesBoxController} is used to handle the messages box.
     * @param userID                The userID is used to identify the user.
     */

    public MessageCell(MessagesBoxController messagesBoxController, String userID) {
        this.messagesBoxController = messagesBoxController;
        this.userID = userID;
    }


    /**
     * The updateItem method is used to update the message cell.
     */

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        setStyle(LIST_VIEW_STYLE_NO_INDEXING);
        if (message == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            loadFXML();
            final String senderName = messagesBoxController.getUsername(message.sender());
            messageContent.setText(message.body());
            setDateAndEdited(message, senderName);
            setMessageOrientationAndStyle(message);
            setGraphic(rootMessageHBox);
            setUserData(message);
            setText(null);
        }
    }

    /**
     * The setMessageOrientationAndStyle method is used to set the message orientation and style.
     *
     * @param message The {@link Message} is used to get the sender of the message.
     */

    private void setMessageOrientationAndStyle(Message message) {
        if (message.sender().equals(userID)) {
            messageVBox.setStyle(messageVBox.getStyle() + OWN_MESSAGE_STYLE);
            setAlignment(CENTER_RIGHT);
        } else {
            messageVBox.setStyle(messageVBox.getStyle() + NOT_OWN_MESSAGE_STYLE);
            setAlignment(CENTER_LEFT);
        }
        messageVBox.setStyle(messageVBox.getStyle() + ROUNDED_CORNERS_STYLE + BORDER_COLOR_BLACK);
    }

    /**
     * The setDateAndEdited method is used to set the date and, if edited, the updated time of the message.
     *
     * @param message    The {@link Message} is used to get the date of the message.
     * @param senderName The senderName is used to get the name of the sender.
     */

    private void setDateAndEdited(Message message, String senderName) {
        if (!message.createdAt().equals(message.updatedAt())) {
            senderTimeDate.setText(senderName + ", " + formatTimeString(getItem().updatedAt()) + " (" + PENCIL + ")");
        } else {
            senderTimeDate.setText(senderName + ", " + formatTimeString(getItem().createdAt()));
        }
    }

    /**
     * The loadFXML method is used to load the FXML file.
     */

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/MessageCell.fxml"));
            loader.setControllerFactory(c -> this);
            try {
                loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The showEditAndDelete method is used to show the edit and delete button if the user is the sender of the message.
     */

    public void showEditAndDelete() {
        if (!getItem().sender().equals(userID)) {
            hideEditAndDelete();
        } else {
            editMessage.setVisible(true);
            deleteMessage.setVisible(true);
        }
    }

    public void hideEditAndDelete() {
        editMessage.setVisible(false);
        deleteMessage.setVisible(false);
    }

    /**
     * The formatTimeString method is used to format the time string.
     *
     * @param dateTime The dateTime is used to get the time.
     * @return The formatted time string.
     */

    private String formatTimeString(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.parse(dateTime), ZoneId.of(ZONE_ID_EUROPE_BERLIN));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return localDateTime.format(outputFormatter);
    }

    /**
     * The editMessage method is used to edit the message.
     * The editMessage method opens a TextInputDialog where the user can edit the message. If the user clicks on the
     * editMessage method calls the editMessage method of the {@link MessagesBoxController}.
     */

    public void editMessage() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(EDIT_MESSAGE_TITLE);
        dialog.setHeaderText(null);
        dialog.setContentText(EDIT_MESSAGE_CONTENT);

        TextArea messageArea = new TextArea();
        messageArea.setText(getItem().body());
        messageArea.setId("messageArea");
        dialog.getDialogPane().setContent(messageArea);
        Button confirmEdit = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        confirmEdit.setOnAction(event -> {
            messagesBoxController.editMessage(messageArea.getText(), getItem());
            dialog.close();
        });
        dialog.showAndWait();
    }

    /**
     * The deleteMessage method is used to delete the message.
     * The deleteMessage method opens an Alert where the user can confirm the deletion of the message. If the user
     * confirms the deletion the deleteMessage method calls the deleteMessage method of the {@link MessagesBoxController}.
     */

    public void deleteMessage() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(DELETE_MESSAGE_TITLE);
        alert.setHeaderText(DELETE_MESSAGE_TITLE);
        alert.setContentText(DELETE_MESSAGE_CONTENT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            messagesBoxController.deleteMessage(getItem());
            alert.close();
        }
    }
}