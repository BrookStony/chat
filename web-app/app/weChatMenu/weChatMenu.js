(function (angular) {
    "use strict";
    var module = angular.module('chat.weChatMenu');
    module.controller('WeChatMenuController', ['$rootScope', '$scope', '$http', '$stateParams', 'Account', 'WeChatMenu',
        function ($rootScope, $scope, $http, $stateParams, Account, WeChatMenu) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.currentPage = 1;
            $scope.max = 50;
            $scope.menuCreating = false;
            $scope.menuEditing = false;
            $scope.menuSorting = false;
            $scope.editMenu = null;
            $scope.menuEditEnable = false;
            $scope.menuEditMessage = "你可以先添加一个菜单，然后开始为其设置响应动作";
            $scope.refresh = function () {
                $scope.errors = null;
                WeChatMenu.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max, accountId: $scope.accountId},
                    function (datas, headers) {
                        $scope.weChatMenus = datas;
                        _.each($scope.weChatMenus, function(menu) {
                            menu.editable = false;
                            _.each(menu.subMenus, function(submenu) {
                                submenu.editable = false;
                            });
                        });
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage', $scope.refresh);

            $scope.showMessage = function(message) {
                $scope.menuEditMessage = message;
                $scope.menuCreating = false;
                $scope.menuEditing = false;
                $scope.menuSorting = false;
                $scope.editMenu = null;
                $scope.menuEditEnable = false;
            };
            $scope.addMenu = function() {
                if(!$scope.menuEditing && !$scope.menuCreating){
                    if($scope.weChatMenus.length > 2){
                        $scope.showMessage("最多只能创建3个一级菜单！");
                        alert("最多只能创建3个一级菜单！");
                    }
                    else if(0 == $scope.weChatMenus.length) {
                        $scope.menuCreating = true;
                        var newMenu = {name: "新菜单", account: {id: $scope.accountId}, code: 1, no: 1, level:1, type: 0, subMenus: []};
                        $scope.editMenu = newMenu;
                    }
                    else {
                        $scope.menuCreating = true;
                        var chatMenu = $scope.weChatMenus[$scope.weChatMenus.length - 1];
                        var no = chatMenu.no + 1;
                        var newMenu = {name: "新菜单", account: {id: chatMenu.account.id}, code: no, no: no, level:1, type: 0, subMenus: []};
                        $scope.editMenu = newMenu;
                    }
                }
                else {
                    if($scope.menuCreating) {
                        alert("请先保存新增菜单！");
                    }
                    else if($scope.menuEditing) {
                        alert("正在编辑菜单，请先保存！");
                    }
                }
            };
            $scope.addSubMenu = function(menu) {
                if(!$scope.menuEditing && !$scope.menuCreating){
                    var subMenus = menu.subMenus;
                    if(subMenus.length > 4){
                        $scope.showMessage("每个一级菜单下可创建最多5个二级菜单！");
                        alert("每个一级菜单下可创建最多5个二级菜单！");
                    }
                    else {
                        if(0 == subMenus.length) {
                            var addSubMenuFlag = false;
                            if(menu.eventKey || menu.url) {
                                if (confirm('使用二级菜单后，当前编辑的消息将被清除，确定使用二级菜单?')) {
                                    addSubMenuFlag = true;
                                }
                            }
                            else {
                                addSubMenuFlag = true;
                            }
                            if(addSubMenuFlag) {
                                $scope.menuCreating = true;
                                var no = menu.no * 10 + 1;
                                var newSubMenu = {name: "新菜单", parent: {id: menu.id}, account: {id: menu.account.id}, code: no, no: no, level: 2, type: 0, editable: false};
                                $scope.editMenu = newSubMenu;
                            }
                        }
                        else {
                            $scope.menuCreating = true;
                            var subMenu = subMenus[subMenus.length - 1];
                            var no = subMenu.no + 1;
                            var newSubMenu = {name:"新菜单", parent: {id: menu.id}, account: {id: menu.account.id}, code: no, no: no, level: 2, type: 0, editable: false};
                            $scope.editMenu = newSubMenu;
                        }
                    }
                }
                else {
                    if($scope.menuCreating) {
                        alert("请先保存新增菜单！");
                    }
                    else if($scope.menuEditing) {
                        alert("正在编辑菜单，请先保存！");
                    }
                }
            };
            $scope.showMenu = function(menu) {
                if(!$scope.menuCreating && !$scope.menuEditing){
                    if(menu.subMenus.length > 0) {
                        $scope.showMessage("已有子菜单，无法设置动作！");
                    }
                    else {
                        $scope.menuEditEnable = true;
                        $scope.editMenu = angular.copy(menu);
                    }
                }
            };
            $scope.showSubMenu = function(subMenu) {
                if(!$scope.menuCreating && !$scope.menuEditing) {
                    $scope.menuEditEnable = true;
                    $scope.editMenu = angular.copy(subMenu);
                }
            };
            $scope.saveMenu = function() {
                if($scope.validationMenu($scope.editMenu)){
                    $http.post("weChatMenu/saveMenu", angular.copy($scope.editMenu)
                    ).success(function () {
                        $scope.refresh();
                        alert("自定义菜单保存成功！");
                    }).error(function() {
                        alert("自定义菜单保存出错！");
                    });
                }
                $scope.menuCreating = false;
            };
            $scope.cancelSaveMenu = function() {
                $scope.showMessage("你可以先添加一个菜单，然后开始为其设置响应动作");
            };

            $scope.removeMenu = function(item) {
                if (confirm('您确定要删除?')) {
                    WeChatMenu.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
                $scope.showMessage("你可以先添加一个菜单，然后开始为其设置响应动作");
            };

            $scope.editMenuName = function(menu) {
                if(!$scope.menuEditing) {
                    menu.oldName = menu.name;
                    menu.editable = true;
                    $scope.menuEditing = true;
                    $scope.menuCreating = false;
                    $scope.menuEditEnable = false;
                    if(1 == menu.level) {
                        $scope.menuEditMessage = "修改菜单名称, 一级菜单名称名字不多于4个汉字或8个字母！";
                    }
                    else {
                        $scope.menuEditMessage = "修改菜单名称, 二级菜单名称名字不多于8个汉字或16个字母！";
                    }
                }
            };
            $scope.saveMenuName = function(menu) {
                if($scope.validationMenu(menu)){
                    $http.post("weChatMenu/saveMenu", angular.copy(menu)
                    ).success(function () {
                        $scope.refresh();
                        alert("自定义菜单保存成功！");
                    }).error(function() {
                        alert("自定义菜单保存出错！");
                    });
                }
                $scope.menuEditing = false;
                menu.editable = false;
                $scope.showMessage("你可以先添加一个菜单，然后开始为其设置响应动作");
            };
            $scope.cancelSaveMenuName = function(menu) {
                if(menu.oldName && menu.oldName.length > 0){
                    menu.name = menu.oldName;
                }
                menu.editable = false;
                $scope.menuEditing = false;
            };
            $scope.validationMenu = function(menu) {
                if(!menu.name || menu.name.length < 1){
                    alert("菜单名称不能为空！");
                    return false;
                }
                else {
                    if(1 == menu.level) {
                        if(menu.name.replace(/[^\x00-\xff]/g, 'xx').length > 8){
                            alert("菜单名称名字不多于4个汉字或8个字母！");
                            return false;
                        }
                    }
                    else {
                        if (menu.name.replace(/[^\x00-\xff]/g, 'xx').length > 16) {
                            alert("菜单名称名字不多于8个汉字或16个字母！");
                            return false;
                        }
                    }
                    if(1 == menu.type) {
                        if(ChatUtil.notBlank(menu.url)) {
                            if(!ChatUtil.validationUrl(menu.url)){
                                alert("请输入正确的URL！");
                                return false;
                            }
                        }
                    }
                }
                return true;
            };

            $scope.enableMenuSort = function() {
                if(!$scope.menuEditing && !$scope.menuCreating){
                    $scope.showMessage("修改菜单顺序，点击向上、向下图标改变菜单顺序！");
                    $scope.menuSorting = true;
                }
                else {
                    if($scope.menuCreating) {
                        alert("请先保存新增菜单！");
                    }
                    else if($scope.menuEditing) {
                        alert("正在编辑菜单，请先保存！");
                    }
                }
            };
            $scope.menuSortUp = function(menu) {
                if(menu.no > 1){
                    var index = menu.no - 1;
                    var nowMenu = $scope.weChatMenus[index];
                    nowMenu.no -= 1;
                    var nowSubMenus = nowMenu.subMenus;
                    for(var i=0; i<nowSubMenus.length; i++) {
                        nowSubMenus[i].no = nowMenu.no * 10 + i + 1;
                    }
                    var chatMenu = $scope.weChatMenus[index - 1];
                    chatMenu.no += 1;
                    var chatSubMenus = chatMenu.subMenus;
                    for(var j=0; j<chatSubMenus.length; j++) {
                        chatSubMenus[j].no = chatMenu.no * 10 + j + 1;
                    }
                    $scope.weChatMenus[index - 1] = nowMenu;
                    $scope.weChatMenus[index] = chatMenu;
                }
            };
            $scope.menuSortDown = function(menu) {
                if(menu.no < $scope.weChatMenus.length){
                    var index = menu.no - 1;
                    var nowMenu = $scope.weChatMenus[index];
                    nowMenu.no += 1;
                    var nowSubMenus = nowMenu.subMenus;
                    for(var i=0; i<nowSubMenus.length; i++) {
                        nowSubMenus[i].no = nowMenu.no * 10 + i + 1;
                    }
                    var chatMenu = $scope.weChatMenus[index + 1];
                    chatMenu.no -= 1;
                    var chatSubMenus = chatMenu.subMenus;
                    for(var j=0; j<chatSubMenus.length; j++) {
                        chatSubMenus[j].no = chatMenu.no * 10 + j + 1;
                    }
                    $scope.weChatMenus[index + 1] = nowMenu;
                    $scope.weChatMenus[index] = chatMenu;
                }
            };
            $scope.subMenuSortUp = function(menu, subMenu) {
                var index = menu.no - 1;
                var subMenus = $scope.weChatMenus[index].subMenus;
                for(var j=0; j<subMenus.length; j++) {
                    var nowMenu = subMenus[j];
                    if(nowMenu.id == subMenu.id){
                        if(nowMenu.no % 10 > 1) {
                            nowMenu.no -= 1;
                            var chatMenu = subMenus[j - 1];
                            chatMenu.no += 1;
                            subMenus[j - 1] = nowMenu;
                            subMenus[j] = chatMenu;
                        }
                        return;
                    }
                }
            };
            $scope.subMenuSortDown = function(menu, subMenu) {
                var index = menu.no - 1;
                var subMenus = $scope.weChatMenus[index].subMenus;
                for(var j=0; j<subMenus.length; j++) {
                    var nowMenu = subMenus[j];
                    if(nowMenu.id == subMenu.id){
                        if(nowMenu.no % 10 < subMenus.length) {
                            nowMenu.no += 1;
                            var chatMenu = subMenus[j + 1];
                            chatMenu.no -= 1;
                            subMenus[j + 1] = nowMenu;
                            subMenus[j] = chatMenu;
                        }
                        return;
                    }
                }
            };
            $scope.saveMenuSort = function() {
                $scope.showMessage("你可以先添加一个菜单，然后开始为其设置响应动作");
                var menus = [];
                _.each($scope.weChatMenus, function(menu) {
                    menus.push({id: menu.id, no: menu.no});
                    _.each(menu.subMenus, function(submenu) {
                        menus.push({id: submenu.id, no: submenu.no});
                    });
                });
                $http.post("weChatMenu/saveMenusSort", menus).success(function () {
                    $scope.refresh();
                    alert("菜单排序保存成功！");
                });
            };
            $scope.cancelMenuSort = function() {
                $scope.showMessage("你可以先添加一个菜单，然后开始为其设置响应动作");
                $scope.refresh();
            };

            $scope.pull = function() {
                $scope.errors = null;
                $http.get("weChatMenu/pull", {params: {accountId: $scope.accountId}}).success(function () {
                    $scope.refresh();
                    alert("从微信公众号同步菜单成功！");
                }).error(function() {
                    alert("从微信公众号同步菜单失败！");
                });
            };
            $scope.publish = function() {
                $scope.errors = null;
                $http.get("weChatMenu/publish", {params: {accountId: $scope.accountId}}).success(function () {
                    $scope.refresh();
                    alert("自定义菜单发布成功！");
                }).error(function() {
                    alert("自定义菜单发布失败！");
                });
            };
        }
    ]);
})(angular);