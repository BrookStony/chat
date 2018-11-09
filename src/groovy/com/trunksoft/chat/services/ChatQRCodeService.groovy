package com.trunksoft.chat.services

import com.trunksoft.chat.Account
import com.trunksoft.chat.account.QRCode
import com.trunksoft.chat.services.exception.ApiErrorException

/**
 * Created by Administrator on 2016/3/23.
 */
public interface ChatQRCodeService {

    QRCode create(Account account, QRCode qrCode) throws ApiErrorException
}