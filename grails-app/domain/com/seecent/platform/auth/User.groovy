package com.seecent.platform.auth

import com.seecent.platform.DateBase
import com.seecent.platform.system.UserGroup

class User extends DateBase {

    transient springSecurityService

    UserGroup userGroup
    String username
    String password
    String name
    String email
    String phone
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false
    boolean initCompleted = true

    static transients = ['springSecurityService']

    static constraints = {
        userGroup(nullable: true)
        username blank: false, unique: true
        password blank: false
        name(nullable: true)
        email(nullable: true, email: true)
        phone(nullable: true)
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
}
