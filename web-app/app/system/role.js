(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('RoleListController', ['$scope', 'Role',
        function ($scope, Role) {
            $scope.sort = {
                sort: 'name',
                order: 'asc'
            };
            var sort = store.get(ROLE_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(ROLE_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                Role.query({sort: $scope.sort.sort,
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
                    Role.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('RoleEditController', ['$scope', '$state', '$stateParams', 'Role', 'Menu',
        function ($scope, $state, $stateParams, Role, Menu) {
            Role.get({id: $stateParams.id}, function (data) {
                $scope.role = data;
                var menuIds = _.map($scope.role.menus, function (v) {
                    return v.id;
                });
                Menu.query({}, function (datas, headers) {
                    $scope.menus = datas;
                    _.each($scope.menus, function (v) {
                        if (_.contains(menuIds, v.id)) {
                            v.check = true;
                        }
                    });
                });
            });
            $scope.submit = function () {
                var role = angular.copy($scope.role);
                role.menus = _.filter($scope.menus, function (v) {
                    return v.check == true;
                });
                if (role.name == null)delete role.name;
                Role.update(role, function (res) {
                    $state.transitionTo('role.list');
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
    module.controller('RoleCreateController', ['$scope', '$state', '$stateParams', 'Role', 'Menu',
        function ($scope, $state, $stateParams, Role, Menu) {
            Menu.query({}, function (datas) {
                $scope.menus = datas;
            });
            $scope.submit = function () {
                var role = angular.copy($scope.role);
                role.menus = _.filter($scope.menus, function (v) {
                    return v.check == true;
                });
                Role.save(role, function (res) {
                    $state.transitionTo('role.list');
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
    module.controller('RoleMenuEditController', ['$scope', '$http', '$state', '$stateParams', 'Role', 'Menu',
        function ($scope, $http, $state, $stateParams, Role, Menu) {
            if(!$stateParams.id){
                $state.transitionTo('role.list');
            }
            $scope.role = {id: $stateParams.id};
            var zTreeOnCheck = function (event, treeId, treeNode) {
                $scope.$apply(function () {
                    if(treeNode.isParent && treeNode.checked) {
                        var treeObj = $.fn.zTree.getZTreeObj("menuTree");
                        treeObj.expandNode (treeNode, true);
                    }
                });
            };
            $scope.zTreeSetting = {
//                async: {
//                    enable: true,
//                    contentType: "application/json",
//                    type: "get",
//                    dataType: "json",
//                    url: "role/asyncMenuTree?roleId=" + $stateParams.id,
//                    autoParam: ["id", "name=n", "level=1v"]
//                },
                callback: {
                    onCheck: zTreeOnCheck
                },
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                }
            };
            $scope.refresh = function () {
                $http.get("role/menuTree", {params: {roleId: $stateParams.id}}).success(function (data) {
                    $scope.zNodes = data;
                    $.fn.zTree.init($("#menuTree"), $scope.zTreeSetting, $scope.zNodes);
                });

//                var treeObj = $.fn.zTree.getZTreeObj("menuTree");
//                treeObj.refresh();
            };
            $scope.refresh();
            $scope.submit = function () {
                var treeObj = $.fn.zTree.getZTreeObj("menuTree");
                var menuIds = [];
                var nodes = treeObj.getCheckedNodes(true);
                for(var i=0;i<nodes.length;i++){
                    menuIds.push(nodes[i].id);
                }
                console.log(nodes);
                $http.post("role/saveMenus", {id: $stateParams.id, menuIds: menuIds}
                ).success(function () {
                    $scope.refresh();
                    alert("用户角色菜单保存成功！");
                }).error(function() {
                    alert("用户角色菜单保存出错！");
                });
            }
        }
    ]);
})(angular);