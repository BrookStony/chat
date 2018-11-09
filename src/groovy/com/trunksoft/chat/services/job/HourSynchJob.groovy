package com.trunksoft.chat.services.job

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import com.trunksoft.chat.Account

class HourSynchJob extends QuartzJobBean {
    Logger log = LoggerFactory.getLogger(HourSynchJob)

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//        log.info("SynchPerformanceService Job start context:${context}")
//        Account.list().each { Account account ->
//
//        }
    }
}
