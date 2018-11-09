package com.trunksoft.chat.assets

import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.type.PictureType

class Picture extends Material {

    private static final long MEDIASIZE = 2048l * 1024l * 8l

    static belongsTo = [pictureGroup: PictureGroup]

    MaterialType materialType = MaterialType.PICTURE
    PictureType type
    String picurl
    Integer width
    Integer height

    static constraints = {
        mediaSize(validator: { val, obj ->
            if(val > MEDIASIZE){
                return false
            }
            return true
        })
        picurl(nullable: true)
        width(nullable: true)
        height(nullable: true)
        pictureGroup(nullable: true)
    }

    static mapping = {
        type enumType: 'ordinal'
    }
}
