(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('MenuListController', ['$scope', 'Menu',
        function ($scope, Menu) {
            $scope.sort = {
                sort: 'level',
                order: 'asc'
            };
            var sort = store.get(MENU_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(MENU_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 15;
            $scope.refresh = function () {
                Menu.query({sort: $scope.sort.sort,
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
                    Menu.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('MenuEditController', ['$scope', '$http', '$state', '$stateParams', 'Menu', 'Role',
        function ($scope, $http, $state, $stateParams, Menu, Role) {
            $scope.menuLevels = [1, 2];
            Menu.get({id: $stateParams.id}, function (data) {
                $scope.menu = data;
                if($scope.menu.level > 1) {
                    $http.get("menu/searchMenus", {params: {level: 1}}).success(function (data) {
                        $scope.menus = data;
                        $scope.menu.parent = _.find($scope.menus, function (v) {
                            return v.id == $scope.menu.parent.id;
                        });
                    });
                }
            });
            $scope.submit = function () {
                var menu = angular.copy($scope.menu);
                if (menu.name == null)delete menu.name;
                if (menu.code == null)delete menu.code;
                Menu.update(menu, function (res) {
                    $state.transitionTo('menu.list');
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
    module.controller('MenuCreateController', ['$scope', '$http', '$state', '$stateParams', 'Menu',
        function ($scope, $http, $state, $stateParams, Menu) {
            $scope.menuLevels = [1, 2];
            $scope.menu = {level: 1, enable: true};
            $scope.selectMenuLevel = function() {
                if($scope.menu.level > 1) {
                    $http.get("menu/searchMenus", {params: {level: 1}}).success(function (data) {
                        $scope.menus = data;
                        if($scope.menus && $scope.menus.length > 0) {
                            $scope.menu.parent = $scope.menus[0];
                        }
                    });
                }
                else {
                    $scope.menus = null;
                }
            };
            $scope.submit = function () {
                var menu = angular.copy($scope.menu);
                Menu.save(menu, function (res) {
                    $state.transitionTo('menu.list');
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