package com.trunksoft.platform.system

import com.trunksoft.platform.auth.Role
import com.trunksoft.platform.auth.RoleMenu
import com.trunksoft.platform.auth.User

class MenuService {

    def findMenuTree(User user) {
        def menuTree = []
        if(user) {
            Set<Menu> menus = new HashSet<Menu>()
            Set<Role> roles = user.getAuthorities()
            for(Role r : roles) {
                RoleMenu.where {
                    role == r
                }.list().each {
                    menus.add(it.menu)
                }
            }

            for(Menu menu : menus) {
                if(menu.enable && 1 == menu.level) {
                    menuTree << [menu: menu]
                }
            }
            menuTree.sort { a, b ->
                a.menu.sortno <=> b.menu.sortno
            }

            menuTree.each {
                it.children = buildChildren(it.menu, menus)
            }
        }
        return menuTree
    }

    private def buildChildren(parent, Set<Menu> menus) {
        def children = []
        for(Menu menu : menus) {
            if(menu.level == parent.level + 1 && menu.parent == parent) {
                children << menu
            }
        }
        children.sort { a, b ->
            a.sortno <=> b.sortno
        }
        return children
    }
}
