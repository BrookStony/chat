package com.seecent.chat.message.handler

import reactor.event.Event

public interface ReceiveEventHandler {
    void handle(Event<ReceiveContext> evt)
}