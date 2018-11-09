package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.chat.common.Tag
import com.trunksoft.chat.services.MaterialResult
import com.trunksoft.platform.DateBase
import com.trunksoft.chat.type.MaterialType

class Material extends DateBase {

    static belongsTo = [account: Account]

    static hasMany = [tags: Tag]

    String uuid
    MaterialType materialType
    Date createTime
    String mediaId
    String mediaUrl
    String name
    String path
    String url
    String fileType
    Long mediaSize = 0l
    Long costTime
    String description
    Boolean article = false
    Integer errcode
    String errmsg
    Long expiresTime = 3*24*3600

    static constraints = {
        uuid(nullable: true)
        mediaId(nullable: true)
        mediaUrl(nullable: true)
        createTime(nullable: true)
        name(nullable: true)
        path(nullable: true)
        url(nullable: true)
        fileType(nullable: true)
        costTime(nullable: true)
        description(nullable: true)
        errcode(nullable: true)
        errmsg(nullable: true)
        tags(maxSize: 20)
    }

    static mapping = {
        materialType enumType: 'ordinal'
        errmsg(length: 2000)
        tags joinTable: [name: "chat_material_tag", key: 'material_id', column: 'tag_id'], batchSize: 10, index: 'Material_Tag_Idx'
    }

    boolean isTimeOut(){
        //永久素材不会超时
        if(mediaId && mediaUrl) {
            return false
        }
        //零时素材判断有效时间
        if(expiresTime && createTime){
            long elapsedTime =  System.currentTimeMillis() - createTime.getTime()
            if(elapsedTime < ((expiresTime - 20) * 1000)){
                return false
            }
        }
        return true
    }

    public void bindResult(MaterialResult result) {
        this.errcode = result.code
        if(result.message && result.message.length() <= 200){
            this.errmsg = result.message
        }
        this.costTime = result.costTime
        if(result.isOk()){
            if(result.mediaId) {
                this.mediaId = result.mediaId
            }
            if(result.url) {
                this.mediaUrl = result.url
            }
            if(result.createTime) {
                this.createTime = new Date(result.createTime * 1000)
            }
        }
    }
}
