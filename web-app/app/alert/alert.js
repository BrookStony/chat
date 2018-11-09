(function (angular) {
    "use strict";
    var module = angular.module('chat.alert');
    module.controller('AlertController', ['$scope', '$state', '$http', '$stateParams', '$modal', 'Alert', 'Account',
        function ($scope, $state, $http, $stateParams, $modal, Alert, Account) {
            $scope.currentPage = 1;
            $scope.max = 15;
            $scope.includeCleared = false;
            $scope.severity = {
                critical: true,
                major: true,
                warning: true
            };
            $scope.selectedAlertTab = "all";
            $scope.refresh = function () {
                $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
                if(!$scope.accountId){
                    alert("未选择微信公众号");
                }
                var params = {offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max,
                    includeCleared: $scope.includeCleared,
                    critical: $scope.severity.critical,
                    major: $scope.severity.major,
                    warning: $scope.severity.warning};
                var alertId = $stateParams.id;
                if (alertId) {
                    params.alertId = alertId;
                }
                if ($scope.account.id) {
                    params.accountId = $scope.account.id;
                }
                Alert.query(params, function (datas, headers) {
                    $scope.datas = datas;
                    $scope.totalItems = parseInt(headers('totalItems'));
                });
            };
            $scope.$watch('account', $scope.refresh);
            $scope.$watch('currentPage', $scope.refresh);
            $scope.$watch('includeCleared', $scope.refresh);
            $scope.$watch('severity', $scope.refresh, true);
            $scope.setting = function () {
                $state.transitionTo("alert.setting");
            };
            $scope.markAsRead = function (id) {
                $http.post("alert/markAsRead", {ids: [id]})
                        .success(function (data) {
                            $scope.refresh();
                        });
            };
            $scope.markAllAsRead = function () {
                if (confirm("标记全部为已读?")) {
                    var ids = _.chain($scope.datas)
                            .filter(function (data) {
                                return data.isRead == false;
                            }).map(function (d) {
                                return d.id;
                            }).value();
                    $http.post("alert/markAsRead", {ids: ids})
                            .success(function (data) {
                                $scope.refresh();
                            });
                }
            };
            $scope.searchAlert = function (tab) {
                $scope.selectedAlertTab = tab;
                $scope.refresh();
            };
            $scope.history = function (id) {
                $modal.open({
                    windowClass: 'model-width-xlarge',
                    templateUrl: 'app/alert/history.html' + "?t=" + new Date().getTime(),
                    controller: "AlertHistoryController",
                    resolve: { id: function () {
                        return angular.copy(id);
                    }}});
            };
        }
    ]);
    module.controller('AlertSettingController', ['$scope', '$http', 'AlertType', function ($scope, $http, AlertType) {
        $scope.currentPage = 1;
        $scope.max = 15;
        $scope.severity = {
            critical: true,
            major: true,
            warning: true
        };
        $scope.checked = false;
        $scope.checkDisabled = true;
        $scope.toggleCheckedAll = function () {
            $scope.checked = !$scope.checked;
            angular.forEach($scope.datas, function (value) {
                value.checked = $scope.checked;
            });
        };
        $scope.$watch('datas', function (datas) {
            $scope.checked = datas && datas.length > 0 && _.all(datas, function (data) {
                return data.checked == true;
            });
            $scope.pauseDisabled = !_.any(datas, function (data) {
                return data.checked == true && data.enable == true
            });
            $scope.startDisabled = !_.any(datas, function (data) {
                return data.checked == true && data.enable == false
            });
        }, true);
        $scope.activate = function (enable) {
            var isActivated = enable ? "启用" : "暂停";
            if (confirm("确认要" + isActivated + "改预警类型?")) {
                var ids = _.chain($scope.datas)
                        .filter(function (data) {
                            return data.checked == true;
                        }).map(function (d) {
                            return d.id;
                        }).value();
                $http.post("alertType/activate", {ids: ids, enable: enable})
                        .success(function (data) {
                            $scope.refresh();
                        });
            }
        };
        $scope.refresh = function () {
            AlertType.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max,
                        critical: $scope.severity.critical,
                        major: $scope.severity.major,
                        warning: $scope.severity.warning},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
        };
        $scope.$watch('currentPage', $scope.refresh);
        $scope.$watch('severity', $scope.refresh, true);
    }]);
    module.controller('AlertHistoryController', ['$scope', '$modalInstance', 'id', 'AlertHistory',
        function ($scope, $modalInstance, id, AlertHistory) {
            $scope.severity = {
                includeCleared: false,
                critical: true,
                major: true,
                warning: true
            };
            $scope.page = {
                currentPage: 1,
                max: 10
            };
            $scope.refresh = function () {
                AlertHistory.query({alertId: id, offset: ($scope.page.currentPage - 1) * $scope.page.max, max: $scope.page.max,
                            includeCleared: $scope.severity.includeCleared,
                            critical: $scope.severity.critical,
                            major: $scope.severity.major,
                            warning: $scope.severity.warning},
                        function (datas, headers) {
                            $scope.datas = datas;
                            $scope.totalItems = parseInt(headers('totalItems'));
                        });
            };
            $scope.$watch('page.currentPage', function () {
                $scope.refresh();
            }, true);
            $scope.$watch('severity', $scope.refresh, true);
            $scope.close = function () {
                $modalInstance.close();
            };
        }]);
})(angular);
