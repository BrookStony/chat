var module = angular.module("chat.autoreply", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
            .when('/autoReplyRule', '/autoReplyRule/list');
        $stateProvider
            .state('autoReplyRule', {
                'abstract': true, url: '/autoReplyRule', template: '<div ui-view></div>'
            })
            .state('autoReplyRule.list', {
                url: '/list?accountId', templateUrl: 'app/module/autoreply/autoReplyRule/list.html',
                controller: "AutoReplyRuleListController"
            }).state('autoReplyRule.edit', {
                url: '/edit/:id', templateUrl: 'app/module/autoreply/autoReplyRule/form.html',
                controller: "AutoReplyRuleEditController"
            }).state('autoReplyRule.create', {
                url: '/create?accountId', templateUrl: 'app/module/autoreply/autoReplyRule/form.html',
                controller: "AutoReplyRuleCreateController"
            });
    }]);