package com.seecent.chat.member

import com.seecent.chat.Account
import com.seecent.chat.util.XmlUtil

class MemberGradeInitService {

    static transactional = false

    public void initFromXml(Account account, xmlName) {
        try {
            log.info(" initFromXml xmlName: " + xmlName)
            if(!MemberGrade.countByAccount(account)){
                def configNode = new XmlSlurper().parse(MemberGradeInitService.class.getResourceAsStream("/data/${xmlName}"))
                initMemberGrades(account, configNode)
            }
        } catch (Exception e) {
            log.error(" init error with xml:" + xmlName, e)
            e.printStackTrace()
        }
    }

    private void initMemberGrades(Account account, configNode) {
        try {
            int count = 0
            configNode.membergrade?.each{xmlNode ->
                count++
                Integer grade = XmlUtil.attrIntegerValue(xmlNode, "grade")
                String name = XmlUtil.attrValue(xmlNode, "name")
                if(grade && name){
                    MemberGrade memberGrade = new MemberGrade(grade: grade, name: name)
                    memberGrade.score = XmlUtil.attrIntegerValue(xmlNode, "score")
                    memberGrade.image = XmlUtil.attrValue(xmlNode, "image")
                    memberGrade.imageAmount = XmlUtil.intValue(xmlNode, "imageAmount", 1)
                    memberGrade.description = xmlNode.text()?.trim()
                    memberGrade.account = account
                    memberGrade.save(flush: true, failOnError: true)
                }
            }
            log.info(" initMemberGrades count: " + count)
        } catch (Exception e) {
            log.error(" initMemberGrades error", e)
            e.printStackTrace()
        }
    }
}
