package com.trunksoft.chat.statistics

class ArticleDayStat extends BaseDayStat {

    Long accountId
    Long articleId
    Long msgId
    Integer int_page_read_user
    Integer int_page_read_count
    Integer ori_page_read_user
    Integer ori_page_read_count
    Integer shareScene  //分享的场景 1代表好友转发 2代表朋友圈 3代表腾讯微博 255代表其他
    Integer shareUser
    Integer shareCount
    Integer addToFavUser
    Integer addToFavCount
    Integer targetUser

    static constraints = {
    }

    static mapping = {
        version false
        id composite: ['articleId', 'timestamp']
        accountId index: 'ArticleDayStat_AccountId_Idx'
        articleId index: 'ArticleDayStat_ArticleId_Idx'
        timestamp index: 'ArticleDayStat_Timestamp_Idx'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ArticleDayStat)) return false

        ArticleDayStat that = (ArticleDayStat) o

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