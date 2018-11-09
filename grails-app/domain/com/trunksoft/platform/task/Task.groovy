package com.trunksoft.platform.task

import com.trunksoft.platform.DateBase

class Task extends DateBase {

    public static final String SYNCHDOMAIN = "synchdomain"

    String name
    String label
    TaskType type
    TaskStatus status

    static constraints = {
        label(nullable: true)
    }

    static mapping = {
        type enumType: 'ordinal'
        status enumType: 'ordinal'
    }
}
