var module = angular.module("chat.member", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
            .when('/member?accountId', '/member/list?accountId')
            .when('/memberGroup', '/memberGroup/list');
        $stateProvider
            .state('member', {
                'abstract': true, url: '/member', template: '<div ui-view></div>'
            })
            .state('member.list', {
                url: '/list?accountId', templateUrl: 'app/member/member/list.html',
                controller: "MemberListController"
            }).state('member.edit', {
                url: '/edit/:id', templateUrl: 'app/member/member/form.html',
                controller: "MemberEditController"
            }).state('member.create', {
                url: '/create', templateUrl: 'app/member/member/form.html',
                controller: "MemberCreateController"
            })
            .state('memberGroup', {
                'abstract': true, url: '/memberGroup', template: '<div ui-view></div>'
            })
            .state('memberGroup.list', {
                url: '/list?accountId', templateUrl: 'app/member/group/list.html',
                controller: "MemberGroupListController"
            }).state('memberGroup.edit', {
                url: '/edit/:id', templateUrl: 'app/member/group/form.html',
                controller: "MemberGroupEditController"
            }).state('memberGroup.create', {
                url: '/create', templateUrl: 'app/member/group/form.html',
                controller: "MemberGroupCreateController"
            });
    }]);