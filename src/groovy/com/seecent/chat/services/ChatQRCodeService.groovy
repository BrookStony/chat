package com.seecent.chat.services

import com.seecent.chat.Account
import com.seecent.chat.account.QRCode
import com.seecent.chat.services.exception.ApiErrorException

/**
 * Created by Administrator on 2016/3/23.
 */
public interface ChatQRCodeService {

    QRCode create(Account account, QRCode qrCode) throws ApiErrorException
}