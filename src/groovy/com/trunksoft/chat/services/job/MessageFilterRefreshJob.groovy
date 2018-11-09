package com.trunksoft.chat.services.job

import com.trunksoft.chat.northapi.MessageFilterService
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean

class MessageFilterRefreshJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(MessageFilterRefreshJob)

    MessageFilterService messageFilterService

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Message filter fefresh start context:${context}")
        messageFilterService.refresh()
    }
}
