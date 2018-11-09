package com.trunksoft.platform.task

import com.trunksoft.platform.DateBase

class TaskRecord extends DateBase {

    static belongsTo = [task: Task]

    Date beginTime
    Date endTime
    Integer costTime
    Double progress = 0.0
    TaskResult result
    TaskStatus status

    static constraints = {
        beginTime(nullable: true)
        endTime(nullable: true)
        costTime(nullable: true)
    }

    static mapping = {
        result enumType: 'ordinal'
        status enumType: 'ordinal'
    }
}
