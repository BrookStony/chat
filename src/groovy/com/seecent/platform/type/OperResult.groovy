package com.seecent.platform.type

public enum OperResult {
    SUCCESS, PARTIALSUCCESS, FAILURE

    public static OperResult codeOf(int code){
        switch (code){
            case 1: return SUCCESS
            case 2: return PARTIALSUCCESS
            case 3: return FAILURE
            default: return null
        }
    }
}