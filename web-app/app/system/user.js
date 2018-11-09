(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('UserListController', ['$scope', 'User',
        function ($scope, User) {
            $scope.sort = {
                sort: 'username',
                order: 'asc'
            };
            var sort = store.get(USER_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(USER_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                User.query({sort: $scope.sort.sort,
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
                    User.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('UserEditController', ['$scope', '$http', '$state', '$stateParams', 'User', 'Role',
        function ($scope, $http, $state, $stateParams, User, Role) {
            User.get({id: $stateParams.id}, function (data) {
                $scope.user = data;
                var roleIds = _.map($scope.user.roles, function (v) {
                    return v.id;
                });
                Role.query({}, function (datas, headers) {
                    $scope.roles = datas;
                    _.each($scope.roles, function (v) {
                        if (_.contains(roleIds, v.id)) {
                            v.check = true;
                        }
                    });
                });
                $http.get("userGroup/searchUserGroups").success(function (data) {
                    $scope.userGroups = data;
                    $scope.user.userGroup = _.find($scope.userGroups, function (v) {
                        return v.id == $scope.user.userGroup.id;
                    });
                });
            });
            $scope.submit = function () {
                var user = angular.copy($scope.user);
                user.roles = _.filter($scope.roles, function (v) {
                    return v.check == true;
                });
                if (user.name == null)delete user.name;
                if (user.email == null)delete user.email;
                User.update(user, function (res) {
                    $state.transitionTo('user.list');
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
    module.controller('UserCreateController', ['$scope', '$http', '$state', '$stateParams', 'User', 'Role',
        function ($scope, $http, $state, $stateParams, User, Role) {
            $scope.user = {};
            Role.query({}, function (datas) {
                $scope.roles = datas;
            });
            $http.get("userGroup/searchUserGroups").success(function (data) {
                $scope.userGroups = data;
                if($scope.userGroups && $scope.userGroups.length > 0) {
                    $scope.user.userGroup = $scope.userGroups[0];
                }
            });
            $scope.submit = function () {
                var user = angular.copy($scope.user);
                user.roles = _.filter($scope.roles, function (v) {
                    return v.check == true;
                });
                User.save(user, function (res) {
                    $state.transitionTo('user.list');
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