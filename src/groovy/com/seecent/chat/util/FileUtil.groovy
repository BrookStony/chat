package com.seecent.chat.util

class FileUtil {

    static String suffixType(String fileName) {
        int index = fileName.indexOf(".")
        if(-1 != index) {
            return fileName.substring(index + 1, fileName.length())
        }
        return null
    }

    static String createFileName(String uuid, String suffix) {
        return uuid + "." + suffix
    }

    static String getFileType(String suffixType) {
        switch (suffixType.toLowerCase()) {
            case "jpg" :
            case "jpeg" :
            case "png" :
            case "gif" :
            case "bmp" :
                return "image"
            case "js" :
                return "js"
            case "css" :
                return "css"
        }
    }
}
