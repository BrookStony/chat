(function (angular) {
    "use strict";
    var module = angular.module('platform.task');
    module.controller('TaskListController', ['$scope', 'Task',
        function ($scope, Task) {
            $scope.sort = {
                sort: 'id',
                order: 'asc'
            };
            var sort = store.get(TASK_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(TASK_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                Task.query({sort: $scope.sort.sort,
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
                    Task.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('TaskEditController', ['$scope', '$state', '$stateParams', 'Task',
        function ($scope, $state, $stateParams, Task) {
            Task.get({id: $stateParams.id}, function (data) {
                $scope.task = data;
            });
            $scope.submit = function () {
                var task = angular.copy($scope.task);
                Task.update(task, function (res) {
                    $state.transitionTo('system.task.list');
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
    module.controller('TaskCreateController', ['$scope', '$state', '$stateParams', 'Task',
        function ($scope, $state, $stateParams, Task) {
            $scope.submit = function () {
                var task = angular.copy($scope.task);
                Task.save(task, function (res) {
                    $state.transitionTo('system.task.list');
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