package com.trunksoft.chat.util

import grails.util.Environment
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings

import javax.sql.DataSource

import static org.jooq.impl.DSL.using

class DSLContextUtil {
    static DSLContext getDSLContext(DataSource dataSource) {
//        def dialect = Environment.current == Environment.PRODUCTION ? SQLDialect.MYSQL : SQLDialect.H2
//        def nameStyle = Environment.current == Environment.PRODUCTION ? RenderNameStyle.LOWER : RenderNameStyle.UPPER
//        return using(dataSource, dialect, new Settings(renderSchema: false, renderNameStyle: nameStyle))
        return using(dataSource, SQLDialect.MYSQL, new Settings(renderSchema: false, renderNameStyle: RenderNameStyle.LOWER))
    }
}
