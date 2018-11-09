package com.seecent.chat.services.job

import com.seecent.chat.services.SynchDomainService
import com.seecent.chat.status.AccountStatus
import groovyx.gpars.GParsPool
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import com.seecent.chat.Account

class DaySynchJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(DaySynchJob)

    SynchDomainService synchDomainService

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Day stat start context:${context}")
        GParsPool.withPool {
            Account.findAllByStatus(AccountStatus.ACTIVATE).eachParallel { Account account ->
                try {
                    synchDomainService.doSynch(account)
                } catch (e) {
                    log.error(e.message, e)
                }
            }
        }
    }
}
