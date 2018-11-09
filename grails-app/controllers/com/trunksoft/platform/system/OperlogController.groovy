package com.trunksoft.platform.system

import grails.converters.JSON

import java.text.DateFormat
import java.text.SimpleDateFormat

import grails.converters.JSON
import grails.transaction.Transactional
import com.trunksoft.chat.Account
import com.trunksoft.platform.auth.Role
import com.trunksoft.platform.auth.UserRole
import com.trunksoft.platform.type.OperResult
import com.trunksoft.platform.type.OperType

import java.text.DateFormat
import java.text.SimpleDateFormat

@Transactional(readOnly = true)
class OperlogController {

    def springSecurityService

    private final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)
        params.offset = params.offset ?: 0
        params.sort = params.sort ?: 'operTime'
        params.order = params.order ?: 'desc'

        def startDate = params.getDate("startDate", "yyyy-MM-dd")
        def endDate = params.getDate("endDate", "yyyy-MM-dd")?.minus(-1)
//        def filters = JSON.parse(params.remove('filters') as String)
//        def filter = params.filter
//        def type = OperType.codeOf(filters.remove("type").id as Integer)
//        def result = OperResult.codeOf(filters.remove("result").id as Integer)

        def accountIds = params.accountId
        def accounts = Account.getAll(accountIds)
        def superAdminRole = Role.findByAuthority(Role.ROLE_SYSTEM_ADMIN)
        def users = UserRole.findAllByRole(superAdminRole)*.user
        def operlogList = Operlog.createCriteria().list(params) {
            between("operTime",startDate,endDate)
//            if(filter){
//                like("detail", "%" + filter + "%")
//            }
//            if(type){
//                eq("type", type)
//            }
//            if(result){
//                eq("result", result)
//            }
            if(accounts && accounts.size() == 1){
                'in'("account", accounts)
            }
            if(superAdminRole){
                if(!currentUserIsSuperAdmin(superAdminRole)){
                    if(users.size() > 0){
                        not{'in'("user",users)}
                    }
                }
            }
        }
        response.setHeader("totalItems", operlogList.totalCount as String)
        def data = operlogList.collect {
            return [id: it.id, operTime: it.opertime.format("yyyy-MM-dd hh:mm:ss"),
                    type: it.type.ordinal(), source: it.source,
                    category: it.category, result: it.result.ordinal(),
                    detail: it.detail, ip: it.ip,
                    user: [id: it.user?.id, username: it.user?.username],
                    account: [id: it.account?.id, username: it.account?.username]]
        }
        respond data
    }

    private boolean currentUserIsSuperAdmin(superAdminRole) {
        def currentUser = springSecurityService.currentUser
        return currentUser.authorities.contains(superAdminRole)
    }

}
