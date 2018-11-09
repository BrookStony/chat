package com.trunksoft.chat.util

import java.security.MessageDigest

class EncodeUtil {

    private static final String MD5 = "MD5"
    private static final String SHA1 = "SHA1"
    private static final def HEX_DIGITS = ['0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']

    static String sha1(String text) {
        return encode(SHA1, text)
    }

    static String md5(String text) {
        return encode(MD5, text)
    }

    static String encode(String algorithm, String text) {
        if(text){
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm)
            messageDigest.update(text.getBytes())
            return formatText(messageDigest.digest())
        }
        return null
    }

    // 把密文转换成十六进制的字符串形式
    static String formatText(byte[] bytes) {
        int len = bytes.length
        StringBuilder buf = new StringBuilder(len * 2)
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f])
            buf.append(HEX_DIGITS[bytes[j] & 0x0f])
        }
        return buf.toString()
    }

}
