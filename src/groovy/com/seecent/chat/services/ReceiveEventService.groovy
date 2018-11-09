package com.seecent.chat.services

import com.seecent.chat.message.handler.ReceiveContext
import com.seecent.chat.services.exception.ApiErrorException

public interface ReceiveEventService {
    void onSubscribeEvent(ReceiveContext context) throws ApiErrorException
    void onScanSubscribeEvent(ReceiveContext context) throws ApiErrorException
    void onScanEvent(ReceiveContext context) throws ApiErrorException
    void onLocationEvent(ReceiveContext context) throws ApiErrorException
    void onClickEvent(ReceiveContext context) throws ApiErrorException
    void onViewEvent(ReceiveContext context) throws ApiErrorException
}