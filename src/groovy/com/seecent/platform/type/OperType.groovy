package com.seecent.platform.type

public enum OperType {
    LOGIN, LOGOUT, ADD, UPDATE, REMOVE, SYNC, IMPORT

    public static OperType codeOf(int code){
        switch (code){
            case 1: return LOGIN
            case 2: return LOGOUT
            case 3: return ADD
            case 4: return UPDATE
            case 5: return REMOVE
            case 6: return SYNC
            case 7: return IMPORT
            default: return null
        }
    }
}