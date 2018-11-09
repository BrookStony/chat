package com.seecent.chat.type

public enum ChatMenuType {
    CLICK("click"), VIEW("view")

    String name

    public ChatMenuType(String name) {
        this.name = name
    }

    static ChatMenuType codeOf(int code) {
        switch (code) {
            case CLICK.ordinal(): return CLICK
            default: return VIEW
        }
    }
}