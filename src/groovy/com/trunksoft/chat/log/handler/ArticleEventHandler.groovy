package com.trunksoft.chat.message.handler

import reactor.event.Event

public interface ArticleEventHandler {
    void handle(Event<ArticleEventContext> evt)
}