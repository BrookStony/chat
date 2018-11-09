(function (angular) {
    "use strict";
    var module = angular.module('platform.system');

    module.controller('PersonalController', ['$scope', '$state', '$http', function ($scope, $state, $http) {
        $http.get("personal").success(function (data) {
            $scope.user = data;
        });
        $scope.submit = function () {
            var user = angular.copy($scope.user);
            if (user.name == null)delete user.name;
            if (user.email == null)delete user.email;
            $http.post("personal/update", user).success(function (data) {
                $scope.errors = null;
                alert("更新成功!");
                $state.transitionTo('setting.personal');
            }).error(function (data, status) {
                        if (status == 422) {
                            $scope.errors = data.errors;
                        } else {
                            alert("发生未知异常，请联系管理员!");
                        }
                    });
        }
    }]);
})(angular);
