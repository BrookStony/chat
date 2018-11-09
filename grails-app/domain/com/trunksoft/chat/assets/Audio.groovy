package com.trunksoft.chat.assets

import com.trunksoft.chat.type.AudioType
import com.trunksoft.chat.type.MaterialType

class Audio extends Material {

    private static final long MEDIASIZE = 5120l * 1024l * 8l

    MaterialType materialType = MaterialType.AUDIO
    AudioType type = AudioType.MP3
    Integer time

    static constraints = {
        time(range: 0..60)
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
