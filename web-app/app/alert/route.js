var module = angular.module("chat.alert", []);

module.config(["$urlRouterProvider", "$stateProvider", function ($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.when('/alert', '/alert/list');
    $stateProvider.state('alert', {
        'abstract': true, url: '/alert', template: '<div ui-view></div>'
    }).state('alert.list', {
        url: '/list?id&accountId', templateUrl: 'app/alert/list.html',
        controller: "AlertController"
    }).state('alert.setting', {
        url: '/setting', templateUrl: 'app/alert/setting.html',
        controller: "AlertSettingController"
    });
}]);