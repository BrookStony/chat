package com.seecent.chat.type

public enum AudioType {
    MP3, WMA, WAV, AMR

    static AudioType codeOf(String type) {
        switch (type.toUpperCase()) {
            case "MP3": return MP3
            case "WMA": return WMA
            case "WAV": return WAV
            case "AMR": return AMR
            default: MP3
        }
    }
}