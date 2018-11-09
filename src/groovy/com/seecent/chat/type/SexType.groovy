package com.seecent.chat.type

public enum SexType {
    UNKNOWN, MALE, FEMALE

    public static SexType codeOf(int code){
        switch (code){
            case 1: return MALE
            case 2: return FEMALE
            default: return UNKNOWN
        }
    }
}