package com.trunksoft.chat.db

import com.trunksoft.chat.assets.Material
import com.trunksoft.chat.jooq.tables.ChatChatMessage
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.NewsArticle
import com.trunksoft.chat.message.NewsMessage
import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.type.MessageType
import com.trunksoft.chat.util.DSLContextUtil
import groovy.sql.Sql
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.jooq.conf.ParamType

import javax.sql.DataSource

import static org.jooq.impl.DSL.field

class ChatMessageDBService {

    static transactional = false

    def articleService
    def materialManageService

    DataSource dataSource

    def list(GrailsParameterMap params) {
        def sql = new Sql(dataSource)
        def offset = params.int('offset')
        def max = params.int('max')
        def accountId = params.long('accountId')
        def beginTime = params.long('beginTime')
        def endTime = params.long('endTime')
        def c = ChatChatMessage.CHAT_CHAT_MESSAGE.as('c')
        def dsl = DSLContextUtil.getDSLContext(dataSource)
        def pCondition = c.ACCOUNT_ID.equal(accountId).and(c.STATUS.gt(4))
        if (beginTime && endTime) {
            pCondition = pCondition.and(c.MSG_TIME.lessOrEqual(endTime).and(c.MSG_TIME.greaterOrEqual(beginTime)))
        }
        else if(beginTime) {
            pCondition = pCondition.and(c.MSG_TIME.greaterOrEqual(beginTime))
        }
        if(params.filter){
            pCondition = pCondition.and(c.CONTENT.like("%${params.filter}%".toString()))
        }
        def onStep = dsl.select(
                c.ID.as('id'),
                c.ACCOUNT_ID.as('accountId'),
                c.FROM_USER_ID.as('fromUserId'),
                c.TO_USER_ID.as('toUserId'),
                c.MSG_TIME.as('msgTime'),
                c.MSGID.as('msgId'),
                c.STATUS.as('status'),
                c.TYPE.as('type'),
                c.CONTENT.as('content'),
                c.MEDIA_ID.as('mediaId'),
                c.THUMB_MEDIA_ID.as('thumbMediaId'),
                c.VOICE_FORMAT.as('voiceFormat'),
                c.TITLE.as('title'),
                c.DESCRIPTION.as('description'),
                c.MATERIAL_ID.as('materialId'))
                .from(c)
                .where(pCondition)

        def sortField = c.MSG_TIME.desc()
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
            def material = null
            if(null != it.fromUserId){
                fromMember = Member.get(it.fromUserId as Long)
            }
            if(null != it.toUserId){
                toMember = Member.get(it.toUserId as Long)
            }
            if(MessageType.IMAGE.ordinal() == it.type && null != it.materialId){
                material = Material.get(it.materialId as Long)
            }
            def articles = null
            if(MessageType.NEWS.ordinal() == it.type) {
                def newsMessage = NewsMessage.get(it.id as Long)
                if (newsMessage && newsMessage.articles) {
                    articles = newsMessage.articles.collect { article ->
                        buildNewsArticle(article)
                    }
                }
            }
            return [id: it.id, accountId: it.accountId,
                    msgTime: new Date(it.msgTime).format("yyyy-MM-dd HH:mm:ss"),
                    msgId: it.msgId,
                    status: it.status,
                    fromUser: buildMember(fromMember),
                    toUser: buildMember(toMember),
                    material: buildMaterial(material),
                    articles: articles,
                    type: it.type,
                    content: it.content,
                    mediaId: it.mediaId,
                    thumbMediaId: it.thumbMediaId,
                    voiceFormat: it.voiceFormat,
                    title: it.title,
                    description: it.description
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

    private def buildMaterial(Material m) {
        if(m){
            if(MaterialType.PICTURE == m.materialType) {
                return [id: m.id, name: m.name, path: m.path,
                        materialType: m.materialType.name(),
                        url: materialManageService.serverUrl(m.url)]
            }
            else {
                return [id: m.id, name: m.name, path: m.path,
                        materialType: m.materialType.name()]
            }
        }
        return null
    }

    private def buildNewsArticle(NewsArticle it) {
        def url = it.url
        def picurl = materialManageService.serverUrl(it.coverImage)
        if(null != it.articleItemId) {
            url = articleService.articleItemServerUrl(it.articleItemId)
        }
        else if(null != it.articleId) {
            url = articleService.articleServerUrl(it.articleId)
        }
        [id: it.id, title:it.title, description: it.description, url: url, picurl: picurl]
    }

}
