(function (angular) {
    "use strict";
    var module = angular.module('chat.autoreply');
    module.controller('AutoReplyRuleListController', ['$scope', '$rootScope', '$http', '$state', '$stateParams', 'AutoReplyRule',
        function ($scope, $rootScope, $http, $state, $stateParams, AutoReplyRule) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);
            $scope.sort = {
                sort: 'name',
                order: 'asc'
            };
            var sort = store.get(AUTOREPLYRULE_SORT_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(AUTOREPLYRULE_SORT_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                AutoReplyRule.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        accountId: $scope.accountId,
                        name: $scope.filter || '',
                        offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function (datas, headers) {
                        $scope.datas = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', $scope.refresh);
            $scope.create = function() {
                $state.transitionTo('autoReplyRule.create', {accountId: $scope.accountId});
            };
            $scope.remove = function (item) {
                if (confirm('你确定要删除?')) {
                    AutoReplyRule.remove({id: item}, function () {
                        $scope.refresh();
                    }, function () {
                        alert("无法删除！");
                    });
                }
            };
        }
    ]);
    module.controller('AutoReplyRuleEditController', ['$scope', '$http', '$state', '$stateParams', '$modal', 'AutoReplyRule', 'Account',
        function ($scope, $http, $state, $stateParams, $modal, AutoReplyRule, Account) {
            $scope.messageType = "TEXT";
            $scope.sendText = "";
            $scope.sendMaterialId = null;
            $scope.sendArticleId = null;

            AutoReplyRule.get({id: $stateParams.id}, function (data) {
                $scope.autoReplyRule = data;
                if(data) {
                    $scope.messageType = data.messageType;
                    $scope.sendText = data.msg;
                    $scope.sendMaterialId = data.materialId;
                    $scope.sendArticleId = data.articleId;
                    if("IMAGE" == data.messageType) {
                        if(data.image) {
                            $scope.imageUrl = data.image.url;
                        }
                    }
                    else if("NEWS" == data.messageType) {
                        $scope.article = data.article;
                    }
                }
            });

            $scope.showTextArea = function(){
                $scope.messageType = "TEXT";
                $scope.sendText = "";
            };
            $scope.openImageModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-image-xlarge',
                    templateUrl: 'app/module/autoreply/autoReplyRule/imageModal.html',
                    controller: 'AutoReplyRuleImageModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedPicture){
                    $scope.messageType = "IMAGE";
                    $scope.sendMaterialId = selectedPicture.id;
                    $scope.imageUrl = selectedPicture.url;
                });
            };
            $scope.openArticleModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-article-xlarge',
                    templateUrl: 'app/module/autoreply/autoReplyRule/articleModal.html',
                    controller: 'AutoReplyRuleArticleModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedArticle){
                    $scope.messageType = "NEWS";
                    $scope.sendArticleId = selectedArticle.id;
                    $scope.article = selectedArticle;
                });
            };

            $scope.submit = function () {
                var autoReplyRule = angular.copy($scope.autoReplyRule);
                if("TEXT" == $scope.messageType) {
                    autoReplyRule.messageType = "TEXT";
                    autoReplyRule.msg = $scope.sendText;
                    autoReplyRule.materialId = null;
                    autoReplyRule.articleId = null;
                }
                else if("NEWS" == $scope.messageType) {
                    autoReplyRule.messageType = "NEWS";
                    autoReplyRule.msg = null;
                    autoReplyRule.materialId = null;
                    autoReplyRule.articleId = $scope.sendArticleId;
                }
                else {
                    autoReplyRule.messageType = $scope.messageType;
                    autoReplyRule.msg = null;
                    autoReplyRule.materialId = $scope.sendMaterialId;
                    autoReplyRule.articleId = null;
                }
                AutoReplyRule.update(autoReplyRule, function (res) {
                    $state.transitionTo('autoReplyRule.list');
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
    module.controller('AutoReplyRuleCreateController', ['$scope', '$state', '$stateParams', '$modal', 'AutoReplyRule', 'Account',
        function ($scope, $state, $stateParams, $modal, AutoReplyRule, Account) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.autoReplyRule = {account: {id: $scope.accountId, class: "com.kindsoft.chat.Account"},
                messageType: "TEXT", type: "KEYWORD", status: "ACTIVATE"};
            $scope.messageType = "TEXT";
            $scope.sendText = "";
            $scope.sendMaterialId = null;
            $scope.sendArticleId = null;

            $scope.showTextArea = function(){
                $scope.messageType = "TEXT";
                $scope.sendText = "";
            };
            $scope.openImageModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-image-xlarge',
                    templateUrl: 'app/module/autoreply/autoReplyRule/imageModal.html',
                    controller: 'AutoReplyRuleImageModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedPicture){
                    $scope.messageType = "IMAGE";
                    $scope.sendMaterialId = selectedPicture.id;
                    $scope.imageUrl = selectedPicture.url;
                });
            };
            $scope.openArticleModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-article-xlarge',
                    templateUrl: 'app/module/autoreply/autoReplyRule/articleModal.html',
                    controller: 'AutoReplyRuleArticleModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedArticle){
                    $scope.messageType = "NEWS";
                    $scope.sendArticleId = selectedArticle.id;
                    $scope.article = selectedArticle;
                });
            };

            $scope.submit = function () {
                var autoReplyRule = angular.copy($scope.autoReplyRule);
                if("TEXT" == $scope.messageType) {
                    autoReplyRule.messageType = "TEXT";
                    autoReplyRule.msg = $scope.sendText;
                    autoReplyRule.materialId = null;
                    autoReplyRule.articleId = null;
                }
                else if("NEWS" == $scope.messageType) {
                    autoReplyRule.messageType = "NEWS";
                    autoReplyRule.msg = null;
                    autoReplyRule.materialId = null;
                    autoReplyRule.articleId = $scope.sendArticleId;
                }
                else {
                    autoReplyRule.messageType = $scope.messageType;
                    autoReplyRule.msg = null;
                    autoReplyRule.materialId = $scope.sendMaterialId;
                    autoReplyRule.articleId = null;
                }
                AutoReplyRule.save(autoReplyRule, function (res) {
                    $state.transitionTo('autoReplyRule.list');
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
    module.controller('AutoReplyRuleImageModalController', ['$scope', '$state','$http', '$modalInstance', 'Picture', 'accountId', 'PictureGroup',
        function($scope, $state, $http, $modalInstance, Picture, accountId, PictureGroup){
            $scope.refreshPictureGroup = function(){
                PictureGroup.query({accountId: $scope.accountId},function(datas){
                    $scope.pictureGroups = datas;
                });
                /**未分组的图片个数*/
                $http.get('picture/findAllNotInGroup', { params: { accountId: $scope.accountId}})
                    .success(function(data){
                        $scope.withOutGroupPictures = data.length;
                    });
            };
            $scope.refreshPictureGroup();
            /**分组列表的样式*/
            $scope.addClass = function(event){
                angular.element(event).addClass("current_group");
                angular.element(event).siblings().removeClass("current_group");
            };

            $scope.picturePage = {currentPage: 1};
            $scope.max = 8;
            $scope.refresh = function (groupId) {
                $scope.selectedPicture = null;
                Picture.query({offset: ($scope.picturePage.currentPage - 1) * $scope.max, max: $scope.max,
                        accountId: accountId, pictureGroupId: groupId},
                    function (datas, headers) {
                        $scope.pictures = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };

            $scope.$watch('picturePage.currentPage', function(){
                $scope.refresh();
            });

            $scope.selectedPicture = null;
            $scope.showCover = function(picture,index){
                $('.select_inner').css('display','none');
                $('.select_inner'+index).css('display','block');
                $('.cover').css('display','none');
                $('.cover'+index).css('display','inline-block');
                $scope.selectedPicture = angular.copy(picture);
            };
            $scope.cancel = function(){
                $modalInstance.dismiss('cancel');
            };
            $scope.ok = function(){
                $modalInstance.close($scope.selectedPicture);
            };
        }
    ]);
    module.controller('AutoReplyRuleArticleModalController', ['$scope', '$state', '$modalInstance', 'Article', 'accountId',
        function($scope, $state, $modalInstance, Article, accountId){
            $scope.currentPage = 1;
            $scope.max = 5;
            $scope.totalItems = -1;
            $scope.articles = [] ;
            $scope.addArticle = function(){
                var offset = ($scope.currentPage - 1) * $scope.max;
                if(-1 == $scope.totalItems || offset < $scope.totalItems) {
                    Article.query({offset: offset, max: $scope.max, accountId: accountId},
                        function (datas, headers) {
                            $scope.totalItems = parseInt(headers('totalItems'));
                            if(datas.length > 0) {
                                _.each(datas, function (v) {
                                    $scope.articles.push(v);
                                });
                            }
                        }
                    );
                }
                ++$scope.currentPage;
            };
            $scope.addArticle();

            $scope.selectedArticle = null;
            var selectedIndex;
            $scope.showInner = function(index){
                $('.article_modal_select_inner').css('display','none');
                $('.article_modal_select_inner'+selectedIndex).css('display','block');
                $('.article_modal_select_inner'+index).css('display','block');
            };
            $scope.selectArticle = function(article,index){
                $('.article_modal_select_inner'+selectedIndex).css('display','none');
                selectedIndex = index;
                $('.article_modal_cover').css('display','none');
                $('.article_modal_cover'+index).css('display','inline-block');
                $scope.selectedArticle = angular.copy(article);
            };
            $scope.cancel = function(){
                $modalInstance.dismiss('cancel');
            };
            $scope.ok = function(){
                $modalInstance.close($scope.selectedArticle);
            };
        }
    ]);
})(angular);