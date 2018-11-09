var module = angular.module("platform.system", []);

module.config(["$urlRouterProvider", "$stateProvider", function ($urlRouterProvider, $stateProvider) {
    $urlRouterProvider
        .when('/user', '/user/list')
        .when('/role', '/role/list')
        .when('/userGroup', '/userGroup/list')
        .when('/menu', '/menu/list')
        .when('/notification', '/notification/list')
        .when('/operlog', '/operlog/list');
    $stateProvider
        .state('user', {
            'abstract': true, url: '/user', template: '<div ui-view></div>'
        })
        .state('user.list', {
            url: '/list', templateUrl: 'app/system/user/list.html',
            controller: "UserListController"
        }).state('user.edit', {
            url: '/edit/:id', templateUrl: 'app/system/user/form.html',
            controller: "UserEditController"
        }).state('user.create', {
            url: '/create', templateUrl: 'app/system/user/form.html',
            controller: "UserCreateController"
        })
        .state('role', {
            'abstract': true, url: '/role', template: '<div ui-view></div>'
        })
        .state('role.list', {
            url: '/list', templateUrl: 'app/system/role/list.html',
            controller: "RoleListController"
        }).state('role.edit', {
            url: '/edit/:id', templateUrl: 'app/system/role/edit.html',
            controller: "RoleEditController"
        }).state('role.editMenus', {
            url: '/editMenus/:id', templateUrl: 'app/system/role/menus.html',
            controller: "RoleMenuEditController"
        }).state('role.create', {
            url: '/create', templateUrl: 'app/system/role/form.html',
            controller: "RoleCreateController"
        })
        .state('userGroup', {
            'abstract': true, url: '/userGroup', template: '<div ui-view></div>'
        })
        .state('userGroup.list', {
            url: '/list', templateUrl: 'app/system/userGroup/list.html',
            controller: "UserGroupListController"
        }).state('userGroup.edit', {
            url: '/edit/:id', templateUrl: 'app/system/userGroup/form.html',
            controller: "UserGroupEditController"
        }).state('userGroup.create', {
            url: '/create', templateUrl: 'app/system/userGroup/form.html',
            controller: "UserGroupCreateController"
        })
        .state('menu', {
            'abstract': true, url: '/menu', template: '<div ui-view></div>'
        })
        .state('menu.list', {
            url: '/list', templateUrl: 'app/system/menu/list.html',
            controller: "MenuListController"
        }).state('menu.edit', {
            url: '/edit/:id', templateUrl: 'app/system/menu/form.html',
            controller: "MenuEditController"
        }).state('menu.create', {
            url: '/create', templateUrl: 'app/system/menu/form.html',
            controller: "MenuCreateController"
        })
        .state('notification', {
            'abstract': true, url: '/notification', template: '<div ui-view></div>'
        })
        .state('notification.list', {
            url: '/list', templateUrl: 'app/system/notification/list.html',
            controller: "NotificationListController"
        }).state('notification.edit', {
            url: '/edit/:id', templateUrl: 'app/system/notification/form.html',
            controller: "NotificationEditController"
        }).state('notification.create', {
            url: '/create', templateUrl: 'app/system/notification/form.html',
            controller: "NotificationCreateController"
        })
        .state('operlog', {
            'abstract': true, url: '/operlog', template: '<div ui-view></div>'
        })
        .state('operlog.list', {
            url: '/list', templateUrl: 'app/system/operlog/list.html',
            controller: "OperlogListController"
        }).state('operlog.edit', {
            url: '/edit/:id', templateUrl: 'app/system/operlog/form.html',
            controller: "OperlogEditController"
        }).state('operlog.create', {
            url: '/create', templateUrl: 'app/system/operlog/form.html',
            controller: "OperlogCreateController"
        });
}]);
