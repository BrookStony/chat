var module = angular.module("chat.material", []);

module.config(["$urlRouterProvider", "$stateProvider",
    function ($urlRouterProvider, $stateProvider) {
        $urlRouterProvider
            .when('/material?accountId', '/material/article?accountId')
            .when('/material/article?accountId', '/material/article/card?accountId')
            .when('/material/picture?accountId', '/material/picture/list?accountId')
            .when('/material/audio?accountId', '/material/audio/list?accountId')
            .when('/material/video?accountId', '/material/video/list?accountId')
        ;
        $stateProvider
            .state({
                'abstract': true, name: 'material', url: '/material', templateUrl: 'app/material/material.html'
            })
            .state('material.article', {
                'abstract': true, url: '/article', template: '<div ui-view></div>'
            })
            .state('material.article.card', {
                url: '/card?accountId', templateUrl: 'app/material/article/articles.html',
                controller: "MaterialArticleController"
            })
            .state('material.article.list', {
                url: '/list?accountId', templateUrl: 'app/material/article/list.html',
                controller: "MaterialArticleListController"
            })
            .state('material.article.create', {
                url: '/create?accountId', templateUrl: 'app/material/article/createArticle.html',
                controller: "MaterialArticleCreateController"
            })
            .state('material.article.edit', {
                url: '/edit/:id', templateUrl: 'app/material/article/editArticle.html',
                controller: "MaterialArticleEditController"
            })
            .state('material.article.createMulArticle', {
                url: '/createMulArticle?accountId', templateUrl: 'app/material/article/createMulArticle.html',
                controller: "MaterialMulArticleCreateController"
            })
            .state('material.article.editMulArticle', {
                url: '/editMulArticle/:id', templateUrl: 'app/material/article/editMulArticle.html',
                controller: "MaterialMulArticleEditController"
            })
            .state('material.picture', {
                'abstract': true, url: '/picture', template: '<div ui-view></div>'
            })
            .state('material.picture.list', {
                url: '/list?accountId', templateUrl: 'app/material/picture/picture.html',
                controller: "MaterialPictureController"
            })
            .state('material.audio', {
                'abstract': true, url: '/audio', template: '<div ui-view></div>'
            })
            .state('material.audio.list', {
                url: '/list?accountId', templateUrl: 'app/material/audio/audio.html',
                controller: "MaterialAudioController"
            })
            .state('material.video', {
                'abstract': true, url: '/video', template: '<div ui-view></div>'
            })
            .state('material.video.list', {
                url: '/list?accountId', templateUrl: 'app/material/video/video.html',
                controller: "MaterialVideoController"
            })
            .state('material.video.create', {
                url: '/create', templateUrl: 'app/material/video/create.html',
                controller: "MaterialVideoCreateController"
            })
            .state('material.video.edit', {
                url: '/edit/:id', templateUrl: 'app/material/video/create.html',
                controller: "MaterialVideoEditController"
            })
        ;
    }]);