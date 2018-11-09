package com.trunksoft.chat.log

class ArticleLog implements Serializable {

    Long timestamp
    Long accountId
    Long articleId
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
        id composite: ['articleId', 'timestamp']
        accountId index: 'ArticleLog_AccountId_Idx'
        articleId index: 'ArticleLog_ArticleId_Idx'
        timestamp index: 'ArticleLog_Timestamp_Idx'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ArticleLog)) return false

        ArticleLog that = (ArticleLog) o

        if (articleId != that.articleId) return false
        if (timestamp != that.timestamp) return false

        return true
    }

    int hashCode() {
        int result
        result = articleId.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
