var module = angular.module("chat.singleSend",[]);

module.config(["$urlRouterProvider", "$stateProvider",
    function($urlRouterProvider,$stateProvider){
        $urlRouterProvider
            .when('/singleSend?userId', '/singleSend/toUser?userId');

        $stateProvider
            .state('singleSend', {
                'abstract': true, url: '/singleSend', template: '<div ui-view></div>'
            })
            .state('singleSend.toUser', {
                url: '/toUser?userId', templateUrl:'app/singleSend/form.html',
                controller: 'SingleSendIndexController'
            })
    }
]);
