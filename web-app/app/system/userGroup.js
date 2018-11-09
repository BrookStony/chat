(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('UserGroupListController', ['$scope', 'UserGroup',
        function ($scope, UserGroup) {
            $scope.sort = {
                sort: 'name',
                order: 'asc'
            };
            var sort = store.get(USERGROUP_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(USERGROUP_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                UserGroup.query({sort: $scope.sort.sort,
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
                    UserGroup.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('UserGroupEditController', ['$scope', '$state', '$stateParams', 'UserGroup', 'Account',
        function ($scope, $state, $stateParams, UserGroup, Account) {
            UserGroup.get({id: $stateParams.id}, function (data) {
                $scope.userGroup = data;
                var accountIds = _.map($scope.userGroup.accounts, function (v) {
                    return v.id;
                });
                Account.query({}, function (datas, headers) {
                    $scope.accounts = datas;
                    _.each($scope.accounts, function (v) {
                        if (_.contains(accountIds, v.id)) {
                            v.check = true;
                        }
                    });
                });
            });
            $scope.submit = function () {
                var userGroup = angular.copy($scope.userGroup);
                userGroup.accounts = _.filter($scope.accounts, function (v) {
                    return v.check == true;
                });
                if (userGroup.name == null)delete userGroup.name;
                UserGroup.update(userGroup, function (res) {
                    $state.transitionTo('userGroup.list');
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
    module.controller('UserGroupCreateController', ['$scope', '$http', '$state', '$stateParams', 'UserGroup', 'Account',
        function ($scope, $http, $state, $stateParams, UserGroup, Account) {
            $scope.userGroup = {name: "", accounts: []};
            Account.query({}, function (datas, headers) {
                $scope.accounts = datas;
                _.each($scope.accounts, function (v) {
                    v.check = false;
                });
            });
            $scope.submit = function () {
                var userGroup = angular.copy($scope.userGroup);
                userGroup.accounts = _.filter($scope.accounts, function (v) {
                    return v.check == true;
                });
                UserGroup.save(userGroup, function (res) {
                    $state.transitionTo('userGroup.list');
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