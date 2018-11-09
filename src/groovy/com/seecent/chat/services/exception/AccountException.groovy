package com.seecent.chat.services.exception

class AccountException extends BusinessException {
    Integer code

    AccountException(String message, Integer code) {
        super(message)
        this.code = code
    }
}
