package com.seecent.chat.type

public enum MaterialType {
    PICTURE("images"), AUDIO("audios"), VIDEO("videos"), UNKNOWN("unknown")

    private String dir

    MaterialType(String dir) {
        this.dir = dir
    }

    public String getDir() {
        return dir
    }

    static MaterialType suffixOf(String suffix) {
        switch (suffix.toLowerCase()) {
            case "jpg" :
            case "jpeg" :
            case "png" :
            case "gif" :
            case "bmp" :
            default:
                return UNKNOWN
        }
    }
}