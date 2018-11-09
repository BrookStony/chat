package com.trunksoft.chat.services

import com.trunksoft.chat.Account
import com.trunksoft.chat.message.MassMessage
import com.trunksoft.chat.services.exception.ApiErrorException

public interface ChatMassMessageService {
    MassMessageResult send(Account account, MassMessage msg) throws ApiErrorException
    MassMessageResult preview(Account account, MassMessage msg) throws ApiErrorException
    MassMessageResult delete(Account account, MassMessage msg) throws ApiErrorException
    MassMessageResult getStatus(Account account, MassMessage msg) throws ApiErrorException
}
