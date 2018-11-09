package com.trunksoft.chat.statistics

class ArticleItemDayStat extends BaseDayStat {

    Long accountId
    Long articleId
    Long articleItemId
    Long msgId
    Integer no
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
        id composite: ['articleItemId', 'timestamp']
        accountId index: 'ArticleItemDayStat_AccountId_Idx'
        articleItemId index: 'ArticleItemDayStat_ArticleItemId_Idx'
        timestamp index: 'ArticleItemDayStat_Timestamp_Idx'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ArticleItemDayStat)) return false

        ArticleItemDayStat that = (ArticleItemDayStat) o

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