package com.trunksoft.platform.auth

import com.trunksoft.platform.system.Menu
import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class RoleController {

    def springSecurityService

    def index() {
        def superAdminRole = Role.findByAuthority(Role.ROLE_SYSTEM_ADMIN)
        def isSuperAdmin = superAdminRole && !currentUserIsSuperAdmin(superAdminRole)
        def criteria = Role.where {
            if(isSuperAdmin) {
                authority != Role.ROLE_SYSTEM_ADMIN
            }
            if (params.filter) {
                name =~ "%${params.filter}%"
            }
        }
        def roleCount = criteria.count()
        def roleList = criteria.list(params)
        response.setHeader('totalItems', roleCount as String)
        respond roleList
    }

    def show(Role roleInstance) {
        respond roleInstance
    }

    def create() {
        respond new Role(params)
    }

    @Transactional
    def save(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }
        if (roleInstance.hasErrors()) {
            respond roleInstance.errors, view: 'create'
            return
        }
        roleInstance.save failOnError: true
        respond roleInstance, [status: CREATED]
    }

    def edit(Role roleInstance) {
        respond roleInstance
    }

    @Transactional
    def update(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }
        if (roleInstance.hasErrors()) {
            respond roleInstance.errors, view: 'edit'
            return
        }
        roleInstance.save flush: true
        respond roleInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def roleInstance = Role.get(params.id)
        if (roleInstance == null) {
            notFound()
            return
        }
        UserRole.removeAll(roleInstance)
        RoleMenu.removeAll(roleInstance)
        roleInstance.delete()
        render status: NO_CONTENT
    }

    def menuTree(Long roleId) {
        def menuMap = [:]
        RoleMenu.where {
            role == Role.load(roleId)
        }.list().each {
            menuMap.put(it.menu.id, it.menu.id)
        }
        def criteria = Menu.createCriteria().list {
            eq("enable", true)
            order("level")
            order("sortno")
        }

        def datas = criteria.collect {
            def isParent = it.parent == null
            def checked = menuMap.get(it.id) != null
            [cls: 'menu', id: it.id, pId: (it.parent != null ? it.parent.id : 0),
             name: it.id + ":" + it.name,
             isParent: isParent, checked: checked, open: isParent && checked]
        }
        respond datas
    }

    def asyncMenuTree() {
        def id = params.long("id")
        def roleId = params.long("roleId")
        def menuMap = [:]
        RoleMenu.where {
            role == Role.load(roleId)
        }.list().each {
            menuMap.put(it.menu.id, it.menu.id)
        }
        def criteria = Menu.createCriteria().list {
            eq("enable", true)
            if(null != id) {
               parent {
                   eq("id", id)
               }
            }
            else {
                isNull("parent")
            }
            order("sortno")
        }

        def datas = criteria.collect {
            def isParent = it.parent == null
            def checked = menuMap.get(it.id) != null
            [cls: 'menu', id: it.id, pId: (it.parent != null ? it.parent.id : 0),
             name: it.id + ":" + it.name,
             isParent: isParent, checked: checked, open: isParent && checked]
        }
        respond datas
    }

    @Transactional
    def saveMenus() {
        def data = request.JSON
        def roleId = data.id as Long
        def menuIds = data.menuIds as List<Long>
        def roleInstance = Role.get(roleId)
        if (roleInstance == null) {
            notFound()
            return
        }
        if(menuIds && menuIds.size() > 0) {
            def existIds = []
            RoleMenu.where {
                role == Role.load(roleId)
            }.list().each {
                existIds << it.menu.id
            }
            //新增
            (menuIds - existIds).each { id ->
                RoleMenu.create(Menu.proxy(id), roleInstance, true)
            }
            //删除
            (existIds - menuIds).each { Long id ->
                RoleMenu.remove(id, roleId, true)
            }
        }
        else {
            RoleMenu.removeAllByRoleId(roleId)
        }
        render status: OK
    }

    protected void notFound() {
        render status: NOT_FOUND
    }

    private boolean currentUserIsSuperAdmin(superAdminRole) {
        def currentUser = springSecurityService.currentUser
        return currentUser.authorities.contains(superAdminRole)
    }

}
