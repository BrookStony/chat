package com.trunksoft.chat.assets

import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.type.VideoType

class Video extends Material {

    private static final long MEDIASIZE = 20480l * 1024l * 8l

    MaterialType materialType = MaterialType.VIDEO
    VideoType type = VideoType.MP4
    String title
    static constraints = {
        mediaSize(validator: { val, obj ->
            if(val > MEDIASIZE){
                return false
            }
            return true
        })
    }

    static mapping = {
        type enumType: 'ordinal'
    }
}
