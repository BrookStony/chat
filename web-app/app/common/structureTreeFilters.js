(function (angular) {
    "use strict";
    var module = angular.module('chat.common');
    module.controller('StructureTreeFiltersController', ['$scope', '$state', '$stateParams',
        'Account', 'StructureTree',
        function ($scope, $state, $stateParams, Account, StructureTree) {
            $scope.currentAccount = {username: "微信公众号", id: null};
            Account.query(function (data) {
                $scope.accounts = data;
                if ($stateParams.accountId) {
                    $scope.currentAccount = _.find($scope.accounts, function (v) {
                        return v.id == $stateParams.accountId;
                    });
                }
            });
//            $scope.currentCampaign = {name: "推广计划", id: null};
//            if ($stateParams.accountId) {
//                StructureTree.query({lv: 0, id: $stateParams.accountId}, function (data) {
//                    $scope.campaigns = data;
//                    if ($stateParams.campaignId) {
//                        $scope.currentCampaign = _.find($scope.campaigns, function (v) {
//                            return v.id == $stateParams.campaignId;
//                        });
//                    }
//                });
//            }
//            $scope.currentAdGroup = {name: "推广单元", id: null};
//            if ($stateParams.campaignId) {
//                StructureTree.query({lv: 1, id: $stateParams.campaignId}, function (data) {
//                    $scope.adGroups = data;
//                    if ($stateParams.adGroupId) {
//                        $scope.currentAdGroup = _.find($scope.adGroups, function (v) {
//                            return v.id == $stateParams.adGroupId;
//                        });
//                    }
//                });
//            }
        }
    ]);
})(angular);
