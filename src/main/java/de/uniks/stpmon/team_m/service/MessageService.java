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

    public Observable<Message> getMessageOfUserByID(String receiverID, String messageID) {
        return messagesApiService.getMessage(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID, messageID);
    }

    public Observable<List<Message>> getMessagesOfUser(String receiverID) {
        return messagesApiService.getMessages(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID);
    }

    public Observable<Message> newPrivateMessage(String receiverID, String message) {
        CreateMessageDto createMessageDto = new CreateMessageDto(message);
        return messagesApiService.create(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID, createMessageDto);
    }

    public Observable<Message> updatePrivateMessage(String receiverID, String messageID, String updatedMessage) {
        UpdateMessageDto updateMessageDto = new UpdateMessageDto(updatedMessage);
        return messagesApiService.update(Constants.MESSAGE_NAMESPACE_GLOBAL, receiverID, messageID, updateMessageDto);
    }
}
