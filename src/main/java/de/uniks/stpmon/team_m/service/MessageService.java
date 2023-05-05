package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.CreateMessageDto;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.UpdateMessageDto;
import de.uniks.stpmon.team_m.rest.MessagesApiService;
import io.reactivex.rxjava3.core.Observable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageService {

    /*
    * TODO: Tests still have to be implemented, but we need mocking to realize this
    * */
    private final MessagesApiService messagesApiService;

    public MessageService(MessagesApiService messagesApiService) {
        this.messagesApiService = messagesApiService;
    }

    // Base api communication
    /**
     * Gets a singular message. Important to note is that the first parameter needs to be the ID of
     * the user the message is directed to, not the sender!
     *
     * @param receiverID the ID of the user who received the message
     * @param messageID the ID of the message
     * @param namespace the namespace used for api communication. Allowed values are CONSTANTS_NAMESPACE constants
     * @return an observable record of message
     */
    public Observable<Message> getMessageOfUserByID(String receiverID, String messageID, String namespace) {
        return messagesApiService.getMessage(namespace, receiverID, messageID);
    }

    /**
     * Works similar to the "getMessageOfUserID" method, but instead returns a list of messages. Again, important
     * to note is, that the first parameter needs to be the ID of the user who received the messages, not the sender!
     *
     * @param receiverID the ID of the user who received the messages
     * @param namespace the namespace used for api communication
     * @return an observable list of message records
     */
    public Observable<List<Message>> getMessagesByNamespace(String receiverID, String namespace) {
        return messagesApiService.getMessages(namespace, receiverID);
    }

    /**
     * Creates a message that is directed to one user/group/region.
     *
     * @param receiverID the ID of the receiver
     * @param message the body of the message
     * @param namespace the namespace used for api communication
     * @return the created message as an observable
     */
    public Observable<Message> newMessage(String receiverID, String message, String namespace) {
        CreateMessageDto createMessageDto = new CreateMessageDto(message);
        return messagesApiService.create(namespace, receiverID, createMessageDto);
    }

    /**
     * Updates a message.
     *
     * @param receiverID the ID of the receiver
     * @param messageID the message ID
     * @param updatedMessage the body of the updated message
     * @param namespace the namespace used for api communication
     * @return the updated message as an observable
     */
    public Observable<Message> updatePrivateMessage(String receiverID, String messageID, String updatedMessage, String namespace) {
        UpdateMessageDto updateMessageDto = new UpdateMessageDto(updatedMessage);
        return messagesApiService.update(namespace, receiverID, messageID, updateMessageDto);
    }


    // General message methods
    /**
     * Gets all the messages of two users. Messages will be sorted after property "createdAt".
     *
     * @return an observable list of all messages between two users (sorted after "createdAt")
     */
    public Observable<List<Message>> getPrivateChatMessages(String friendUserID, String ownUserID) {
        // these are the messages from the friend to the user (these need to be displayed on the right)
        Observable<List<Message>> messagesFriendUser = this.getMessagesByNamespace(ownUserID, Constants.MESSAGE_NAMESPACE_GLOBAL)
                .map(messages -> messages
                        .stream()
                        .filter(message -> message.sender().equals(friendUserID)).collect(Collectors.toList())
                );

        // these are the messages from the user to the friend
        Observable<List<Message>> messagesUserFriend = this.getMessagesByNamespace(friendUserID, Constants.MESSAGE_NAMESPACE_GLOBAL)
                .map(messages -> messages
                        .stream()
                        .filter(message -> message.sender().equals(ownUserID)).collect(Collectors.toList()));

        // sorts the messages after createdAt property (don't quite know yet if it's ascending or descending rn)
        Observable<List<Message>> allMessages = messagesFriendUser.mergeWith(messagesUserFriend);
        allMessages = allMessages.map(messages -> {
            messages.sort(Comparator.comparing(Message::createdAt));
            return messages;
        });

        return allMessages;
    }

    /**
     * Gets all the messages from a group and returns them sorted after createdAt
     *
     * @param groupID the ID of the group which messagse you need
     * @return an sorted observable of a sorted message list
     */
    public Observable<List<Message>> getGroupMessages(String groupID) {
        return this.getMessagesByNamespace(groupID, Constants.MESSAGE_NAMESPACE_GROUPS)
                .map(messages -> {
                    messages.sort(Comparator.comparing(Message::createdAt));
                    return messages;
                });
    }
}

