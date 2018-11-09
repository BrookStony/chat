package com.trunksoft.chat.type

public enum VideoType {
    RM, RMVB, WMV, AVI, MPG, MPEG, MP4

    static VideoType codeOf(String type) {
        switch (type.toUpperCase()) {
            case "RM": return RM
            case "MP4": return MP4
            case "RMVB": return RMVB
            case "WMV": return WMV
            case "AVI": return AVI
            case "MPG": return MPG
            case "MPEG": return MPEG
            default: MP4
        }
    }
}