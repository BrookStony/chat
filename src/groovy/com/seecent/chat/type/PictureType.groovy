package com.seecent.chat.type

public enum PictureType {
    BMP, PNG, JPEG, JPG, GIF

    static PictureType codeOf(String type) {
        switch (type.toUpperCase()) {
            case "JPG": return JPG
            case "JPEG": return JPEG
            case "GIF": return GIF
            case "PNG": return PNG
            case "BMP": return BMP
            default: JPG
        }
    }
}