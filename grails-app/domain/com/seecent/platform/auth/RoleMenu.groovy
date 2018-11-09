package com.seecent.platform.auth

import com.seecent.platform.system.Menu
import org.apache.commons.lang.builder.HashCodeBuilder

class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1

    Role role
    Menu menu

    boolean equals(other) {
        if (!(other instanceof RoleMenu)) {
            return false
        }

        other.menu?.id == menu?.id &&
                other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (menu) builder.append(menu.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static RoleMenu get(long menuId, long roleId) {
        RoleMenu.where {
            menu == Menu.load(menuId) &&
                    role == Role.load(roleId)
        }.get()
    }

    static RoleMenu create(Menu menu, Role role, boolean flush = false) {
        new RoleMenu(menu: menu, role: role).save(flush: flush, insert: true)
    }

    static boolean remove(Menu m, Role r, boolean flush = false) {

        int rowCount = RoleMenu.where {
            menu == Menu.load(m.id) &&
                    role == Role.load(r.id)
        }.deleteAll()

        rowCount > 0
    }

    static boolean remove(Long menuId, Long roleId, boolean flush = false) {

        int rowCount = RoleMenu.where {
            menu == Menu.load(menuId) &&
                    role == Role.load(roleId)
        }.deleteAll()

        rowCount > 0
    }

    static void removeAll(Menu m) {
        RoleMenu.where {
            menu == Menu.load(m.id)
        }.deleteAll()
    }

    static void removeAll(Role r) {
        RoleMenu.where {
            role == Role.load(r.id)
        }.deleteAll()
    }

    static void removeAllByRoleId(Long id) {
        RoleMenu.where {
            role == Role.load(id)
        }.deleteAll()
    }

    static mapping = {
        id composite: ['role', 'menu']
        version false
    }
}
