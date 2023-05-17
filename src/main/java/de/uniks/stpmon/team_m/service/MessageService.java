package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.CreateMessageDto;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.UpdateMessageDto;
import de.uniks.stpmon.team_m.rest.MessagesApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

public class MessageService {

    /*
     * TODO: Tests still have to be implemented, but we need mocking to realize this
     * */
    private final MessagesApiService messagesApiService;

    @Inject
    public MessageService(MessagesApiService messagesApiService) {
        this.messagesApiService = messagesApiService;
    }

    // Base api communication

    /**
     * Gets a singular message. Important to note is that the first parameter needs to be the ID of
     * the user the message is directed to, not the sender!
     *
     * @param receiverID the ID of the user who received the message
     * @param messageID  the ID of the message
     * @param namespace  the namespace used for api communication. Allowed values are CONSTANTS_NAMESPACE constants
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
     * @param namespace  the namespace used for api communication
     * @return an observable list of message records
     */
    private Observable<List<Message>> getMessagesByNamespace(String receiverID, String namespace) {
        return messagesApiService.getMessages(namespace, receiverID);
    }

    /**
     * Creates a message that is directed to one user/group/region.
     *
     * @param receiverID the ID of the receiver
     * @param message    the body of the message
     * @param namespace  the namespace used for api communication
     * @return the created message as an observable
     */
    public Observable<Message> newMessage(String receiverID, String message, String namespace) {
        CreateMessageDto createMessageDto = new CreateMessageDto(message);
        return messagesApiService.create(namespace, receiverID, createMessageDto);
    }

    /**
     * Updates a message.
     *
     * @param receiverID     the ID of the receiver
     * @param messageID      the message ID
     * @param updatedMessage the body of the updated message
     * @param namespace      the namespace used for api communication
     * @return the updated message as an observable
     */
    public Observable<Message> updateMessage(String receiverID, String messageID, String updatedMessage, String namespace) {
        UpdateMessageDto updateMessageDto = new UpdateMessageDto(updatedMessage);
        return messagesApiService.update(namespace, receiverID, messageID, updateMessageDto);
    }


    // General message methods

    /**
     * Gets all the messages from a group and returns them sorted after createdAt. This also includes private chat
     * messages, since private messages are realized via a group of two persons.
     *
     * @param groupID the ID of the group which messages you need
     * @return a sorted observable of a sorted message list
     */
    public Observable<List<Message>> getGroupMessages(String groupID) {
        return this.getMessagesByNamespace(groupID, Constants.MESSAGE_NAMESPACE_GROUPS)
                .map(messages -> {
                    messages.sort(Comparator.comparing(Message::createdAt));
                    return messages;
                });
    }

    //TODO: maybe you can refactor this function out, since it's basically the same code as the getGroupMessages
    // method, but maybe it will be better for readability and usability if we keep them separate.
    // We also could create a helper method and just make one line calls in the getGroupMessages and getRegionMessages
    // methods.

    /**
     * Gets all the messages from a region and returns them sorted after createdAt
     *
     * @param regionID the ID of the group which messages you need
     * @return a sorted observable of a sorted message list
     */
    public Observable<List<Message>> getRegionMessages(String regionID) {
        return this.getMessagesByNamespace(regionID, Constants.MESSAGE_NAMESPACE_REGIONS)
                .map(messages -> {
                    messages.sort(Comparator.comparing(Message::createdAt));
                    return messages;
                });
    }

    public Observable<List<Message>> getGlobalMessages(String parentID) {
        return this.getMessagesByNamespace(parentID, Constants.MESSAGE_NAMESPACE_GLOBAL)
                .map(messages -> {
                    messages.sort(Comparator.comparing(Message::createdAt));
                    return messages;
                });
    }
}

