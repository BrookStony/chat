package com.seecent.chat.util

class XmlUtil {

    public static int intValue(xmlNode, String name, int defaultValue = 0) {
        def value = attrValue(xmlNode, name)
        if(null != value && !"".equals(value.trim())) {
            return Integer.valueOf(value).intValue()
        }
        return defaultValue
    }

    public static long longValue(xmlNode, String name, long defaultValue = 0l) {
        def value = attrValue(xmlNode, name)
        if(null != value && !"".equals(value.trim())) {
            return Long.valueOf(value).longValue()
        }
        return defaultValue
    }

    public static boolean booleanValue(xmlNode, String name, boolean defaultValue = true) {
        def value = attrValue(xmlNode, name)
        if(value) {
            if("false".equals(value)){
                return false
            }
        }
        return defaultValue
    }

    public static Integer attrIntegerValue(xmlNode, String name) {
        def value = attrValue(xmlNode, name)
        if(null != value && !"".equals(value.trim())) {
            return Integer.valueOf(value)
        }
        return null
    }

    public static Long attrLongValue(xmlNode, String name) {
        def value = attrValue(xmlNode, name)
        if(null != value && !"".equals(value.trim())) {
            return Long.valueOf(value)
        }
        return null
    }

    public static String attrValue(xmlNode, String name, defaultValue = null) {
        def value = xmlNode."@${name}"
        if (!value) {
            value = defaultValue
        } else {
            value = value.text()
        }
        return value
    }
}
