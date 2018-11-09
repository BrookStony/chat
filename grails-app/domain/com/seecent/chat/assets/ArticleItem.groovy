package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.chat.status.ArticleItemStatus
import com.seecent.platform.DateBase

class ArticleItem extends DateBase {

    static belongsTo = [account: Account, article: Article]

    static transients = ["content", "coverImage"]

    Integer no
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

    ArticleItemStatus status

    String content
    Picture coverImage

    static constraints = {
        no(default: 0)
        uuid(nullable: true)
        path(nullable: true)
        title(maxSize: 50, nullable: false)
        author(nullable: true)
        originalUrl(nullable: true)
        description(nullable: true)
        coverImageId(nullable: true)
        coverImagePath(nullable: true)
        coverImageUrl(nullable: true)
    }

    static mapping = {
        status enumType: 'ordinal'
    }

    Picture getCoverImage() {
        return Picture.get(coverImageId)
    }
}
