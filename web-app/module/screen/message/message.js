(function (angular) {
    "use strict";
    var module = angular.module('chatScreen.message');
    module.controller('MessageListController', ['$scope', '$http', '$state', '$stateParams',
        function ($scope, $http, $state, $stateParams) {
            $scope.userList = [];
        }
    ]);
})(angular);