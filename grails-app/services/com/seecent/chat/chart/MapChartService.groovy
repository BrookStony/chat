package com.seecent.chat.chart

import grails.web.JSONBuilder

class MapChartService {

    static boolean transactional = false

    def createBaiscMap() {

    }

    def createAreasMap(String chartTitle, String subTitle, seriesDatas, options) {
        def builder = new JSONBuilder()
        builder.build {
            chart = [type: 'Map']
            title = [text: chartTitle]
            subtitle = [text: subTitle]
            mapNavigation = [enabled: true, buttonOptions: [verticalAlign: 'bottom']]
            colorAxis = [min: 0]
            series = seriesDatas
        }
    }
}
