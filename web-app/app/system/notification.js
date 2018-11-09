(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('NotificationListController', ['$scope', 'Notification',
        function ($scope, Notification) {
            $scope.sort = {
                sort: 'title',
                order: 'asc'
            };
            var sort = store.get(NOTIFICATION_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(NOTIFICATION_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                Notification.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        filter: $scope.filter || '',
                        offset: ($scope.currentPage - 1) * $scope.max,
                        max: $scope.max},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', $scope.refresh);
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    Notification.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('NotificationEditController', ['$scope', '$state', '$stateParams', 'Notification',
        function ($scope, $state, $stateParams, Notification) {
            Notification.get({id: $stateParams.id}, function (data) {
                $scope.notification = data;
            });
            $scope.submit = function () {
                var notification = angular.copy($scope.notification);
                notification.update(notification, function (res) {
                    $state.transitionTo('system.notification.list');
                }, function (res) {
                    if (res.status == 422) {
                        $scope.errors = res.data.errors;
                    } else {
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            }
        }
    ]);
    module.controller('NotificationCreateController', ['$scope', '$state', '$stateParams', 'Notification',
        function ($scope, $state, $stateParams, Notification) {
            $scope.submit = function () {
                var notification = angular.copy($scope.notification);
                Notification.save(notification, function (res) {
                    $state.transitionTo('system.notification.list');
                }, function (res) {
                    if (res.status == 422) {
                        $scope.errors = res.data.errors;
                    } else {
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            }
        }
    ]);
})(angular);