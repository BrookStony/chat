package com.seecent.chat.statistics

import grails.converters.JSON

import java.text.DateFormat
import java.text.SimpleDateFormat

class MemberStatController {

    private static final DateFormat DF = new SimpleDateFormat("MM.dd")

    def columnBarChartService
    def mapChartService

    def index() { }

    def sexChart() {
        def categories = [DF.format(new Date())]
        def series = [[color: "#44B549", name: "男", data: [212]],
                      [color: "#4A90E2", name: "女", data: [129]],
                      [color: "#EBCB6B", name: "未知", data: [3]]]
        def chartData = columnBarChartService.createStackedBarChart("", categories, "", series, null)
        render chartData
    }

    def languageChart() {
        def categories = [DF.format(new Date())]
        def series = [[color: "#44B549", name: "简体中文", data: [24]],
                      [color: "#4A90E2", name: "未知", data: [2]],
                      [color: "#EBCB6B", name: "英文", data: [1]]]
        def chartData = columnBarChartService.createStackedBarChart("", categories, "", series, null)
        render chartData
    }

    def provincesMapData() {

        def nameMap = ["吉林":"cn-jl" ,"天津":"cn-tj","安徽":"cn-ah","山东":"cn-sd","山西":"cn-sx",
                       "新疆":"cn-xj","河北":"cn-hb","河南":"cn-he" ,"湖南":"cn-hn","甘肃":"cn-gs",
                       "福建":"cn-fj","贵州":"cn-gz" ,"重庆":"cn-cq","江苏":"cn-js","湖北":"cn-hu",
                       "内蒙古":"cn-nm" ,"广西":"cn-gx","黑龙江":"cn-hl","云南":"cn-yn","辽宁":"cn-ln",
                       "香港":"cn-6668","浙江":"cn-zj","上海":"cn-sh","北京":"cn-bj","广东":"cn-gd",
                       "澳门":"cn-3681","西藏":"cn-xz","陕西":"cn-sa","四川":"cn-sc","海南":"cn-ha",
                       "宁夏":"cn-nx","青海":"cn-qh","江西":"cn-jx","台湾":"tw-tw"]

        //  {"hc-key": "cn-sh","value": 0},  
        Random random = new Random()
        def data = []
        nameMap.each {
            data << ['hc-key': it.value, value: random.nextInt(100).toString()]
        }

        render (data as JSON)
    }

}
