package com.trunksoft.chat.member

import com.trunksoft.chat.util.StringFormatUtil
import com.trunksoft.platform.DateBase
import com.trunksoft.platform.common.Location
import com.trunksoft.chat.Account
import com.trunksoft.chat.type.SexType

class Member extends DateBase {

    static belongsTo = [account: Account]

    static transients = ['sexOrdinal']

    Integer no
    String openId
    String name
    String nickName
    String headimgurl
    SexType sex = SexType.MALE
    Integer age = 0
    String phone
    String weixin
    String qq
    String email

    Date birthday
    Boolean subscribe = false
    Date subscribeTime
    String unionid

    MemberGrade grade
    MemberGroup group

    String language = "zh_CN"
    Location location
    String postcode
    String address
    String city
    String province
    String country

    String description

    Integer sexOrdinal

    static constraints = {
        no(blank: false, unique: 'account')
        name(nullable: true)
        nickName(nullable: true)
        headimgurl(nullable: true)
        age(range: 0..150)
        phone(nullable: true)
        weixin(nullable: true)
        qq(nullable: true)
        email(nullable: true, email: true)
        postcode(nullable: true)
        address(nullable: true)
        birthday(nullable: true)
        subscribeTime(nullable: true)
        unionid(nullable: true)
        location(nullable: true)
        group(nullable: true)
        city(nullable: true)
        province(nullable: true)
        country(nullable: true)
        description(nullable: true)
    }

    static mapping = {
        cache(true)
        sex enumType: 'ordinal'
        openId index: 'Member_OpenId_Idx'
    }

    String noLabel() {
        return StringFormatUtil.formatMemberNo(no)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("Member{id: " + id)
        sb.append(",openId: " + openId)
        sb.append(",name: " + name)
        sb.append(",nickName: " + nickName)
        sb.append(",sex: " + sex)
        sb.append(",language: " + language)
        sb.append(",city: " + city)
        sb.append(",province: " + province)
        sb.append(",country: " + country)
        sb.append(",subscribe: " + subscribe)
        sb.append(",subscribeTime: " + subscribeTime)
        sb.append("}")
    }
}
