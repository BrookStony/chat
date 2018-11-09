package com.trunksoft.chat.common

import com.trunksoft.chat.Account

class Filter {

    String name
    String description

    static belongsTo = [account: Account]

    static hasMany = [fields: FilterField, tags: Tag]

    static constraints = {
        name(blank: false, unique: 'account')
        fields(nullable: true)
        tags(nullable: true)
        description(nullable: true)
    }

    static mapping = {
        tags joinTable: [name: "chat_filter_tag", key: 'filter_id', column: 'tag_id'], batchSize: 10
    }
}
