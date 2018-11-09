package com.seecent.platform.common

import org.quartz.CronExpression

abstract class CronTrigger {
    String name
    String cron
    String description

    Date dateCreated
    Date lastUpdated

    static constraints = {
        name(blank: false)
        cron(blank: false, validator: { val, obj ->
            CronExpression.isValidExpression(val)
        })
        description()
        dateCreated()
        lastUpdated()
    }
}
