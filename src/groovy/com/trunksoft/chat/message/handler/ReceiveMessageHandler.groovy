package com.trunksoft.chat.message.handler

import reactor.event.Event

public interface ReceiveMessageHandler {
    void handle(Event<ReceiveContext> evt)
}