package com.trunksoft.chat.common

import com.trunksoft.platform.type.FieldType
import com.trunksoft.platform.type.FilterOperator

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
