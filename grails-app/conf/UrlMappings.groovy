class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
        //north api
        "/rest/message/receive"(controller:"receiveMessage", action:"index")
        "/rest/message/alarm"(controller:"chatMessageApi", action:"sendAlarmMsg")
        "/rest/message/clearAlarm"(controller:"chatMessageApi", action:"sendClearAlarmMsg")
        "/rest/message/textMsg"(controller:"chatMessageApi", action:"sendTextMsg")
        "/rest/message/template"(controller:"chatMessageApi", action:"sendTemplateMsg")
        "/rest/message/msgStatus"(controller:"chatMessageApi", action:"searchMsgStatus")
        "/rest/massMessage/send"(controller:"chatMassMessageApi", action:"send")
        "/rest/customMessage/send"(controller:"chatCustomMessageApi", action:"send")
        "/rest/member/list"(controller:"memberApi", action:"list")
        "/rest/member/update"(controller:"memberApi", action:"update")
        "/rest/member/show"(controller:"memberApi", action:"show")
        "/rest/member/listMembers"(controller:"memberApi", action:"listMembers")

        "/login"(controller: 'login', action: 'auth')
        "/"(controller: 'home', action: 'index')
        "/articles"(controller: 'article')
        "/audios"(controller: 'audio')
        "/pictures"(controller: 'picture')
        "/videos"(controller: 'video')
        "/structureTrees"(resources: 'structureTree')
        "/accounts"(resources: 'account')
        "/alerts"(resources: 'alert')
        "/alertHistorys"(resources: 'alertHistory')
        "/alertTypes"(resources: 'alertType')
        "/weChatMenus"(resources: 'weChatMenu')
        "/members"(resources: 'member')
        "/memberGroups"(resources: 'memberGroup')
        "/chatMessages"(resources: 'chatMessage')
        "/massMessages"(resources: 'chatMassMessage')
        "/templateMessages"(resources: 'templateMessage')
        "/messageTemplates"(resources: 'messageTemplate')
        "/autoReplyRules"(resources: 'autoReplyRule')
        "/bindAccountSettings"(resources: 'bindAccountSetting')
        "/tasks"(resources: 'task')
        "/users"(resources: 'user')
        "/roles"(resources: 'role')
        "/userGroups"(resources: 'userGroup')
        "/menus"(resources: 'menu')
        "/notifications"(resources: 'notification')
        "/operlogs"(resources: 'operlog')
        "/settings"(resources: 'setting')
        "/pictureGroups"(resources: 'pictureGroup')
        "500"(view: '/error')
	}
}
