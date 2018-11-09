package com.seecent.platform.system

import com.seecent.platform.DateBase
import org.apache.commons.lang.builder.HashCodeBuilder

class Menu extends DateBase {

    Menu parent
    String code
    String name
    String url
    String image
    String cssicon
    Integer level
    Integer sortno
    Boolean enable = true

    static constraints = {
        parent(nullable: true)
        url(nullable: true)
        image(nullable: true)
        cssicon(nullable: true)
    }

    static mapping = {
        id(generator: 'assigned')
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Menu)) return false

        Menu that = (Menu) o

        if (id != that.id) return false

        return true
    }

    int hashCode() {
        int result
        result = id.hashCode()
        return result
    }
}
