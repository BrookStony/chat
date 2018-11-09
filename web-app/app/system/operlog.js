(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('OperlogListController', ['$scope', 'Operlog',
        function ($scope, Operlog) {
            $scope.sort = {
                sort: 'id',
                order: 'asc'
            };
            var sort = store.get(OPERLOG_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(OPERLOG_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                Operlog.query({sort: $scope.sort.sort,
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
                    Operlog.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('OperlogEditController', ['$scope', '$state', '$stateParams', 'Operlog',
        function ($scope, $state, $stateParams, Operlog) {
            Operlog.get({id: $stateParams.id}, function (data) {
                $scope.operlog = data;
            });
            $scope.submit = function () {
                var operlog = angular.copy($scope.operlog);
                Operlog.update(operlog, function (res) {
                    $state.transitionTo('system.operlog.list');
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
    module.controller('OperlogCreateController', ['$scope', '$state', '$stateParams', 'Operlog',
        function ($scope, $state, $stateParams, Operlog) {
            $scope.submit = function () {
                var operlog = angular.copy($scope.operlog);
                Operlog.save(operlog, function (res) {
                    $state.transitionTo('system.operlog.list');
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