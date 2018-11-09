var module = angular.module("chat.massSend", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider){
        $urlRouterProvider
            .when('/massSend', '/massSend/send');
        $stateProvider
            .state('massSend', {
                'abstract': true, url: '/massSend', template: '<div ui-view></div>'
            })
            .state('massSend.send', {
                url: '/send?accountId', templateUrl:'app/massSend/massMessage/form.html',
                controller: 'MassSendIndexController'
            })
            .state('massSend.list', {
                url: '/list?accountId', templateUrl: 'app/massSend/massMessage/list.html',
                controller: 'MassSendListController'
            })
    }]);