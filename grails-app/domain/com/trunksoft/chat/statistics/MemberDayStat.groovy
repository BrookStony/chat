package com.trunksoft.chat.statistics

class MemberDayStat extends BaseDayStat {

    Long accountId
    Integer newAmount
    Integer cancelAmount
    Integer netAmount
    Integer cumulateAmount
    Integer newByWeixinName
    Integer newByWeixinId
    Integer newByArticleMenu
    Integer newByCard
    Integer newByOther

    static constraints = {
        newByWeixinName nullable: true
        newByWeixinId nullable: true
        newByArticleMenu nullable: true
        newByCard nullable: true
        newByOther nullable: true
    }

    static mapping = {
        version false
        id composite: ['accountId', 'timestamp']
        accountId index: 'MemberDayStat_AccountId_Idx'
        timestamp index: 'MemberDayStat_Timestamp_Idx'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof MemberDayStat)) return false

        MemberDayStat that = (MemberDayStat) o

        if (accountId != that.accountId) return false
        if (timestamp != that.timestamp) return false

        return true
    }

    int hashCode() {
        int result
        result = accountId.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
