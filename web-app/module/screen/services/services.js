'use strict';

/* Services */
(function (angular) {
    var domainService = angular.module('chat-screen.domain', ['ngResource']);

    function domainResource(resourceName) {
        return ['$resource', function ($resource) {
            return $resource(resourceName + '/:id', {id: '@id'}, {
                update: {method: 'PUT'}
            });
        }];
    }

    domainService.factory('ChatScreen', domainResource('chatScreens'));
    domainService.factory('ScreenMessage', domainResource('screenMessages'));
    domainService.factory('ScreenPhoto', domainResource('screenPhotos'));

})(angular);