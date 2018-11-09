package com.seecent.chat.common

import grails.converters.JSON
import com.seecent.chat.alert.Alert
import com.seecent.chat.alert.AlertCronTrigger
import com.seecent.chat.alert.AlertHistory
import com.seecent.chat.alert.AlertType
import com.seecent.platform.auth.Role
import com.seecent.platform.auth.User
import com.seecent.platform.auth.UserRole

class JsonMarshallerService {

    def grailsApplication

    def registerObjectMarshaller() {
        def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib');
        def format = 'yyyy-MM-dd HH:mm:ss'
//        JSON.registerObjectMarshaller(AlertType) { AlertType a ->
//            def trigger = a.trigger
//            return [id     : a.id, enable: a.enable, label: a.label, severity: a.severity.name(),
//                    count  : a.count, lastTime: a.lastTime.format(format),
//                    trigger: [id: trigger.id, name: trigger.name, description: trigger.description]]
//        }
//        JSON.registerObjectMarshaller(Alert) { Alert a ->
//            return [id       : a.id, severity: a.severity.name(), message: a.message, accountId: a.accountId,
//                    isRead   : a.isRead, category: a.category, source: a.source,
//                    occurTime: a.occurTime.format(format)]
//        }
//        JSON.registerObjectMarshaller(AlertHistory) { AlertHistory a ->
//            return [id       : a.id, alertId: a.alert.id, severity: a.severity.name(), message: a.message,
//                    isRead   : a.isRead, category: a.category, source: a.source,
//                    occurTime: a.occurTime.format(format)]
//        }
        JSON.registerObjectMarshaller(User) { User u ->
            return [id         : u.id, username: u.username, name: u.username, email: u.email, phone: u.phone,
                    userGroup  : [id: u.userGroup?.id, name: u.userGroup?.name],
                    dateCreated: u.dateCreated.format(format), lastUpdated: u.lastUpdated,
                    roles      : u.authorities.collect {
                        [id: it.id, name: it.name, authority: it.authority]
                    }
            ]
        }
//        JSON.registerObjectMarshaller(Schedule) { Schedule s ->
//            [id: s.id, weekDay: s.weekDay.ordinal(), startHour: s.startHour, endHour: s.endHour]
//        }
    }
}
