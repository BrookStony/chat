import grails.plugin.springsecurity.SpringSecurityUtils
//import retrofit.RestAdapter

import java.util.concurrent.Executors

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

def configName = "chat-config.groovy"
def path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile().replace("/WEB-INF/classes/" + getClass().getSimpleName() + ".class", "").substring(1);
path = path.substring(path.lastIndexOf("/") + 1)
if(path.equals("chat")){
    configName = "chat-config.groovy"
}
else if(!path.equals("")) {
    configName = "${path}-chat-config.groovy".toString()
}

grails.config.locations = ["file:${userHome}/.grails/" + configName]
println "path: ${path}"
println "configName: ${configName}"

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['none']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.gsp.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = ['com.trunksoft.chat.services', 'com.trunksoft.chat.message', 'com.trunksoft.chat.log',
                               'com.trunksoft.chat.assets', 'com.trunksoft.chat.wechat', 'com.trunksoft.chat.northapi']
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

//Enable optimistic locking check
grails.converters.json.domain.include.version = true

//database migration
//grails.plugin.databasemigration.updateOnStartFileNames = ['001_setup.xml']

environments {
    development {
        quartz.autoStartup = false
        activiti.databaseType = 'h2'
        grails.logging.jul.usebridge = true
        grails.plugin.databasemigration.updateOnStart = false
    }
    production {
        quartz.autoStartup = true
        activiti.databaseType = 'mysql'
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
        grails.plugin.databasemigration.updateOnStart = true
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    root {
        info()
    }

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

griffon.wslite.injectInto = ['service']

beans {
    asyncApplicationEventMulticaster {
        taskExecutor = Executors.newCachedThreadPool()
        retryScheduler = Executors.newScheduledThreadPool(5)
    }
}

chat {
    home_url = "http://localhost:8080/chat"
    oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize"
    oauth_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token"
    bindAccountToken = "Rs12Isabscd873Idkk"
    assets {
        articleServer = "http://localhost:8080/chat/articleView"
//        server = "http://112.65.239.75:9000/chat-assets/"
        server = "http://localhost:9000/chat-assets/"
        basePath="D:\\soft\\tomcat-sem\\webapps\\chat-assets"
        fileSeparator = "\\"
    }
    curl {
        uploadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload"
        downloadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get"
        materialUrl = "https://api.weixin.qq.com/cgi-bin/material"
        waitfor = ">"
        charset = "GBK"
    }
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.active = true
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.trunksoft.platform.auth.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.trunksoft.platform.auth.UserRole'
grails.plugin.springsecurity.authority.className = 'com.trunksoft.platform.auth.Role'
//grails.plugin.springsecurity.securityConfigType = "Requestmap"
//grails.plugin.springsecurity.requestMap.className = 'com.trunksoft.platform.Requestmap'
//grails.plugin.springsecurity.requestMap.urlField = 'url'
//grails.plugin.springsecurity.requestMap.configAttributeField = 'configAttribute'
//grails.plugin.springsecurity.requestMap.httpMethodField = 'httpMethod'
//grails.plugin.springsecurity.rejectIfNoRule = true
//grails.plugin.springsecurity.fii.rejectPublicInvocations = false
//grails.plugin.springsecurity.controllerAnnotations.staticRules = [
//        '/index':          ['permitAll'],
//        '/index.gsp':      ['permitAll'],
//        '/assets/**':      ['permitAll'],
//        '/**/app/**':      ['permitAll'],
//        '/**/js/**':       ['permitAll'],
//        '/**/css/**':      ['permitAll'],
//        '/**/images/**':   ['permitAll'],
//        '/**/favicon.ico': ['permitAll'],
//        '/'              : ['IS_AUTHENTICATED_FULLY'],
//        '/**'            : ['IS_AUTHENTICATED_FULLY']
//]
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.interceptUrlMap = [
        '/index.gsp'            : ['permitAll'],
        '/**/app/**'            : ['permitAll'],
        '/**/lib/**'            : ['permitAll'],
        '/**/js/**'             : ['permitAll'],
        '/**/css/**'            : ['permitAll'],
        '/**/images/**'         : ['permitAll'],
        '/**/favicon.ico'       : ['permitAll'],
        '/login/**'             : ['permitAll'],
        '/logout/**'            : ['permitAll'],
        '/rest/**'              : ['permitAll'],
        '/receiveMessage/**'    : ['permitAll'],
        '/chatMessageApi/**'    : ['permitAll'],
        '/chatCustomMessageApi/**'    : ['permitAll'],
        '/chatMassMessageApi/**'    : ['permitAll'],
        '/memberApi/**'         : ['permitAll'],
        '/articleView/**'       : ['permitAll'],
        '/oauthArticleView/**'  : ['permitAll'],
        '/bindAccount/**'       : ['permitAll'],
        '/auth/**'              : ['permitAll'],
        '/user/**'              : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/users/**'             : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/role/**'              : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/roles/**'             : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/admin/**'             : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/settings/**'          : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/setting/**'           : ['ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN'],
        '/'                     : ['IS_AUTHENTICATED_FULLY'],
        '/**'                   : ['IS_AUTHENTICATED_FULLY']
]
grails.plugin.springsecurity.logout.postOnly = false

