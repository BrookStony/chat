package com.trunksoft.chat.alert

class AlertTypeInitService {

    static transactional = false

    public void initFromXml(xmlName) {
        try {
            log.info(" initFromXml xmlName: " + xmlName)
            def configNode = new XmlSlurper().parse(AlertTypeInitService.class.getResourceAsStream("/data/${xmlName}"))
            initAlertTypes(configNode)
        } catch (Exception e) {
            log.error(" init error with xml:" + xmlName, e)
            e.printStackTrace()
        }
    }

    private initAlertCronTriggerMap() {

        def alertCronTriggerMap = [:]

        AlertCronTrigger.list().each{ alertCronTrigger ->
            alertCronTriggerMap.put(alertCronTrigger.name, alertCronTrigger)
        }

        return alertCronTriggerMap
    }

    private void initAlertTypes(configNode) {

        def alertTypeMap = [:]

        try {

            def alertCronTriggerMap = initAlertCronTriggerMap()
            def alertTypeList = AlertType.list()

            alertTypeList.each {alertType ->
                alertTypeMap.put(alertType.name, alertType)
            }

            int count = 0
            configNode.alerttype?.each{alertTypeNode ->

                String name = attrValue(alertTypeNode, "name")
                String severity = attrValue(alertTypeNode, "severity", Severity.CRITICAL.name())
                String cronTrigger = attrValue(alertTypeNode, "cronTrigger")
                if(name && cronTrigger){
                    def trigger = alertCronTriggerMap.get(cronTrigger)
                    if(trigger){
                        def alertType = alertTypeMap.get(name) as AlertType
                        if(!alertType){
                            alertType = new AlertType()
                            alertType.name = name
                            alertType.count = 0
                            alertType.lastTime = new Date()
                            alertType.enable = attrBooleanValue(alertTypeNode, "enable", true)
                        }

                        alertType.label = attrValue(alertTypeNode, "label")
                        alertType.severity = Severity.valueOf(severity)
                        alertType.trigger = trigger
                        alertType.template = alertTypeNode.text()?.trim()

                        if (alertType.id == null || alertType.isDirty()) {
                            alertType.save(failOnError: true)
                            count++
                        }
                        alertTypeMap.put(name, alertType)
                    }
                }
            }

            log.info(" initAlertTypes count: " + count)
        } catch (Exception e) {
            log.error(" init alertTypes error", e)
            e.printStackTrace()
        }

    }

    private boolean attrBooleanValue(xmlNode, String name, boolean defaultValue = true) {
        def value = attrValue(xmlNode, name)
        if(value) {
            if("false".equals(value)){
                return false
            }
        }
        return defaultValue
    }

    private int attrIntValue(xmlNode, String name, int defaultValue = 0) {
        def value = attrValue(xmlNode, name)
        if(null != value && !"".equals(value.trim())) {
            return Integer.valueOf(value).intValue()
        }
        return defaultValue
    }

    private String attrValue(xmlNode, String name, defaultValue = null) {
        def value = xmlNode."@${name}"
        if (!value) {
            value = defaultValue
        } else {
            value = value.text()
        }
        return value
    }

}
