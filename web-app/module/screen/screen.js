(function (angular) {
    'use strict';
    var module = angular.module('chatScreen', ['ui.utils', 'ui.bootstrap', 'ui.router',
        'ui.sortable', 'ui.select2', '$strap.directives', 'chatScreen.message'
    ]);
    module.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
        function ($stateProvider, $urlRouterProvider, $httpProvider) {
            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
            $urlRouterProvider.otherwise('/screen');
            var interceptor = ['$location', '$q', '$rootScope', function ($location, $q, $rootScope) {
                function success(response) {
                    $rootScope.loadingCount--;
                    if ($rootScope.loadingCount == 0) {
                        $rootScope.loading = false;
                    }
                    return response;
                }

                function error(response) {
                    $rootScope.loadingCount--;
                    if ($rootScope.loadingCount == 0) {
                        $rootScope.loading = false;
                    }
                    if (response.status === 401) {
                        location.href = "login/auth?m=true";
                        return $q.reject(response);
                    }
                    else {
                        return $q.reject(response);
                    }
                }

                return function (promise) {
                    return promise.then(success, error);
                }
            }];
            $httpProvider.responseInterceptors.push(interceptor);
            $httpProvider.interceptors.push('noCacheInterceptor');
            $httpProvider.defaults.transformRequest.push(function (data, headersGetter) {
                var $rootScope = $injector.get('$rootScope');
                $rootScope.loadingCount++;
                $rootScope.loading = true;
                return data;
            });
        }]).factory('noCacheInterceptor', function () {
        return {
            request: function (config) {
                if (config.method == 'GET' && config.url.indexOf('.html') === -1) {
                    var separator = config.url.indexOf('?') === -1 ? '?' : '&';
                    config.url = config.url + separator + 'noCache=' + new Date().getTime();
                }
                return config;
            }
        };
    });
    module.run(['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.loadingCount = 0;
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;

//            $rootScope.users = [{userName: "222aea", word: "123456"}, {userName: "222aea", word: "123456"}, {userName: "222aea", word: "123456"}];

            $rootScope.navigationTo = function (state) {
                $state.transitionTo(state);
            };
        }]);
})(angular);