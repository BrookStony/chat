(function (angular) {
    'use strict';
    var module = angular.module('chat', ['chat.filters', 'chat.directives', 'chat.domain',
        'ui.utils', 'ui.bootstrap', 'ui.router', 'ui.sortable', 'ui.select2', '$strap.directives',
        'platform.system', 'platform.task',
        'chat.dashboard', 'chat.setting', 'chat.common', 'chat.weChatMenu', 'chat.message', 'chat.member', 'chat.alert',
        'chat.material','wu.masonry','chat.massSend','chat.singleSend', 'chat.statistics', 'chat.autoreply', 'chat.bindAccount'
    ]);
    module.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
        function ($stateProvider, $urlRouterProvider, $httpProvider) {
            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
            $urlRouterProvider.otherwise('/');
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
                var $injector = angular.element("#brand").injector();
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
    module.run(['$rootScope', '$state', '$stateParams', 'Account',
        function ($rootScope, $state, $stateParams, Account) {
            $rootScope.loadingCount = 0;
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;

            $rootScope.accountId = $rootScope.accountId || store.get(CURRENT_ACCOUNT_KEY);
            Account.query(function (data) {
                $rootScope.accounts = data;
                if(!$rootScope.accountId){
                    if($rootScope.accounts.length > 0){
                        $rootScope.account = $rootScope.accounts[0];
                        $rootScope.accountId =  $rootScope.account.id;
                        store.set(CURRENT_ACCOUNT_KEY, $rootScope.accountId);
                    }
                }
                else {
                    $rootScope.account = _.find($rootScope.accounts, function (v) {
                        return v.id == $rootScope.accountId;
                    });
                }
            });

            $rootScope.selectAccount = function(account) {
                $rootScope.account = account;
                $rootScope.accountId = account.id;
                store.set(CURRENT_ACCOUNT_KEY, $rootScope.accountId);
                var params = angular.copy($stateParams);
                params.accountId = $rootScope.accountId;
                $state.transitionTo($state.$current.toString(), params);
            };

            $rootScope.navigationTo = function (state) {
                $state.transitionTo(state);
            };
    }]);
})(angular);


