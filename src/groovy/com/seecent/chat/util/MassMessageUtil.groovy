package com.seecent.chat.util

import com.seecent.chat.message.MassMessage
import com.seecent.chat.status.MassMessageStatus
import com.seecent.chat.type.MessageType
import grails.converters.JSON

class MassMessageUtil {

    static String toJson(MassMessage msg) {

        msg.msgTime = System.currentTimeMillis()
        msg.status = MassMessageStatus.SENDING

        def data = [:]
        if(msg.isToAll) {
            data.filter = [is_to_all: true]
        }
        else if(null != msg.groupId) {
            data.filter = [is_to_all: false, group_id: msg.groupId]
        }
        else {
            def toUsers = []
            msg.toUsers?.each {
                toUsers << it
            }
            data.touser = toUsers
        }

        if(MessageType.NEWS == msg.type) {
            data.mpnews = [media_id: msg.mediaId]
            data.msgtype = "mpnews"
        }
        else if(MessageType.TEXT == msg.type) {
            data.text = [content: msg.content]
            data.msgtype = "text"
        }
        else if(MessageType.IMAGE == msg.type) {
            data.image = [media_id: msg.mediaId]
            data.msgtype = "image"
        }
        else if(MessageType.VOICE == msg.type) {
            data.voice = [media_id: msg.mediaId]
            data.msgtype = "voice"
        }
        else if(MessageType.VIDEO == msg.type) {
            if(msg.isToAll || null != msg.groupId) {
                data.mpvideo = [media_id: msg.mediaId]
                data.msgtype = "mpvideo"
            }
            else {
                data.video = [media_id: msg.mediaId]
                data.msgtype = "video"
            }
        }

        return (data as JSON).toString()
    }

    static String toPreviewJson(MassMessage msg) {

        msg.msgTime = System.currentTimeMillis()
        msg.status = MassMessageStatus.SENDING

        def data = [:]
        if(msg.touser) {
            data.touser = msg.touser
        }
        else if(msg.towxname) {
            data.towxname = msg.towxname
        }

        if(MessageType.NEWS == msg.type) {
            data.mpnews = [media_id: msg.mediaId]
            data.msgtype = "mpnews"
        }
        else if(MessageType.TEXT == msg.type) {
            data.text = [content: msg.content]
            data.msgtype = "text"
        }
        else if(MessageType.IMAGE == msg.type) {
            data.image = [media_id: msg.mediaId]
            data.msgtype = "image"
        }
        else if(MessageType.VOICE == msg.type) {
            data.voice = [media_id: msg.mediaId]
            data.msgtype = "voice"
        }
        else if(MessageType.VIDEO == msg.type) {
            data.mpvideo = [media_id: msg.mediaId]
            data.msgtype = "mpvideo"
        }

        return (data as JSON).toString()
    }

}
