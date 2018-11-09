package com.trunksoft.chat.services

import com.trunksoft.chat.message.handler.ReceiveContext
import com.trunksoft.chat.services.exception.ApiErrorException

public interface ReceiveEventService {
    void onSubscribeEvent(ReceiveContext context) throws ApiErrorException
    void onScanSubscribeEvent(ReceiveContext context) throws ApiErrorException
    void onScanEvent(ReceiveContext context) throws ApiErrorException
    void onLocationEvent(ReceiveContext context) throws ApiErrorException
    void onClickEvent(ReceiveContext context) throws ApiErrorException
    void onViewEvent(ReceiveContext context) throws ApiErrorException
}