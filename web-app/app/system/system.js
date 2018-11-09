(function (angular) {
    "use strict";
    var module = angular.module('platform.system');
    module.controller('SystemController', ['$scope', 'Setting',
        function ($scope, Setting) {
            $scope.currentPage = 1;
            $scope.max = 50;
            $scope.refresh = function () {
                $scope.errors = null;
                Setting.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage', $scope.refresh);
            $scope.submit = function (setting) {
                Setting.update(setting, function (res) {
                    $scope.refresh();
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