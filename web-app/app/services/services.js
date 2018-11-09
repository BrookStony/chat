'use strict';

/* Services */
(function (angular) {
    var domainService = angular.module('chat.domain', ['ngResource']);

    function domainResource(resourceName) {
        return ['$resource', function ($resource) {
            return $resource(resourceName + '/:id', {id: '@id'}, {
                update: {method: 'PUT'}
            });
        }];
    }

    domainService.factory('StructureTree', ['$resource', function ($resource) {
        return $resource('structureTrees', {}, {
            update: {method: 'PUT'}
        });
    }]);
    domainService.factory('Article', domainResource('articles'));
    domainService.factory('Audio', domainResource('audios'));
    domainService.factory('Picture', domainResource('pictures'));
    domainService.factory('Video', domainResource('videos'));
    domainService.factory('WeChatMenu', domainResource('weChatMenus'));
    domainService.factory('Member', domainResource('members'));
    domainService.factory('MemberGroup', domainResource('memberGroups'));
    domainService.factory('MemberCard', domainResource('memberCards'));
    domainService.factory('MemberGrade', domainResource('memberGrades'));
    domainService.factory('ChatMessage', domainResource('chatMessages'));
    domainService.factory('MassMessage', domainResource('massMessages'));
    domainService.factory('TemplateMessage', domainResource('templateMessages'));
    domainService.factory('MessageTemplate', domainResource('messageTemplates'));
    domainService.factory('AutoReplyRule', domainResource('autoReplyRules'));
    domainService.factory('BindAccountSetting', domainResource('bindAccountSettings'));
    domainService.factory('Account', domainResource('accounts'));
    domainService.factory('Location', domainResource('locations'));
    domainService.factory('User', domainResource('users'));
    domainService.factory('Role', domainResource('roles'));
    domainService.factory('UserGroup', domainResource('userGroups'));
    domainService.factory('Menu', domainResource('menus'));
    domainService.factory('Notification', domainResource('notifications'));
    domainService.factory('Operlog', domainResource('operlogs'));
    domainService.factory('Task', domainResource('tasks'));
    domainService.factory('AlertType', domainResource('alertType'));
    domainService.factory('Alert', domainResource('alert'));
    domainService.factory('AlertHistory', domainResource('alertHistories'));
    domainService.factory('PictureGroup', domainResource('pictureGroups'));

})(angular);


