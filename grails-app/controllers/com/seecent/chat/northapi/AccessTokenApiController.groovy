package com.seecent.chat.northapi

import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class AccessTokenApiController {

    def index() {
        def appId = params.appId
        def secret = params.secret
        try {

        }
        catch (Exception e) {
            log.error(" refreshToken error!", e)
        }
        render ""
    }
}
