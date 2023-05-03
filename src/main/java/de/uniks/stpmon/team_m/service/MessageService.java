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

    public Observable<Message> getMessageOfUserByID(String userID, String messageID) {
        return messagesApiService.getMessage(Constants.API_URL, userID, messageID);
    }

    public Observable<List<Message>> getMessagesOfUser(String userID) {
        return messagesApiService.getMessages(Constants.API_URL, userID);
    }

    public Observable<Message> newMessage(String userID, String message) {
        CreateMessageDto createMessageDto = new CreateMessageDto(message);
        return messagesApiService.create(Constants.API_URL, userID, createMessageDto);
    }

    public Observable<Message> updateMessage(String userID, String messageID, String updatedMessage) {
        UpdateMessageDto updateMessageDto = new UpdateMessageDto(updatedMessage);
        return messagesApiService.update(Constants.API_URL, userID, messageID, updateMessageDto);
    }
}
