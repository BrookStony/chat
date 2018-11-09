package com.seecent.chat.services.exception

class ApiErrorException extends BusinessException {
    Integer code

    ApiErrorException(String message, Integer code) {
        super(message)
        this.code = code
    }

}
