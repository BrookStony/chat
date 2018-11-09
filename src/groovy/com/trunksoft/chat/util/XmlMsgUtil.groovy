package com.trunksoft.chat.util

class XmlMsgUtil {
    public static boolean attrBooleanValue(xmlNode, String name, boolean defaultValue) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            if("false".equals(value)){
                return false
            }
            else if("true".equals(value)){
                return true
            }
        }
        return defaultValue
    }

    public static double attrDoubleValue(xmlNode, String name, double defaultValue) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            return Double.valueOf(value).doubleValue()
        }
        return defaultValue
    }

    public static Double attrDoubleValue(xmlNode, String name) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            return Double.valueOf(value)
        }
        return null
    }

    public static int attrIntValue(xmlNode, String name, int defaultValue) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            return Integer.valueOf(value).intValue()
        }
        return defaultValue
    }

    public static Integer attrIntegerValue(xmlNode, String name) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            return Integer.valueOf(value)
        }
        return null
    }

    public static long attrLongValue(xmlNode, String name, long defaultValue) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            return Long.valueOf(value).longValue()
        }
        return defaultValue
    }

    public static Long attrLongValue(xmlNode, String name) {
        def value = attrTrimValue(xmlNode, name)
        if(value) {
            return Long.valueOf(value)
        }
        return null
    }

    public static String attrTrimValue(xmlNode, String name) {
        def value = xmlNode."${name}"
        if(value){
            return value.text().trim()
        }
        return null
    }

    public static String attrValue(xmlNode, String name) {
        def value = xmlNode."${name}"
        if(value){
            return value.text()
        }
        return null
    }

}
