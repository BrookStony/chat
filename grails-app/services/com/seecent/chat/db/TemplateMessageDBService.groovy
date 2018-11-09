package com.seecent.chat.db

import com.seecent.chat.jooq.tables.ChatTemplateMessage
import com.seecent.chat.jooq.tables.ChatMessageTemplate
import com.seecent.chat.member.Member
import com.seecent.chat.util.DSLContextUtil
import groovy.sql.Sql
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.jooq.conf.ParamType

import javax.sql.DataSource

import static org.jooq.impl.DSL.field
import static org.jooq.impl.DSL.field

class TemplateMessageDBService {

    static transactional = false

    DataSource dataSource

    def list(GrailsParameterMap params) {
        def sql = new Sql(dataSource)
        def offset = params.int('offset')
        def max = params.int('max')
        def accountId = params.long('accountId')
        def beginTime = params.long('beginTime')
        def endTime = params.long('endTime')
        def m = ChatTemplateMessage.CHAT_TEMPLATE_MESSAGE.as('m')
        def t = ChatMessageTemplate.CHAT_MESSAGE_TEMPLATE.as('t')
        def dsl = DSLContextUtil.getDSLContext(dataSource)
        def pCondition = m.ACCOUNT_ID.equal(accountId)
        if (beginTime && endTime) {
            pCondition = pCondition.and(m.MSG_TIME.lessOrEqual(endTime).and(m.MSG_TIME.greaterOrEqual(beginTime)))
        }
        else if(beginTime) {
            pCondition = pCondition.and(m.MSG_TIME.greaterOrEqual(beginTime))
        }
        if(params.filter){
            pCondition = pCondition.and(m.CONTENT.like("%${params.filter}%".toString()))
        }
        def onStep = dsl.select(
                m.ID.as('id'),
                m.ACCOUNT_ID.as('accountId'),
                m.FROM_USER_ID.as('fromUserId'),
                m.TO_USER_ID.as('toUserId'),
                m.MSG_TIME.as('msgTime'),
                m.MSGID.as('msgId'),
                m.STATUS.as('status'),
                m.TYPE.as('type'),
                m.CONTENT.as('content'),
                t.ID.as('templateId'),
                t.TITLE.as('title'))
                .from(m).leftOuterJoin(t).on(m.TEMPLATE_ID.equal(t.ID))
                .where(pCondition)

        def sortField = m.MSG_TIME.desc()
        if (params.order == "asc") {
            sortField = field(params.sort as String).asc()
        } else {
            sortField = field(params.sort as String).desc()
        }
        def count = onStep.fetchCount()
        def querySQL = onStep.orderBy(sortField).getSQL(ParamType.INLINED)
        return [count, sql.rows(querySQL, offset + 1, max).collect {
            def fromMember = null
            def toMember = null
            if(it.fromUserId){
                fromMember = Member.get(it.fromUserId as Long)
            }
            if(it.toUserId){
                toMember = Member.get(it.toUserId as Long)
            }
            return [id: it.id, accountId: it.accountId,
                    msgTime: new Date(it.msgTime).format("yyyy-MM-dd HH:mm:ss"),
                    msgId: it.msgId,
                    status: it.status,
                    fromUser: buildMember(fromMember),
                    toUser: buildMember(toMember),
                    type: it.type,
                    content: it.content,
                    title: it.title,
                    templateId: it.templateId
            ]
        }]
    }

    private def buildMember(Member m) {
        if(m){
            return [id: m.id, name: m.name, nickName: m.nickName,
                    headimgurl: m.headimgurl, sex: m.sex.ordinal(),
                    city: m.city, province: m.province,
                    groupId: m.group?.id, groupName: m.group?.name]
        }
        return null
    }

}

