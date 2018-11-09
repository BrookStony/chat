var module = angular.module("chat.statistics",[]);

module.config(["$urlRouterProvider", "$stateProvider",
    function($urlRouterProvider, $stateProvider){
        $urlRouterProvider
            .when('/memberStat?accountId', '/memberStat/growth?accountId');

        $stateProvider
            .state('memberStat', {
                'abstract': true, url: '/memberStat', template: '<div ui-view></div>'
            })
            .state('memberStat.growth', {
                url: '/growth?accountId', templateUrl:'app/statistics/member/growth.html',
                controller: 'MemberGrowthStatController'
            })
            .state('memberStat.attributes', {
                url: '/attributes?accountId', templateUrl:'app/statistics/member/attributes.html',
                controller: 'MemberAttributesStatController'
            });
    }
]);