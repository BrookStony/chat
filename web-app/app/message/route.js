var module = angular.module("chat.message", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
            .when('/chatMessage', '/chatMessage/list')
            .when('/templateMessage', '/templateMessage/list')
            .when('/messageTemplate', '/messageTemplate/list');
        $stateProvider
            .state('chatMessage', {
                'abstract': true, url: '/chatMessage', template: '<div ui-view></div>'
            })
            .state('chatMessage.list', {
                url: '/list?accountId', templateUrl: 'app/message/chatMessage/list.html',
                controller: "ChatMessageListController"
            }).state('chatMessage.edit', {
                url: '/edit/:id', templateUrl: 'app/message/chatMessage/form.html',
                controller: "ChatMessageEditController"
            }).state('chatMessage.create', {
                url: '/create', templateUrl: 'app/message/chatMessage/form.html',
                controller: "ChatMessageCreateController"
            })
            .state('templateMessage', {
                'abstract': true, url: '/templateMessage', template: '<div ui-view></div>'
            })
            .state('templateMessage.list', {
                url: '/list?accountId', templateUrl: 'app/message/templateMessage/list.html',
                controller: "TemplateMessageListController"
            }).state('templateMessage.show', {
                url: '/show/:id', templateUrl: 'app/message/templateMessage/form.html',
                controller: "TemplateMessageShowController"
            })
            .state('messageTemplate', {
                'abstract': true, url: '/messageTemplate', template: '<div ui-view></div>'
            })
            .state('messageTemplate.list', {
                url: '/list?accountId', templateUrl: 'app/message/messageTemplate/list.html',
                controller: "MessageTemplateListController"
            }).state('messageTemplate.edit', {
                url: '/edit/:id', templateUrl: 'app/message/messageTemplate/form.html',
                controller: "MessageTemplateEditController"
            }).state('messageTemplate.create', {
                url: '/create?accountId', templateUrl: 'app/message/messageTemplate/form.html',
                controller: "MessageTemplateCreateController"
            });
    }]);