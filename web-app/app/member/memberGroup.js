(function (angular) {
    "use strict";
    var module = angular.module('chat.member');
    module.controller('MemberGroupListController', ['$scope', 'MemberGroup',
        function ($scope, MemberGroup) {
            $scope.currentPage = 1;
            $scope.max = 50;
            $scope.refresh = function () {
                MemberGroup.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage', $scope.refresh);
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    MemberGroup.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('MemberGroupEditController', ['$scope', '$state', '$stateParams', 'MemberGroup', 'Account',
        function ($scope, $state, $stateParams, MemberGroup, Account) {
            MemberGroup.get({id: $stateParams.id}, function (data) {
                $scope.memberGroup = data;
            });
            $scope.submit = function () {
                var memberGroup = angular.copy($scope.memberGroup);
                if (memberGroup.name == null)delete memberGroup.name;
                if (memberGroup.email == null)delete memberGroup.email;
                MemberGroup.update(memberGroup, function (res) {
                    $state.transitionTo('member.group.list');
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
    module.controller('MemberGroupCreateController', ['$scope', '$state', '$stateParams', 'MemberGroup', 'Account',
        function ($scope, $state, $stateParams, MemberGroup, Account) {
            $scope.submit = function () {
                var memberGroup = angular.copy($scope.memberGroup);
                MemberGroup.save(memberGroup, function (res) {
                    $state.transitionTo('member.group.list');
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