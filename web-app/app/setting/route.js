var module = angular.module("chat.setting", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
                .when('/account', '/account/list');
        $stateProvider
                .state('account', {
                    'abstract': true, url: '/account', template: '<div ui-view></div>'
                })
                .state('account.list', {
                    url: '/list', templateUrl: 'app/setting/account/list.html',
                    controller: "AccountListController"
                }).state('account.edit', {
                    url: '/edit/:id', templateUrl: 'app/setting/account/form.html',
                    controller: "AccountEditController"
                }).state('account.create', {
                    url: '/create', templateUrl: 'app/setting/account/form.html',
                    controller: "AccountCreateController"
                });
    }]);