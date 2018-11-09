package com.seecent.chat.chart

import grails.web.JSONBuilder

class ColumnBarChartService {

    static boolean transactional = false

    def createBaiscBarChart() {

    }

    def createStackedBarChart(String chartTitle, List<String> categories, String yAxisTitle, seriesDatas, options) {
        def builder = new JSONBuilder()
        builder.build {
            chart = [type: 'bar']
            title = [text: chartTitle]
            xAxis = [categories: categories]
            yAxis = [title: [text: yAxisTitle], min: 0, stackLabels: [enabled: true, style:[fontWeight: 'bold', color: 'gray']]]
            plotOptions = [series: [stacking: 'normal']]
            legend = [reversed: true]
            series = seriesDatas
        }
    }
}
