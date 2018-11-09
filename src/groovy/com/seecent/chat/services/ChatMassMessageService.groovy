package com.seecent.chat.services

import com.seecent.chat.Account
import com.seecent.chat.message.MassMessage
import com.seecent.chat.services.exception.ApiErrorException

public interface ChatMassMessageService {
    MassMessageResult send(Account account, MassMessage msg) throws ApiErrorException
    MassMessageResult preview(Account account, MassMessage msg) throws ApiErrorException
    MassMessageResult delete(Account account, MassMessage msg) throws ApiErrorException
    MassMessageResult getStatus(Account account, MassMessage msg) throws ApiErrorException
}
