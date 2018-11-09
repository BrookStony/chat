(function (angular) {
    "use strict";
    var module = angular.module('chat.member');
    module.controller('MemberListController', ['$scope', '$rootScope', '$http', '$stateParams', 'Member',
        function ($scope, $rootScope, $http, $stateParams, Member) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);
            $scope.sort = {
                sort: 'name',
                order: 'asc'
            };
            var sort = store.get(MEMBER_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(MEMBER_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                Member.query({sort: $scope.sort.sort,
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
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    Member.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
            $scope.refreshMember = function() {
                $http.post("member/synchMembers", {accountId: $scope.accountId})
                    .success(function (data, status, headers, config) {
                        $scope.refresh();
                    }).error(function (data, status, headers, config) {
                        alert("发生未知异常，请联系管理员!");
                    });
            }
        }
    ]);
    module.controller('MemberEditController', ['$scope', '$http', '$state', '$stateParams', 'Member', 'MemberGroup', 'Account',
        function ($scope, $http, $state, $stateParams, Member, MemberGroup, Account) {
            Member.get({id: $stateParams.id}, function (data) {
                $scope.member = data;
                $scope.member.sex = $scope.member.sex.name;

                MemberGroup.query(function (data) {
                    $scope.memberGroups = data;
                    $scope.member.group = _.find($scope.memberGroups, function (v) {
                        return v.id == $scope.member.group.id;
                    });
                });
            });

            $scope.submit = function () {
                var member = angular.copy($scope.member);
                if (member.name == null)delete member.name;
                if (member.email == null)delete member.email;
                Member.update(member, function (res) {
                    $state.transitionTo('member.list');
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
    module.controller('MemberCreateController', ['$scope', '$state', '$stateParams', 'Member', 'Account',
        function ($scope, $state, $stateParams, Member, Account) {
            $scope.submit = function () {
                var member = angular.copy($scope.member);
                Member.save(member, function (res) {
                    $state.transitionTo('member.member.list');
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