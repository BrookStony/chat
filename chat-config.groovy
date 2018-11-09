dataSource {
    dbCreate = "update"
    driverClassName = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://localhost:3306/chatdb?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
    username = "chat"
    password = "kindsoft"

    properties {
        maxActive = 500
        minEvictableIdleTimeMillis = 1800000
        timeBetweenEvictionRunsMillis = 1800000
        maxWait = 120000
        numTestsPerEvictionRun = 3
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = true
        validationQuery = "SELECT 1"
    }
}

chat {
    home_url = "http://www.seecent.com/chat"
    oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize"
    oauth_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token"
    assets {
        articleServer = "http://www.seecent.com/chat/articleView"
		server = "http://www.seecent.com/chat-assets/"
		basePath="/apps/nginx/html/chat-assets"
        fileSeparator = "/"
    }
    curl {
        uploadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload"
        downloadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get"
        materialUrl = "https://api.weixin.qq.com/cgi-bin/material"
        waitfor = "\$"
        charset = "utf-8"
    }
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.active = true