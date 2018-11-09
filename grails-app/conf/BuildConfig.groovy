grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        runtime 'mysql:mysql-connector-java:5.1.25'
        compile 'com.xlson.groovycsv:groovycsv:1.0'
        compile "org.ocpsoft.prettytime:prettytime:2.1.1.Final"
        compile 'joda-time:joda-time:2.2'

        //quartz
        compile('org.quartz-scheduler:quartz:2.1.7') {
            excludes 'slf4j-api'
        }

        //spring batch
        compile('org.springframework.batch:spring-batch-core:2.2.1.RELEASE') {
            excludes 'spring-core', 'spring-context', 'spring-aop', 'spring-beans', 'spring-expression',
                    'spring-tx', 'spring-test', 'spring-webmvc', 'spring-web', 'spring-jdbc'
        }

        //Reactor
        reactorVersion = '1.0.0.RELEASE'
        compile "org.projectreactor:reactor-core:$reactorVersion"
        compile("org.projectreactor:reactor-groovy:$reactorVersion") {
            transitive = false
        }
        compile("org.projectreactor:reactor-spring:$reactorVersion") {
            transitive = false
        }

        //retrofit
        compile("com.squareup.retrofit:retrofit:1.3.0")
        //jcaptcha
//        compile 'com.octo.captcha:jcaptcha-extension-sound-freetts:1.0'

        //jooq
        runtime "org.jooq:jooq:3.2.3"
        runtime "org.jooq:jooq-meta:3.2.3"
        build "org.jooq:jooq-codegen:3.2.3"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.52.1"

        // plugins for the compile step
        compile ":scaffolding:2.0.2"
        compile ':cache:1.1.1'

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.9" // or ":hibernate4:4.3.4"
        runtime ":database-migration:1.3.8"
        runtime ":jquery:1.11.0.2"
        runtime ":resources:1.2.7"
        runtime ":zipped-resources:1.0.1"
        runtime ":cached-resources:1.1"
        runtime ":yui-minify-resources:0.1.5"
        compile ":cache-headers:1.1.6"

        compile ":spring-events:1.2"
        compile ":spring-security-core:2.0-RC4"
        compile ":wslite:0.7.2.0"
//        compile ":jcaptcha:1.5.0"

        compile ":grails-melody:1.49.2"
        test ":build-test-data:2.1.2"
        test ":fixtures:1.2"
        test ":codenarc:0.18"
    }
}
