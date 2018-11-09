package com.trunksoft.chat.module.screen

import com.trunksoft.chat.Account
import com.trunksoft.platform.DateBase

class ChatScreen extends DateBase {

    static belongsTo = [account: Account]

    static hasMany = [memberIds: Long, blackList: Long, photos: ScreenPhoto]

    ScreenLogo logo
    String name //活动名称
    List<Long> memberIds //上墙用户
    Set<Long> blackList //用户黑名单
    Date beginDate //开始时间
    Date endDate //结束时间

    ChatScreenJoinType joinType = ChatScreenJoinType.DFAULT
    String keyword
    Boolean disPlayQrCode = false
    Boolean msgAudit = false
    Boolean msgRolling = true
    Integer msgRollingTime = 5

    String description

    ChatScreenStatus status

    static constraints = {
        logo(nullable: true)
        beginDate(nullable: true)
        endDate(nullable: true)
        memberIds(nullable: true)
        blackList(nullable: true)
        photos(nullable: true)
        keyword(nullable: true)
        description(nullable: true)
    }

    static mapping = {
        joinType enumType: 'ordinal'
        status enumType: 'ordinal'
    }
}
