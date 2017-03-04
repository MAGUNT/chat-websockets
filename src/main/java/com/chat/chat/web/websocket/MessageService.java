package com.chat.chat.web.websocket;

import com.chat.chat.security.SecurityUtils;
import com.chat.chat.service.MesageService;
import com.chat.chat.service.dto.MesageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import java.time.ZonedDateTime;


/**
 * Created by melvin on 2/17/2017.
 */
@Controller
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final MesageService mesageService;

    public MessageService(SimpMessageSendingOperations messagingTemplate, MesageService mesageService) {
        this.messagingTemplate = messagingTemplate;
        this.mesageService = mesageService;
    }

    // verificar si deberia usar principal o spring utils.
    @SubscribeMapping("/topic/message/{idEvent}")
    @SendTo("/topic/chat/{idEvent}")
    public MesageDTO sendMessage(@Payload MesageDTO messageDTO, @DestinationVariable Long idEvent) {
        messageDTO.setDate(ZonedDateTime.now());
        messageDTO.setEventoId(idEvent);
        messageDTO = mesageService.saveMessageFromCurrentUser(messageDTO);
        messageDTO.setEmisorLogin(SecurityUtils.getCurrentUserLogin());
        return messageDTO;
    }

}
