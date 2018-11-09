package com.seecent.chat

class HomeController {

    def menuService
    def userService

    def wechatAccountService

    def index() {

        def currentUser = userService.currentUser()
        def accounts = userService.findAllAccounts(currentUser).collect {
            [id: it.id, name: it.name, weixin: it.weixin]
        }

        def menuTree = menuService.findMenuTree(currentUser)

        return [accounts: accounts, menuTree: menuTree, currentUser: currentUser]
    }

}
