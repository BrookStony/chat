var module = angular.module("chat.dashboard", []);

module.config([ "$stateProvider",
    function ($stateProvider) {
        $stateProvider.state({
            name: 'home', url: '/', templateUrl: 'app/dashboard/dashboard.html', controller: "DashboardController"
        });
    }]);