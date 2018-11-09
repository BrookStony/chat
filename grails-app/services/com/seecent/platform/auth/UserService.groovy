package com.seecent.platform.auth

import com.seecent.chat.Account

class UserService {

    static boolean transactional = false

    def springSecurityService

    boolean isSuperAdmin(User user) {
        for(Role role : user.authorities) {
            if(Role.ROLE_SYSTEM_ADMIN == role.authority) {
                return true
            }
        }
        return false
    }

    User currentUser() {
        return springSecurityService.currentUser
    }

    def findAllAccounts(User user) {
        if(isSuperAdmin(user)) {
            return Account.listOrderByName()
        }
        else {
            if(user.userGroup) {
                def accounts = user.userGroup.accounts
                if(accounts && !accounts.empty ) {
                    return accounts
                }
            }
        }
        return []
    }
}
