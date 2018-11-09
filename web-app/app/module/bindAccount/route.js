var module = angular.module("chat.bindAccount", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
            .when('/bindAccountSetting', '/bindAccountSetting/list');
        $stateProvider
            .state('bindAccountSetting', {
                'abstract': true, url: '/bindAccountSetting', template: '<div ui-view></div>'
            })
            .state('bindAccountSetting.list', {
                url: '/list?accountId', templateUrl: 'app/module/bindAccount/bindAccountSetting/list.html',
                controller: "BindAccountSettingListController"
            }).state('bindAccountSetting.edit', {
                url: '/edit/:id', templateUrl: 'app/module/bindAccount/bindAccountSetting/form.html',
                controller: "BindAccountSettingEditController"
            }).state('bindAccountSetting.create', {
                url: '/create?accountId', templateUrl: 'app/module/bindAccount/bindAccountSetting/form.html',
                controller: "BindAccountSettingCreateController"
            });
    }]);