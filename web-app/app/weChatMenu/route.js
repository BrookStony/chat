var module = angular.module("chat.weChatMenu", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
            .when('/weChatMenu', '/weChatMenu/index');
        $stateProvider
            .state('weChatMenu', {
                'abstract': true, url: '/weChatMenu', template: '<div ui-view></div>'
            })
            .state('weChatMenu.index', {
                url: '/index?accountId', templateUrl: 'app/weChatMenu/weChatMenu.html',
                controller: "WeChatMenuController"
            });
    }]);