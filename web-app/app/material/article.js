(function (angular) {
    "use strict";
    var module = angular.module('chat.material');
    module.controller('MaterialArticleController',['$rootScope', '$scope', '$state', '$stateParams', '$http', 'Article',
        function($rootScope, $scope, $state, $stateParams, $http, Article){
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);

            $scope.currentPage = 1;
            $scope.max = 5;

            $scope.loadFirst = true;
            $scope.totalItems = 0;
            $scope.articles = [] ;
            $scope.addArticle = function(){
                var offset = ($scope.currentPage - 1) * $scope.max;
                if($scope.loadFirst || offset < $scope.totalItems) {
                    Article.query({offset: offset, max: $scope.max, accountId: $scope.accountId},
                        function (datas, headers) {
                            $scope.totalItems = parseInt(headers('totalItems'));
                            if(datas.length > 0) {
                                _.each(datas, function (v) {
                                    $scope.articles.push(v);
                                });
                            }
                            if($scope.articles.length > 0) {
                                $scope.hasArticle = true;
                            } else {
                                $scope.hasArticle = false;
                            }
                        }
                    );
                }
                $scope.loadFirst = false;
            };
            $scope.addArticle();

            $(document).scroll(function(){
                var marginBot = 0;
                if (document.documentElement.scrollTop){
                    marginBot = document.documentElement.scrollHeight-
                        (document.documentElement.scrollTop+document.body.scrollTop)-
                        document.documentElement.clientHeight;
                } else {
                    marginBot = document.body.scrollHeight-document.body.scrollTop- document.body.clientHeight;
                }
                marginBot = document.body.scrollHeight-document.body.scrollTop- document.body.clientHeight;
                if(marginBot<=0){
                    ++$scope.currentPage;
                    $scope.addArticle();
                }
            });
            $scope.transToArticleList = function(){
                $state.transitionTo('material.article.list', {accountId: $scope.accountId});
            };
            $scope.transToCreateArticle = function(){
                $state.transitionTo('material.article.create', {accountId: $scope.accountId});
            };
            $scope.transToMulCreateArticle = function(){
                $state.transitionTo('material.article.createMulArticle', {accountId: $scope.accountId});
            };
            $scope.remove =function(id, index){
                if(confirm("确认删除?")){
                    $http.post("article/remove",{ id: id})
                        .success(function(){
                            $scope.articles.splice(index, 1);
                        })
                        .error(function() {
                            alert("发生未知异常，请联系管理员!");
                        });
                }
            };
        }
    ]);
    module.controller('MaterialArticleListController',['$rootScope', '$scope', '$state', '$stateParams', '$http', 'Article',
        function($rootScope, $scope, $state, $stateParams, $http, Article){
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);

            $scope.sort = {
                sort: 'dateCreated',
                order: 'desc'
            };
            var sort = store.get(CHAT_ARITICLE_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(CHAT_ARITICLE_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.totalItems = 0;
            $scope.refresh = function () {
                Article.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        accountId: $scope.accountId,
                        filter: $scope.filter || '',
                        offset: ($scope.currentPage - 1) * $scope.max,
                        max: $scope.max},
                    function (datas, headers) {
                        $scope.articles = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', $scope.refresh);

            $scope.transToArticleCard = function(){
                $state.transitionTo('material.article.card', {accountId: $scope.accountId});
            };
            $scope.transToCreateArticle = function(){
                $state.transitionTo('material.article.create', {accountId: $scope.accountId});
            };
            $scope.transToMulCreateArticle = function(){
                $state.transitionTo('material.article.createMulArticle', {accountId: $scope.accountId});
            };
            $scope.remove =function(id){
                if(confirm("确认删除?")){
                    $http.post("article/remove",{ id: id})
                        .success(function(){
                            $scope.refresh();
                        })
                        .error(function() {
                            alert("发生未知异常，请联系管理员!");
                        });
                }
            };
        }
    ]);
    module.controller('MaterialArticleCreateController',['$rootScope', '$scope', '$state', '$stateParams', '$http', 'Article','$modal',
        function($rootScope, $scope, $state, $stateParams, $http, Article, $modal){
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }

            $scope.article = {content: "", coverImage: null};
            $scope.showDescription = false;
            $scope.showOriginalUrl = false;
            $scope.file_changed = function(){
                var obj = document.getElementById("coverImage");
                var fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#coverImage').val("");
                }
                else {
                    uploadArticlePicture();
                }
            };
            var uploadArticlePicture = function(){
                $.ajaxFileUpload({
                    url: 'article/upload',
                    secureuri: false,
                    fileElementId: 'coverImage',
                    dataType: 'json',
                    data: {accountId: $scope.accountId},
                    success: function (data){
                        if(data && 1 == data.code){
                            $scope.$apply(function(){
                                $scope.article.coverImage = data.picture;
                            });
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };
            $scope.openImgModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-width-xlarge',
                    templateUrl: 'app/singleSend/imageModal.html',
                    controller: 'ArticleImageModalController',
                    resolve: {
                        accountId: function(){
                            return  $scope.accountId
                        }
                    }
                });
                modalInstance.result.then(function(selectedPicture){
                    $scope.article.coverImage = selectedPicture
                });
            };

            $scope.removeCoverImage = function(){
                if(confirm("确认删除？")){
                    if($scope.article.coverImage){
                        $http.post('article/removeCoverImage',{coverImageId: $scope.article.coverImage.id})
                            .success(function(){
                                $scope.article.coverImage = null;
                            })
                            .error(function(){
                                alert("发生未知异常，请联系管理员!");
                            })
                    }
                }
            };

            $scope.submit = function() {
                if(!$scope.article.coverImage){
                    alert("请选择一张图片！");
                    return
                }
                if(!$scope.article.content || "" == $scope.article.content){
                    alert("图文内容不能为空！");
                    return
                }
                var article = angular.copy($scope.article);
                if (article.description == null) delete article.description;
                $http.post('article/save',{accountId: $scope.accountId, article: article})
                    .success(function(){
                        $state.transitionTo('material.article.card', {accountId: $scope.accountId});
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
    module.controller('MaterialArticleEditController',['$rootScope', '$scope', '$state', '$http', 'Article', '$stateParams','$modal',
        function($rootScope, $scope, $state, $http, Article, $stateParams, $modal){
            $http.post('article/edit',{id: $stateParams.id})
                .success(function(data){
                    $scope.article = data ;
                    $scope.accountId = data.account.id;
                    if(data.description){
                        $scope.showDescription = true;
                    }
                    if(data.originalUrl) {
                        $scope.showOriginalUrl = true;
                    }
                });

            $scope.file_changed = function(){
                var obj = document.getElementById("coverImage");
                var fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#coverImage').val("");
                }else{
                    uploadArticlePicture();
                }
            };
            var uploadArticlePicture = function(){
                $.ajaxFileUpload({
                    url: 'article/upload',
                    secureuri: false,
                    fileElementId: 'coverImage',
                    dataType: 'json',
                    data: {accountId: $scope.accountId},
                    success: function (data){
                        if(data && 1 == data.code){
                            $scope.$apply(function(){
                                $scope.article.coverImage = data.picture;
                            });
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };

            $scope.openImgModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-width-xlarge',
                    templateUrl: 'app/singleSend/imageModal.html',
                    controller: 'ArticleImageModalController',
                    resolve: {
                        accountId: function(){
                            return  $scope.accountId
                        }
                    }
                });
                modalInstance.result.then(function(selectedPicture){
                    $scope.article.coverImage = selectedPicture
                });
            };

            $scope.removeCoverImage = function(){
                if(confirm("确认删除？")){
                    if($scope.article.coverImage){
                        $http.post('article/removeCoverImage',{coverImageId: $scope.article.coverImage.id})
                            .success(function(){
                                $scope.article.coverImage = null;
                            })
                            .error(function(){
                                alert("发生未知异常，请联系管理员!");
                            })
                    }
                }
            };

            $scope.submit = function() {
                if(!$scope.article.coverImage){
                    alert("请选择一张图片！");
                    return
                }

                var article = angular.copy($scope.article);
                if (article.description == null) delete article.description;
                $http.post('article/update',{accountId: $scope.accountId, article: article})
                    .success(function(){
                        $state.transitionTo('material.article.card', {accountId: $scope.accountId});
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);

    module.controller('MaterialMulArticleCreateController',['$rootScope', '$scope', '$state', '$stateParams', '$http', 'Article','$modal',
        function($rootScope, $scope, $state, $stateParams, $http, Article, $modal){
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }

            $scope.num = 0;
            $scope.articleItemEditorTopHeight = 184;
            $scope.articleFormDispaly = true;
            $scope.articleItemFormDispaly = false;
            $scope.article = {title: "", author: "", content: "", coverImage: null, originalUrl: ""};
            $scope.articleItems = [{ title: "", author: "", content: "", coverImage: null, originalUrl: ""}];

            $scope.showArticleEditForm = function(){
                $scope.articleFormDispaly = true;
                $scope.articleItemFormDispaly = false;
              $("#articleItemEditorTop").height($scope.articleItemEditorTopHeight);
            };

            /**调整article的编辑区域*/
            $scope.showArticleItemForm = function(index){
                $scope.num = index;
                $scope.articleFormDispaly = false;
                $scope.articleItemFormDispaly = true;
                var height = (index) * 120;
                $("#articleItemEditorTop").height($scope.articleItemEditorTopHeight + height);
            };

            $scope.deleteArticleItem = function(index){
                if($scope.articleItems.length <= 1){
                    alert("无法删除，多条图文至少需要2条消息");
                    return ;
                }
                $scope.articleItems.splice(index,1);
            };

            //添加一个次要article
            $scope.addArticleItem = function(){
                $scope.articleItems.push({ title: "", author: "", content: "", coverImage: null, originalUrl: ""});
            };

            /**选择图片上传前判断图片是否超出限制*/
            $scope.fileOnChange = function(fileId) {
                var obj = document.getElementById(fileId);
                var fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#' + fileId).val("");
                    return
                }else{
                    uploadArticlePicture(fileId);
                }
            };

            /**上传图片函数*/
            var uploadArticlePicture = function(fileId){
                $.ajaxFileUpload({
                    url: 'article/upload',
                    secureuri: false,
                    fileElementId: fileId,
                    dataType: 'json',
                    data: {accountId: $scope.accountId},
                    success: function (data){
                        if(data && 1 == data.code){
                            $scope.$apply(function(){
                                if("articleCoverImage" == fileId){
                                    $scope.article.coverImage = data.picture;
                                }
                                else {
                                    ($scope.articleItems)[$scope.num].coverImage = data.picture;
                                }
                            });
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };
            /**从图库中选择图片*/
            $scope.openImgModal = function(articleCover){
                var modalInstance = $modal.open({
                    windowClass: 'model-width-xlarge',
                    templateUrl: 'app/singleSend/imageModal.html',
                    controller: 'ArticleImageModalController',
                    resolve: {
                        accountId: function(){
                            return  $scope.accountId
                        }
                    }
                });
                modalInstance.result.then(function(selectedPicture){
                    if("articleCoverImage" == articleCover){
                        $scope.article.coverImage = selectedPicture;
                    }
                    else {
                        ($scope.articleItems)[$scope.num].coverImage = selectedPicture;
                    }
                });
            };
            /**删除图片*/
            $scope.removeCoverImage = function(){
                if(confirm("确认删除？")){
                    if($scope.article.coverImage){
                        $http.post('article/removeCoverImage',{coverImageId: $scope.article.coverImage.id})
                            .success(function(){
                                $scope.article.coverImage = null;
                            })
                            .error(function(){
                                alert("发生未知异常，请联系管理员!");
                            })
                    }
                }
            };
            $scope.removeItemCoverImage = function(){
                if(confirm("确认删除？")){
                    if(($scope.articleItems)[$scope.num].coverImage){
                        $http.post('article/removeCoverImage',{coverImageId: ($scope.articleItems)[$scope.num].coverImage.id})
                            .success(function(){
                                ($scope.articleItems)[$scope.num].coverImage = null;
                            })
                            .error(function(){
                                alert("发生未知异常，请联系管理员!");
                            })
                    }
                }
            };

            $scope.remove = function(){
                if(confirm("确认删除？")){
                    $http.post('article/remove',{coverImage: ($scope.articles)[$scope.num].coverImage, accountId: $rootScope.accountId})
                        .success(function(){
                            ($scope.articles)[$scope.num].coverImage = "";
                            $('.preview').css('display', 'none');
                            $('.previewMsg'+$scope.num).css('display','inline-block');
                            $('.preview_img'+$scope.num).attr("src","");
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        })
                }
            };

            /**提交*/
            $scope.submit = function(){
                /**判断是否有选择图片*/
                if(!$scope.article.coverImage){
                    alert("请上传封面图片！");
                    return
                }
                if($scope.articleItems.length < 1){
                    alert("多条图文至少需要2条消息！");
                    return
                }

                for(var i=0; i<($scope.articleItems).length; i++){
                    if(null == ($scope.articleItems)[i].coverImage){
                        $scope.showArticleItemForm(i);
                        alert("请选择一张图片！");
                        return
                    }
                }

                var article = angular.copy($scope.article);
                var articleItems = angular.copy($scope.articleItems);
                $http.post('article/saveMulArticle',{accountId: $scope.accountId, article: article, articleItems: articleItems})
                    .success(function(){
                        $state.transitionTo('material.article.card', {accountId: $scope.accountId});
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");

                    });
            };
        }
    ]);
    module.controller('MaterialMulArticleEditController',['$rootScope', '$scope', '$state', '$stateParams', '$http', 'Article','$modal',
        function($rootScope, $scope, $state, $stateParams, $http, Article, $modal){
            $scope.num = 0;
            $scope.articleItemEditorTopHeight = 198;
            $scope.articleFormDispaly = true;
            $scope.articleItemFormDispaly = false;
            $http.post('article/editMulArticle',{id: $stateParams.id})
                .success(function(data){
                    $scope.accountId = data.article.account.id;
                    $scope.article = data.article;
                    $scope.articleItems = data.articleItems;
                });

            $scope.showArticleEditForm = function(){
                $scope.articleFormDispaly = true;
                $scope.articleItemFormDispaly = false;
                $("#articleItemEditorTop").height($scope.articleItemEditorTopHeight);
            };

            /**调整article的编辑区域*/
            $scope.showArticleItemForm = function(index){
                $scope.num = index;
                $scope.articleFormDispaly = false;
                $scope.articleItemFormDispaly = true;
                var height = (index) * 120;
                $("#articleItemEditorTop").height($scope.articleItemEditorTopHeight + height);
            };

            $scope.deleteArticleItem = function(index){
                if($scope.articleItems.length <= 1){
                    alert("无法删除，多条图文至少需要2条消息");
                    return ;
                }
                $scope.articleItems.splice(index, 1);
            };

            //添加一个次要article
            $scope.addArticleItem = function(){
                $scope.articleItems.push({ title: "", author: "", content: "", coverImage: null, originalUrl: ""});
            };

            /**选择图片上传前判断图片是否超出限制*/
            $scope.fileOnChange = function(fileId) {
                var obj = document.getElementById(fileId);
                var fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#' + fileId).val("");
                    return
                }else{
                    uploadArticlePicture(fileId);
                }
            };

            /**上传图片函数*/
            var uploadArticlePicture = function(fileId){
                $.ajaxFileUpload({
                    url: 'article/upload',
                    secureuri: false,
                    fileElementId: fileId,
                    dataType: 'json',
                    data: {accountId: $scope.accountId},
                    success: function (data){
                        if(data && 1 == data.code){
                            $scope.$apply(function(){
                                if("articleCoverImage" == fileId){
                                    $scope.article.coverImage = data.picture;
                                }
                                else {
                                    ($scope.articleItems)[$scope.num].coverImage = data.picture;
                                }
                            });
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };
            $scope.openImgModal = function(articleCover){
                var modalInstance = $modal.open({
                    windowClass: 'model-width-xlarge',
                    templateUrl: 'app/singleSend/imageModal.html',
                    controller: 'ArticleImageModalController',
                    resolve: {
                        accountId: function(){
                            return  $scope.accountId
                        }
                    }
                });
                modalInstance.result.then(function(selectedPicture){
                    if("articleCoverImage" == articleCover){
                        $scope.article.coverImage = selectedPicture;
                    }
                    else {
                        ($scope.articleItems)[$scope.num].coverImage = selectedPicture;
                    }
                });
            };
            /**删除图片*/
            $scope.removeCoverImage = function(){
                if(confirm("确认删除？")){
                    if($scope.article.coverImage){
                        $http.post('article/removeCoverImage',{coverImageId: $scope.article.coverImage.id})
                            .success(function(){
                                $scope.article.coverImage = null;
                            })
                            .error(function(){
                                alert("发生未知异常，请联系管理员!");
                            })
                    }
                }
            };
            $scope.removeItemCoverImage = function(){
                if(confirm("确认删除？")){
                    if(($scope.articleItems)[$scope.num].coverImage){
                        $http.post('article/removeCoverImage',{coverImageId: ($scope.articleItems)[$scope.num].coverImage.id})
                            .success(function(){
                                ($scope.articleItems)[$scope.num].coverImage = null;
                            })
                            .error(function(){
                                alert("发生未知异常，请联系管理员!");
                            })
                    }
                }
            };

            $scope.remove = function(){
                if(confirm("确认删除？")){
                    $http.post('article/remove',{coverImage: ($scope.articles)[$scope.num].coverImage, accountId: $rootScope.accountId})
                        .success(function(){
                            ($scope.articles)[$scope.num].coverImage = "";
                            $('.preview').css('display', 'none');
                            $('.previewMsg'+$scope.num).css('display','inline-block');
                            $('.preview_img'+$scope.num).attr("src","");
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        })
                }
            };

            $scope.submit = function(){
                /**判断是否有选择图片*/
                if(!$scope.article.coverImage){
                    alert("请上传封面图片！");
                    return
                }
                if($scope.articleItems.length < 1){
                    alert("多条图文至少需要2条消息！");
                    return
                }

                for(var i=0; i<($scope.articleItems).length; i++){
                    if(null == ($scope.articleItems)[i].coverImage){
                        $scope.showArticleItemForm(i);
                        alert("请选择一张图片！");
                        return
                    }
                }

                var article = angular.copy($scope.article);
                var articleItems = angular.copy($scope.articleItems);
                $http.post('article/updateMulArticle',{accountId: $rootScope.accountId, article: $scope.article, articleItems: articleItems})
                    .success(function(){
                        $state.transitionTo('material.article.card', {accountId: $rootScope.accountId});
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
    module.controller('ArticleImageModalController', ['$scope', '$state', '$http','$modalInstance', 'Picture', 'accountId','PictureGroup',
        function($scope, $state, $http, $modalInstance, Picture, accountId, PictureGroup){
            $scope.refreshPictureGroup = function(){
                PictureGroup.query({accountId: $scope.accountId, permanent: true},function(datas){
                    $scope.pictureGroups = datas;
                });
                /**未分组的图片个数*/
                $http.get('picture/findAllNotInGroup', { params: { accountId: $scope.accountId, permanent: true}})
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
                        accountId: accountId, pictureGroupId: groupId, permanent: true},
                    function (datas, headers) {
                        $scope.pictures = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };

            $scope.$watch('picturePage.currentPage', function(){
                $scope.refresh();
            });

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
})(angular);
