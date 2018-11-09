(function (angular) {
    "use strict";
    var module = angular.module('chat.dashboard');
    module.controller('DashboardController', ['$scope', '$http', 'Account', 'dateRangePickerMixin',
        function ($scope, $http, Account, dateRangePickerMixin) {
//            dateRangePickerMixin($scope);
        }
    ]);
})(angular);
