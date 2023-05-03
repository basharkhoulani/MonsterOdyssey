package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.rest.MessagesApiService;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public class MessageService {
    private final MessagesApiService messagesApiService;

    public MessageService(MessagesApiService messagesApiService) {
        this.messagesApiService = messagesApiService;
    }

    
}
