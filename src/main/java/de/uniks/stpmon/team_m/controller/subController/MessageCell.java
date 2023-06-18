package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;
import static de.uniks.stpmon.team_m.controller.subController.IngameMessageCell.getZoneID;
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
    @FXML
    public ImageView avatar;
    private FXMLLoader loader;
    private final MessagesBoxController messagesBoxController;

    public UserStorage user;


    /**
     * MessageCell is used to handle the message cells.
     * Every message cell has a rootMessageHBox which contains the content of the message, the sender and the time and
     * date of the message. The rootMessageHBox has the id of the message. The message cell has a messageVBox which
     * contains the rootMessageHBox. The message cell has a editMessage button and a deleteMessage button.
     *
     * @param messagesBoxController The {@link MessagesBoxController} is used to handle the messages box.
     * @param user                  The user is used to identify the user.
     */

    public MessageCell(MessagesBoxController messagesBoxController, UserStorage user) {
        this.messagesBoxController = messagesBoxController;
        this.user = user;
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
            final String base64avatar = messagesBoxController.getAvatar(message.sender());
            messageContent.setText(message.body());
            setDateAndEdited(message, senderName);
            setMessageOrientationAndStyle(message);
            if (!GraphicsEnvironment.isHeadless()) {
                avatar.setImage(ImageProcessor.fromBase64ToFXImage(base64avatar));
            }
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
        if (message.sender().equals(user.get_id())) {
            messageVBox.setStyle(messageVBox.getStyle() + OWN_MESSAGE_STYLE);
            setAlignment(CENTER_RIGHT);
            avatar.toFront();
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
        if (!getItem().sender().equals(user.get_id())) {
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
        return getZoneID(dateTime, messagesBoxController.getResources(), DATE_TIME_FORMAT);
    }

    /**
     * The editMessage method is used to edit the message.
     * The editMessage method opens a TextInputDialog where the user can edit the message. If the user clicks on the
     * editMessage method calls the editMessage method of the {@link MessagesBoxController}.
     */

    public void editMessage() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(messagesBoxController.getResources().getString("EDIT.MESSAGE.TITLE"));
        dialog.setHeaderText(null);
        dialog.setContentText(messagesBoxController.getResources().getString("EDIT.MESSAGE.CONTENT"));

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
        alert.setTitle(messagesBoxController.getResources().getString("DELETE.MESSAGE.TITLE"));
        alert.setHeaderText(messagesBoxController.getResources().getString("DELETE.MESSAGE.TITLE"));
        alert.setContentText(messagesBoxController.getResources().getString("DELETE.MESSAGE.CONTENT"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            messagesBoxController.deleteMessage(getItem());
            alert.close();
        }
    }

}