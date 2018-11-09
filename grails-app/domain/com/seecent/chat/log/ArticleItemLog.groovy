package com.seecent.chat.log

class ArticleItemLog implements Serializable {

    Long timestamp
    Long accountId
    Long articleId
    Long articleItemId
    Integer type = 0
    String clientip
    String openId
    String unionId

    static constraints = {
        clientip nullable: true
        openId nullable: true
        unionId nullable: true
    }

    static mapping = {
        version false
        id composite: ['articleItemId', 'timestamp']
        accountId index: 'ArticleItemLog_AccountId_Idx'
        articleItemId index: 'ArticleItemLog_ArticleItemId_Idx'
        timestamp index: 'ArticleItemLog_Timestamp_Idx'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ArticleItemLog)) return false

        ArticleItemLog that = (ArticleItemLog) o

        if (articleItemId != that.articleItemId) return false
        if (timestamp != that.timestamp) return false

        return true
    }

    int hashCode() {
        int result
        result = articleItemId.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
