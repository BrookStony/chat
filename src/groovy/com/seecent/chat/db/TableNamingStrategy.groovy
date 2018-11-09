package com.seecent.chat.db

import groovy.transform.CompileStatic
import org.hibernate.cfg.ImprovedNamingStrategy

@CompileStatic
class TableNamingStrategy extends ImprovedNamingStrategy {
    /**
     * @param className
     * @return tableName
     */
    public static final String PREFIX = "chat_"

    String classToTableName(String className) {
        return PREFIX + super.classToTableName(className)
    }
}
