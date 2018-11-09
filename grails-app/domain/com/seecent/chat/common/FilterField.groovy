package com.seecent.chat.common

import com.seecent.platform.type.FieldType
import com.seecent.platform.type.FilterOperator

class FilterField {

    String name
    FieldType type
    FilterOperator operator
    String value

    static belongsTo = [filter: Filter]

    static constraints = {
        name(blank: false)
        value(nullable: true)
    }

}
