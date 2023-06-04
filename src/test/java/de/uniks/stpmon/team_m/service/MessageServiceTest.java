package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.rest.MessagesApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessagesApiService messagesApiService;

    @InjectMocks
    MessageService messageService;

    @Test
    void getMessageOfUserByID() {
        when(messagesApiService.getMessage(any(), any(), any())).thenReturn(Observable.just(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a test message 1.")
        ));

        Message message = messageService.getMessageOfUserByID("6475e51abff65ded36a854ad", "6475e58f40b88eaff651b164", "global").blockingFirst();
        assertEquals("6475e58f40b88eaff651b164", message._id());
        assertEquals("6475e51abff65ded36a854ae", message.sender());
        assertEquals("This is a test message 1.", message.body());
    }

    @Test
    void newMessage() {
        when(messagesApiService.create(any(), any(), any())).thenReturn(Observable.just(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a created test message 2.")
        ));

        Message message = messageService.newMessage("6475e51abff65ded36a854ad", "This is a created test message 2.", "global").blockingFirst();
        assertEquals("6475e58f40b88eaff651b164", message._id());
        assertEquals("6475e51abff65ded36a854ae", message.sender());
        assertEquals("This is a created test message 2.", message.body());
    }

    @Test
    void updateMessage() {
        when(messagesApiService.update(any(), any(), any(), any())).thenReturn(Observable.just(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is an updated test message 3.")
        ));

        Message message = messageService.updateMessage("6475e51abff65ded36a854ad", "6475e58f40b88eaff651b164", "This is an updated test message 3.", "global").blockingFirst();
        assertEquals("6475e58f40b88eaff651b164", message._id());
        assertEquals("6475e51abff65ded36a854ae", message.sender());
        assertEquals("This is an updated test message 3.", message.body());
    }

    @Test
    void deleteMessage() {
        when(messagesApiService.delete(any(), any(), any())).thenReturn(Observable.just(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a deleted test message 4.")
        ));

        Message message = messageService.deleteMessage("6475e51abff65ded36a854ad", "6475e58f40b88eaff651b164", "global").blockingFirst();
        assertEquals("6475e58f40b88eaff651b164", message._id());
        assertEquals("6475e51abff65ded36a854ae", message.sender());
        assertEquals("This is a deleted test message 4.", message.body());
    }

    @Test
    void getGroupMessages() {
        when(messagesApiService.getMessages(any(), any())).thenReturn(Observable.just(Arrays.asList(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a test message 1."),
                new Message("2023-05-31T12:01:57.510Z", "2023-05-31T12:01:57.510Z", "6475e59dd60f90d6f550dd2d",
                        "6475e59dd60f90d6f550dd2d", "This is a test message 2.")
        )));

        List<Message> messages = messageService.getGroupMessages("6475e51abff65ded36a854ad").blockingFirst();
        assertEquals(2, messages.size());
        assertEquals("6475e58f40b88eaff651b164", messages.get(0)._id());
        assertEquals("6475e51abff65ded36a854ae", messages.get(0).sender());
        assertEquals("This is a test message 1.", messages.get(0).body());
        assertEquals("6475e59dd60f90d6f550dd2d", messages.get(1)._id());
        assertEquals("6475e59dd60f90d6f550dd2d", messages.get(1).sender());
        assertEquals("This is a test message 2.", messages.get(1).body());
    }

    @Test
    void getRegionMessages() {
        when(messagesApiService.getMessages(any(), any())).thenReturn(Observable.just(Arrays.asList(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a test message 2."),
                new Message("2023-05-31T12:01:57.510Z", "2023-05-31T12:01:57.510Z", "6475e59dd60f90d6f550dd2d",
                        "6475e59dd60f90d6f550dd2d", "This is a test message 3.")
        )));

        List<Message> messages = messageService.getRegionMessages("6475e51abff65ded36a854ad").blockingFirst();
        assertEquals(2, messages.size());
        assertEquals("6475e58f40b88eaff651b164", messages.get(0)._id());
        assertEquals("6475e51abff65ded36a854ae", messages.get(0).sender());
        assertEquals("This is a test message 2.", messages.get(0).body());
        assertEquals("6475e59dd60f90d6f550dd2d", messages.get(1)._id());
        assertEquals("6475e59dd60f90d6f550dd2d", messages.get(1).sender());
        assertEquals("This is a test message 3.", messages.get(1).body());
    }

    @Test
    void getGlobalMessages() {
        when(messagesApiService.getMessages(any(), any())).thenReturn(Observable.just(Arrays.asList(
                new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a test message 3."),
                new Message("2023-05-31T12:01:57.510Z", "2023-05-31T12:01:57.510Z", "6475e59dd60f90d6f550dd2d",
                        "6475e59dd60f90d6f550dd2d", "This is a test message 3.")
        )));

        List<Message> messages = messageService.getGlobalMessages("647660ed82b2d6d743f0339a").blockingFirst();
        assertEquals(2, messages.size());
        assertEquals("6475e58f40b88eaff651b164", messages.get(0)._id());
        assertEquals("6475e51abff65ded36a854ae", messages.get(0).sender());
        assertEquals("This is a test message 3.", messages.get(0).body());
        assertEquals("6475e59dd60f90d6f550dd2d", messages.get(1)._id());
        assertEquals("6475e59dd60f90d6f550dd2d", messages.get(1).sender());
        assertEquals("This is a test message 3.", messages.get(1).body());
    }
}