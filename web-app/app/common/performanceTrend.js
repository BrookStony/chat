(function (angular) {
    "use strict";
    var module = angular.module('chat.common');
    module.controller('PerformanceTrendController', ['$scope', '$http', '$modalInstance', 'item',
        function ($scope, $http, $modalInstance, item) {
            $scope.title = item.title;
            var callback = item.callback;
            $scope.close = function () {
                $modalInstance.close();
            };
            $http.get("performance/lastYearTrend", {params: {
                type: item.type, indicator: item.indicator, id: item.id
            }}).success(function (data) {
                if (callback) {
                    callback.call(this, data);
                }
                var chartData = {
                    type: 'spline',
                    rangeSelector: {
                        selected: 1
                    },
                    title: {
                        text: item.label
                    },
                    series: [
                        {
                            type: 'spline',
                            name: item.label,
                            data: data,
                            tooltip: {
                                valueDecimals: 2
                            }
                        }
                    ]
                };
                if (item.indicator == "ctr") {
                    chartData.yAxis = {max: 100, min: 0};
                    chartData.tooltip = {
                        pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> %<br/>',
                        valueDecimals: 2
                    };
                }
                $scope.chartData = chartData;
            });
        }
    ]);
})(angular);