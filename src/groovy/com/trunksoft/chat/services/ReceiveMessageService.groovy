package com.trunksoft.chat.services

import com.trunksoft.chat.message.handler.ReceiveContext
import com.trunksoft.chat.services.exception.ApiErrorException

public interface ReceiveMessageService {
    void onTextMessage(ReceiveContext context) throws ApiErrorException
    void onImageMessage(ReceiveContext context) throws ApiErrorException
    void onVoiceMessage(ReceiveContext context) throws ApiErrorException
    void onVideoMessage(ReceiveContext context) throws ApiErrorException
    void onLocationMessage(ReceiveContext context) throws ApiErrorException
    void onLinkMessage(ReceiveContext context) throws ApiErrorException
}