dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
    naming_strategy = 'com.trunksoft.chat.db.TableNamingStrategy'
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://127.0.0.1:3306/chatdb?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
            username = "root"
            password = "admin"

//            driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"
//            url="jdbc:sqlserver://192.168.0.111:1433;DatabaseName=chatdb"
//            dialect = "org.hibernate.dialect.SQLServerDialect"
//            username = "sa"
//            password = "kindsoft"

//            properties {
//                maxActive = 500
//                minEvictableIdleTimeMillis = 1800000
//                timeBetweenEvictionRunsMillis = 1800000
//                numTestsPerEvictionRun = 3
//                maxWait = 120000
//                testOnBorrow = true
//                testWhileIdle = true
//                testOnReturn = true
//                validationQuery = "SELECT 1"
//            }
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://192.168.226.37:3306/chatdb?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
            username = "chat"
            password = "kindsoft"
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
