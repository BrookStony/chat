(function (angular) {
    "use strict";
    var module = angular.module('chat.bindAccount');
    module.controller('BindAccountSettingListController', ['$scope', '$rootScope', '$http', '$state', '$stateParams', 'BindAccountSetting',
        function ($scope, $rootScope, $http, $state, $stateParams, BindAccountSetting) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);
            $scope.sort = {
                sort: 'account.id',
                order: 'asc'
            };
            var sort = store.get(BIND_ACCOUNT_SETTING_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(BIND_ACCOUNT_SETTING_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                BindAccountSetting.query({sort: $scope.sort.sort,
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
                $state.transitionTo('bindAccountSetting.create', {accountId: $scope.accountId});
            };
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    BindAccountSetting.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('BindAccountSettingEditController', ['$scope', '$http', '$state', '$stateParams', 'BindAccountSetting', 'Account',
        function ($scope, $http, $state, $stateParams, BindAccountSetting, Account) {
            BindAccountSetting.get({id: $stateParams.id}, function (data) {
                $scope.bindAccountSetting = data;
            });

            $scope.submit = function () {
                var bindAccountSetting = angular.copy($scope.bindAccountSetting);
                BindAccountSetting.update(bindAccountSetting, function (res) {
                    $state.transitionTo('bindAccountSetting.list');
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
    module.controller('BindAccountSettingCreateController', ['$scope', '$state', '$stateParams', 'BindAccountSetting', 'Account',
        function ($scope, $state, $stateParams, BindAccountSetting, Account) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.bindAccountSetting = {account: {id: $scope.accountId, class: "com.kindsoft.chat.Account"}, type: "WECHAT"};
            $scope.submit = function () {
                var bindAccountSetting = angular.copy($scope.bindAccountSetting);
                BindAccountSetting.save(bindAccountSetting, function (res) {
                    $state.transitionTo('bindAccountSetting.list');
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