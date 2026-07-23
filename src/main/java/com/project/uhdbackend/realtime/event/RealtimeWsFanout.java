package com.project.uhdbackend.realtime.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class RealtimeWsFanout {

    private final SimpMessagingTemplate template;

    public RealtimeWsFanout(SimpMessagingTemplate template) {
        this.template = template;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAfterCommit(UhdRealtimeEvent event) {
        template.convertAndSend("/websocket/v1/uhdconsole", event);
    }
}