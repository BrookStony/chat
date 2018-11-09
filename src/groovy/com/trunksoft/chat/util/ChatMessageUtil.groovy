package com.trunksoft.chat.util

import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.ImageMessage
import com.trunksoft.chat.message.MessageTemplate
import com.trunksoft.chat.message.MusicMessage
import com.trunksoft.chat.message.NewsMessage
import com.trunksoft.chat.message.TemplateMessage
import com.trunksoft.chat.message.TextMessage
import com.trunksoft.chat.message.VideoMessage
import com.trunksoft.chat.message.VoiceMessage
import com.trunksoft.chat.status.ChatMessageStatus
import grails.converters.JSON

class ChatMessageUtil {

    static String toJson(String touser, String content) {
        StringBuilder sb = new StringBuilder()
        sb.append("{\"touser\":")
        sb.append(touser)
        sb.append(",\"msgtype\":\"text\"")
        sb.append(",\"text\":{\"content\":\"")
        sb.append(content?.replaceAll("\"", "\\\\\""))
        sb.append("\"}}")
        println "sb: " + sb.toString()
        return sb.toString()
    }

    static String toJson(TextMessage msg) {
        msg.msgTime = System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        return toJson("\"${msg.toUser.openId}\"".toString(), msg.content)
    }

    static String toJson(ImageMessage msg) {
        msg.msgTime =  System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        return ([touser: msg.toUser.openId, msgtype: "image", image: [media_id: msg.mediaId]] as JSON).toString()
    }

    static String toJson(VoiceMessage msg) {
        msg.msgTime = System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        return ([touser: msg.toUser.openId, msgtype: "voice", voice: [media_id: msg.mediaId]] as JSON).toString()
    }

    static String toJson(VideoMessage msg) {
        msg.msgTime = System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        return ([touser: msg.toUser.openId, msgtype: "video", video: [media_id: msg.mediaId, thumb_media_id: msg.thumbMediaId, title: msg.title, description: msg.description]] as JSON).toString()
    }

    static String toJson(MusicMessage msg) {
        msg.msgTime =  System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        return ([touser: msg.toUser.openId, msgtype: "music", music: [musicurl: msg.musicurl, hqmusicurl: msg.hqmusicurl, thumb_media_id: msg.thumbMediaId, title: msg.title, description: msg.description]] as JSON).toString()
    }

    static String toJson(NewsMessage msg) {
        msg.msgTime =  System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        def articles = []
        msg.articles?.each{
            articles << [no: it.no, title: it.title, description: it.description, url: it.url, picurl: it.picurl]
        }
        return ([touser: msg.toUser.openId, msgtype: "news", news: [articles: articles.sort{a,b-> a.no<=>b.no}]] as JSON).toString()
    }

    static String toJson(TemplateMessage msg) {
        msg.msgTime = System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        def msgMap = [touser: msg.toUser.openId, template_id: msg.template.templateId, data: msg.data]
        if(msg.url) {
            msgMap.url = msg.url
        }
        if(msg.topColor) {
            msgMap.topcolor = msg.topColor
        }
        msg.data.each{
            it.value.color = msg.topColor
        }
        return (msgMap as JSON).toString()
    }

    static String templateContent(MessageTemplate template, data) {
        def content = template.content
        data.each{ e ->
            content = content.replace("{{" + e.key + ".DATA}}", e.value?.value?.toString())
        }
        return content
    }

    static String toJson(TextMessage msg, List<Member> members){
        msg.msgTime = System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        def openIds =[]
        members.each {
            openIds << it.openId
        }
        return toJson((openIds as JSON).toString(), msg.content)
    }

    static String toJson(ImageMessage msg, List<Member> members) {
        msg.msgTime =  System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        def openIds =[]
        members.each {
            openIds << it.openId
        }
        return ([touser: openIds, image: [media_id: msg.mediaId], msgtype: "image"] as JSON).toString()
    }

    static String toJson(NewsMessage msg,  List<Member> members, String media_id) {
        msg.msgTime =  System.currentTimeMillis()
        msg.status = ChatMessageStatus.SENDING
        def openIds =[]
        members.each {
            openIds << it.openId
        }
        return ([msg: openIds, mpnews: [media_id: media_id], msgtype: "news"] as JSON).toString()
    }

    static String toJson(Article article) {
        def articles = []
        articles << [thumb_media_id: article.mediaId, author: article.author,
                title: article.title, content_source_url: article.originalUrl,
                content: article.content, digest: article.description,
                show_cover_pic:article.coverDisplayInText]

        if(article.articleItems && article.articleItems.size() > 0) {
            def articleItems = article.articleItems.sort{a,b-> a.no<=>b.no}
            articleItems.each {
                articles << [thumb_media_id: it.coverImageId, author: it.author,
                             title: it.title, content_source_url: it.originalUrl,
                             content: it.content, digest: it.description,
                             show_cover_pic:it.coverDisplayInText]
            }
        }
        return ([articles: articles] as JSON).toString()
    }
}
