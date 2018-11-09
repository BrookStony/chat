var module = angular.module("chatScreen.message", []);

module.config([ "$stateProvider",
    function ($stateProvider) {
        $stateProvider.state({
            name: 'screenHome', url: '/screen', templateUrl: 'module/screen/message/list.html', controller: "MessageListController"
        });
    }]);