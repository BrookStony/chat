(function (angular) {
    "use strict";
    var module = angular.module('chat.setting');

    module.controller('AccountListController', ['$scope', '$http', 'Account',
        function ($scope, $http, Account) {
            $scope.sort = {
                sort: 'name',
                order: 'asc'
            };
            var sort = store.get(ACCOUNT_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(ACCOUNT_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 50;
            $scope.refresh = function () {
                Account.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        name: $scope.filter || '',
                        offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                        function (datas, headers) {
                            $scope.datas = datas;
                            $scope.totalItems = parseInt(headers('totalItems'));
                        });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', function () {
                $scope.refresh();
            }, true);
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    Account.remove({id: item}, function () {
                        $scope.refresh();
                    });
                }
            };
            $scope.pull = function (id) {
                if (confirm('可能需要比较长时间,你确定?')) {
                    $http.get('account/synchDomain/' + id)
                            .success(function (data, status, headers, config) {
                                alert("账户同步完成！");
                            })
                            .error(function (data, status, headers, config) {
                                alert("账户同步发生异常，请联系管理员！");
                            });
                }
            };
            $scope.refreshAccessToken = function (id) {
                $http.get('account/refreshAccessToken/' + id)
                    .success(function (data, status, headers, config) {
                        alert("刷新AccessToken完成！");
                    })
                    .error(function (data, status, headers, config) {
                        alert("刷新AccessToken发生异常，请联系管理员！");
                    });
            };
        }]);

    module.controller('AccountEditController', ['$scope', '$state', '$stateParams', 'Account',
        function ($scope, $state, $stateParams, Account) {
            Account.get({id: $stateParams.id}, function(data) {
                    $scope.account = data;
                    $scope.account.type = $scope.account.type.name;
            });
            $scope.submit = function () {
                Account.update($scope.account, function (res) {
                    $state.transitionTo('account.list');
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

    module.controller('AccountCreateController', ['$scope', '$state', '$stateParams', 'Account',
        function ($scope, $state, $stateParams, Account) {
            $scope.account = {'class': 'com.kindsoft.chat.Account'};
            $scope.submit = function () {
                Account.save($scope.account, function (res) {
                    $state.transitionTo('account.list');
                }, function (res) {
                    if (res.status == 422) {
                        $scope.errors = res.data.errors;
                    } else {
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            }
        }])
})(angular);

