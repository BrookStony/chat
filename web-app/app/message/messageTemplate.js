(function (angular) {
    "use strict";
    var module = angular.module('chat.message');
    module.controller('MessageTemplateListController', ['$scope', '$rootScope', '$http', '$state', '$stateParams', 'MessageTemplate',
        function ($scope, $rootScope, $http, $state, $stateParams, MessageTemplate) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);
            $scope.sort = {
                sort: 'title',
                order: 'asc'
            };
            var sort = store.get(MESSAGE_TEMPLATE_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(MESSAGE_TEMPLATE_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                MessageTemplate.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        accountId: $scope.accountId,
                        name: $scope.filter || '',
                        offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', $scope.refresh);
            $scope.create = function() {
                $state.transitionTo('messageTemplate.create', {accountId: $scope.accountId});
            };
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    MessageTemplate.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('MessageTemplateEditController', ['$scope', '$http', '$state', '$stateParams', 'MessageTemplate', 'Account',
        function ($scope, $http, $state, $stateParams, MessageTemplate, Account) {
            MessageTemplate.get({id: $stateParams.id}, function (data) {
                $scope.messageTemplate = data;
            });

            $scope.submit = function () {
                var messageTemplate = angular.copy($scope.messageTemplate);
                MessageTemplate.update(messageTemplate, function (res) {
                    $state.transitionTo('messageTemplate.list');
                }, function (res) {
                    if (res.status == 422) {
                        $scope.errors = res.data.errors;
                    } else {
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            }
        }
    ]);
    module.controller('MessageTemplateCreateController', ['$scope', '$state', '$stateParams', 'MessageTemplate', 'Account',
        function ($scope, $state, $stateParams, MessageTemplate, Account) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.messageTemplate = {account: {id: $scope.accountId, class: "com.kindsoft.chat.Account"}, type: "WECHAT"};
            $scope.submit = function () {
                var messageTemplate = angular.copy($scope.messageTemplate);
                MessageTemplate.save(messageTemplate, function (res) {
                    $state.transitionTo('messageTemplate.list');
                }, function (res) {
                    if (res.status == 422) {
                        $scope.errors = res.data.errors;
                    } else {
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            }
        }
    ]);
})(angular);