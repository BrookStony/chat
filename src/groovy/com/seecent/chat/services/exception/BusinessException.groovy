package com.seecent.chat.services.exception

class BusinessException extends Exception {
    BusinessException() {
        super()
    }

    BusinessException(String message) {
        super(message)
    }

    BusinessException(String message, Throwable cause) {
        super(message, cause)
    }

    BusinessException(Throwable cause) {
        super(cause)
    }
}
