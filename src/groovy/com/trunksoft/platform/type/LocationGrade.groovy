package com.trunksoft.platform.type

public enum LocationGrade {
    FIRSTTIER, SECONDTIER, THIRDTIER, FOURTHTIER, FIFTHTIER

    static LocationGrade codeOf(int code){
        switch (code) {
            case 1: return FIRSTTIER
            case 2: return SECONDTIER
            case 3: return THIRDTIER
            case 4: return FOURTHTIER
            case 5: return FIFTHTIER
            default: return FIFTHTIER
        }
    }
}