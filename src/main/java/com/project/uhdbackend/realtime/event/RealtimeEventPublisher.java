package com.project.uhdbackend.realtime.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class RealtimeEventPublisher {

    private final ApplicationEventPublisher publisher;

    public RealtimeEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(UhdRealtimeEvent event) {
        publisher.publishEvent(event);
    }
}
