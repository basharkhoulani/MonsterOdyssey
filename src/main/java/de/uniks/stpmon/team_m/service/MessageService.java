package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.CreateMessageDto;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.UpdateMessageDto;
import de.uniks.stpmon.team_m.rest.MessagesApiService;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public class MessageService {
    private final MessagesApiService messagesApiService;

    public MessageService(MessagesApiService messagesApiService) {
        this.messagesApiService = messagesApiService;
    }

    // Private messages
    /**
     * Gets a singular private message. Important to note is that the first parameter needs to be the ID of
     * the user the message is directed to, not the sender!
     *
     * @param receiverID the ID of the user who received the message
     * @param messageID the ID of the message
     * @return an observable record of message
     */
    public Observable<Message> getMessageOfUserByID(String receiverID, String messageID) {
        return messagesApiService.getMessage(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID, messageID);
    }

    /**
     * Works similar to the "getMessageOfUserID" method, but instead returns a list of private messages. Again, important
     * to note is, that the first parameter needs to be the ID of the user who received the messages, not the sender!
     *
     * @param receiverID the ID of the user who received the messages
     * @return an observable list of message records
     */
    public Observable<List<Message>> getMessagesOfUser(String receiverID, String senderID) {
        return messagesApiService.getMessages(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID);
    }

    /**
     * Creates a message that is directed to one user (private chat).
     *
     * @param receiverID the ID of the receiver
     * @param message the body of the message
     * @return the created message as an observable
     */
    public Observable<Message> newPrivateMessage(String receiverID, String message) {
        CreateMessageDto createMessageDto = new CreateMessageDto(message);
        return messagesApiService.create(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID, createMessageDto);
    }

    /**
     * Updates a private message.
     *
     * @param receiverID the ID of the receiver
     * @param messageID the message ID
     * @param updatedMessage the body of the updated message
     * @return the updated message as an observable
     */
    public Observable<Message> updatePrivateMessage(String receiverID, String messageID, String updatedMessage) {
        UpdateMessageDto updateMessageDto = new UpdateMessageDto(updatedMessage);
        return messagesApiService.update(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID, messageID, updateMessageDto);
    }
}
