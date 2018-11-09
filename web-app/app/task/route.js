var module = angular.module("platform.task", []);

module.config(["$urlRouterProvider", "$stateProvider", function ($urlRouterProvider, $stateProvider) {
    $urlRouterProvider
        .when('/task', '/task/list');
    $stateProvider
        .state('task', {
            'abstract': true, url: '/task', template: '<div ui-view></div>'
        })
        .state('task.list', {
            url: '/list', templateUrl: 'app/task/task/list.html',
            controller: "TaskListController"
        }).state('task.edit', {
            url: '/edit/:id', templateUrl: 'app/task/task/form.html',
            controller: "TaskEditController"
        }).state('task.create', {
            url: '/create', templateUrl: 'app/task/task/form.html',
            controller: "TaskCreateController"
        });
}]);
