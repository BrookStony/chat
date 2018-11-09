package com.trunksoft.chat.assets

import com.trunksoft.chat.status.ArticleStatus
import com.trunksoft.platform.DateBase
import com.trunksoft.chat.Account

/**
 * 图文
 */
class Article extends DateBase {

    static belongsTo = [account: Account]
    static hasMany = [articleItems: ArticleItem]

    static transients = ["content", "coverImage"]

    String uuid
    String path
    String title
    String author
    String originalUrl
    String description

    Long coverImageId
    String coverImagePath
    String coverImageUrl

    Boolean coverDisplayInText = true
    String mediaId
    Long expiresTime = 3*24*3600
    Long msgTime

    ArticleStatus status

    String content
    Picture coverImage

    static constraints = {
        uuid(nullable: true)
        path(nullable: true)
        title(maxSize: 50, nullable: false)
        author(nullable: true)
        originalUrl(nullable: true)
        description(nullable: true)
        mediaId(nullable: true)
        msgTime(nullable: true)
        coverImageId(nullable: true)
        coverImageUrl(nullable: true)
        coverImagePath(nullable: true)
        articleItems(nullable: true)
    }

    static mapping = {
        articleItems order: "no"
        status enumType: 'ordinal'
    }

    boolean isTimeOut(){
        if(expiresTime && msgTime){
            long elapsedTime =  System.currentTimeMillis() - msgTime
            if(elapsedTime < ((expiresTime - 20) * 1000)){
                return false
            }
        }
        return true
    }

    Picture getCoverImage() {
        return Picture.get(coverImageId)
    }
}
