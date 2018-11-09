package com.trunksoft.chat.services.job

import groovyx.gpars.GParsPool
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import com.trunksoft.chat.Account

class StatusSynchJob extends QuartzJobBean {
    Logger logger = LoggerFactory.getLogger(StatusSynchJob)

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//        logger.info("StatusSynchJob start context:${context}")
//        GParsPool.withPool {
//            Account.list().eachParallel { Account account ->
//
//            }
//        }
    }
}
