(function (angular) {
    "use strict";
    var module = angular.module('chat.message');
    module.controller('ChatMessageListController', ['$scope', '$stateParams', 'ChatMessage',
        function ($scope, $stateParams, ChatMessage) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.sort = {
                sort: 'msgTime',
                order: 'asc'
            };
            var sort = store.get(CHAT_MESSAGE_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(CHAT_MESSAGE_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.selectedMsgTab = "total";
            $scope.refresh = function () {
                ChatMessage.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        accountId: $scope.accountId,
                        filter: $scope.filter || '',
                        msgTab: $scope.selectedMsgTab,
                        offset: ($scope.currentPage - 1) * $scope.max,
                        max: $scope.max},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', $scope.refresh);
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    ChatMessage.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
            $scope.searchMessage = function (tab) {
                $scope.selectedMsgTab = tab;
                $scope.refresh();
            }
        }
    ]);
    module.controller('ChatMessageShowController', ['$scope', '$state', '$stateParams', 'ChatMessage',
        function ($scope, $state, $stateParams, ChatMessage) {
            ChatMessage.get({id: $stateParams.id}, function (data) {
                $scope.chatMessage = data;
            });
        }
    ]);
})(angular);