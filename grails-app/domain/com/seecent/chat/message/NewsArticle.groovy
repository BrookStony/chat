package com.seecent.chat.message

class NewsArticle {

    static belongsTo = [newsMessage: NewsMessage]

    Long articleId
    Long articleItemId
    Integer no = 0
    String title
    String description
    String url
    String picurl
    String viewUrl
    String coverImage

    static constraints = {
        articleId(nullable: true)
        articleItemId(nullable: true)
        title(nullable: true)
        description(nullable: true)
        url(nullable: true)
        picurl(nullable: true)
        viewUrl(nullable: true)
        coverImage(nullable: true)
    }
}
