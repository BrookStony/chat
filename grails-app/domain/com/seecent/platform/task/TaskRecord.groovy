package com.seecent.platform.task

import com.seecent.platform.DateBase

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
