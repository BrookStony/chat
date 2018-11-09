(function (angular) {
    "use strict";
    var module = angular.module('chat.material');

    module.controller('MaterialArticleController',['$rootScope','$scope','$state','$http','Article',
        function($rootScope,$scope,$state,$http,Article){
            $scope.currentPage = 1;
            $scope.max = 10;

            $scope.articles = [] ;
            $scope.addArticle = function(){
                Article.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function(datas, headers){
                        if(datas.length>0){
                            _.each(datas,function(v){
                                $scope.articles.push(v);
                            });
                            $scope.totalItems =  $scope.articles.length;
                        }
                        if($scope.totalItems){
                            $scope.hasArticle = true;
                        }else{
                            $scope.hasArticle = false;
                        }
                    }
                );
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
                   ++$scope.currentPage
                   $scope.addArticle();
               }
            });
            $scope.transToArticleCreateHtml = function(){
                $state.transitionTo('material.article.create');
            };
            $scope.transToMulArticleCreateHtml = function(){
                $state.transitionTo('material.article.mulCreate');
            };
            $scope.delete =function(id,coverImage,index){
                if(confirm("确认删除?")){
                    $http.post("article/remove",{ id: id, coverImage: coverImage, accountId: $rootScope.accountId })
                        .success(function(){
                            $scope.articles.splice(index,1);
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        });
                }
            };
            $scope.deleteMulArticle = function(id,index){
                if(confirm("确认删除?")){
                    $http.post('article/removeMulArticle',{id: id, accountId: $rootScope.accountId})
                        .success(function(){
                            $scope.articles.splice(index,1);
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        })
                }
            };
        }
    ]);
    module.controller('MaterialMulArticleCreateController',['$rootScope','$scope','$state','$http','Article',
        function($rootScope,$scope,$state,$http,Article){
            $scope.num = 0;
            $scope.articles = [
                { title: "", author: "", content: "",coverImage:"",originalUrl:""},
                { title: "", author: "", content: "",coverImage:"",originalUrl:""}
            ];
            /**主article*/
            $scope.showEdit = function(){
                $scope.edit_cover = true;
            };
            $scope.hideEdit = function(){
                $scope.edit_cover = false;
            };
            /**调整article的编辑区域*/
            $scope.showEditArea = function(index){
                $scope.num = index;
                var height = (index==0) ? 0 : (170+(index-1)*100);
                var top = height+"px";
                $('.article_edit_area').css('top',top);

                if(($scope.articles)[index].coverImage == ""){
                    $('.preview').css('display', 'none');
                }
                else{
                    $('.preview').css('display', 'block');
                }
            };

            /**articleItem的覆盖层显示 */
            $scope.showLessEdit = function(e){
                $(e).next(".edit_less_article").css("display","block");
            };
            /**articleItem的覆盖层隐藏 */
            $scope.hideLessEdit = function(e){
                $(e).css('display','none');
            };

            $scope.deleteThisLessArticle = function(index){
                if($scope.articles.length<=2){
                    alert("无法删除，多条图文至少需要2条消息");
                    return ;
                }
                $scope.articles.splice(index,1);
            };

            //添加一个次要article
            $scope.add_article = function(){
                $scope.articles.push({ title: "", author: "", content: "",coverImage: ""});
            };
            /**选择图片上传前判断图片是否超出限制*/
            $scope.file_changed = function(){
                var fileSize = 0;
                var obj = document.getElementById("coverImage");
                fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#coverImage').val("");
                    return
                }else{
                    uploadArticlePicture();
                }
            };
            /**上传图片函数*/
            var uploadArticlePicture = function(){
                $.ajaxFileUpload({
                    url: 'article/upload',
                    secureuri: false,
                    fileElementId: 'coverImage',
                    dataType: 'json',
                    data: {accountId: $rootScope.accountId},
                    success: function (data){
                        if(data){
                            ($scope.articles)[$scope.num].coverImage = data.coverImage;
                            $('.preview').css('display', 'block');
                            $('.previewMsg'+$scope.num).css('display','none');
                            $('.preview_img'+$scope.num).attr("src",data.coverImage);
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };
            /**删除图片*/
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
                for(var i=0;i<($scope.articles).length;i++){
                    if(($scope.articles)[i].coverImage == ""){
                        $scope.showEditArea(i);
                        alert("请选择一张图片！");
                        return
                    }
                };

                $http.post('article/saveMul',{articles: $scope.articles, accountId: $rootScope.accountId})
                    .success(function(){
                        $state.transitionTo('material.article.list');
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
    module.controller('MaterialArticleEditController',['$rootScope','$scope','$state','$http','Article','$stateParams',
        function($rootScope,$scope,$state,$http,Article,$stateParams){
            $http.post('article/edit',{id: $stateParams.id})
                .success(function(data){
                    $scope.article = data ;
                    $('.fengmian').css('display','none');
                    if(data.coverImage){
                        $scope.coverImage = data.coverImage;
                        $('.hasCoverImage').css('display','block');
                    }
                    if(data.description){
                        $scope.showDescription = true;
                    }
                });
            $scope.file_changed = function(){
                var fileSize = 0;
                var obj = document.getElementById("coverImage");
                fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#coverImage').val("");
                    return
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
                    data: {accountId: $rootScope.accountId},
                    success: function (data){
                        if(data){
                            $scope.coverImage = data.coverImage;
                            $('.hasCoverImage').css('display','block');
                            $('.preview').attr("src",data.coverImage);
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };

            $scope.remove = function(coverImage){
                if(confirm("确认删除？")){
                    $http.post('article/remove',{coverImage:coverImage, accountId: $rootScope.accountId})
                        .success(function(){
                            $('.hasCoverImage').css('display','none');
                            $('.preview').attr("src","");
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        });
                }
            };

            $scope.submit = function(){
                var article = angular.copy($scope.article)
                if(!$scope.coverImage){
                    alert("请选择一张图片！");
                    return
                }else{
                    article.coverImage =  $scope.coverImage;
                }

                $http.post('article/modify',{article: article, accountId: $rootScope.accountId})
                    .success(function(){
                        $state.transitionTo('material.article.list');
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
    module.controller('MaterialMulArticleEditController',['$rootScope','$scope','$state','$http','Article','$stateParams',
        function($rootScope,$scope,$state,$http,Article,$stateParams){
            $scope.article_edit_area = true;
            $scope.articleItem_edit_area = false;
            $http.post('article/mulArticleEdit',{id: $stateParams.id})
                .success(function(data){
                    $scope.article = data;
                    if(data.coverImage){
                        $scope.hasArticleCoverImage = true;
                    }
                });
            /**article可视覆盖显示和隐藏 */
            $scope.showEdit = function(){
                $scope.edit_cover = true;
            };
            $scope.hideEdit = function(){
                $scope.edit_cover = false;
            };
            /**article编辑区域 */
            $scope.showEditArea = function(){
                $scope.article_edit_area = true;
                $scope.articleItem_edit_area = false;
            };
            /**articleItem可视覆盖层的显示和隐藏*/
            $scope.showArticleItemEdit = function(e){
                $(e).next('.edit_less_article').css('display','block')
            };
            $scope.hideArticleItemEdit = function(e){
                $(e).css('display','none');
            };
            $scope.num=0;
            /**articleItem编辑区域 */
            $scope.showArticleItemEditArea = function(articleItem,index){
                $scope.num = index;
                $scope.article_edit_area = false;
                $scope.articleItem_edit_area = true;
                if(articleItem.coverImage==""){
                    $('.preview').css('display','none');
                }else{
                    $('.preview').css('display','block');
                }
                $scope.articleItem = angular.copy(articleItem);
                $scope.article.articleItem[index] = $scope.articleItem
            };
            $scope.deleteThisArticleItem = function(articleItem,index){
                if($scope.article.articleItem.length<2){
                    alert("无法删除，多条图文至少需要2条消息");
                    return ;
                }
                $scope.article_edit_area = true;
                $scope.articleItem_edit_area = false;
                $scope.article.articleItem.splice(index,1);
            };
            $scope.addArticleItem = function(){
                $scope.article.articleItem.push({ title: "", author: "", content: "",coverImage:""});
            };
            var id="coverImage";
            /**选择图片上传前判断图片是否超出限制*/
            $scope.file_changed = function(){
                if($scope.article_edit_area){
                    id="coverImage";
                }else{
                    id="articleItem_coverImage";
                }
                var fileSize = 0;
                var obj = document.getElementById(id);
                fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#('+id+')').val("");
                    return
                }else{
                    uploadArticlePicture();
                }
            };
            /**上传图片函数*/
            var uploadArticlePicture = function(){
                $.ajaxFileUpload({
                    url: 'article/upload',
                    secureuri: false,
                    fileElementId: id,
                    dataType: 'json',
                    data: {accountId: $rootScope.accountId},
                    success: function (data){
                        if(data){
                            if($scope.article_edit_area){
                                $scope.hasArticleCoverImage = true;
                                $scope.article.coverImage = data.coverImage;
                                $('.preview_article_img').attr("src",data.coverImage);
                            }else{
                                $('.preview').css('display','block');
                                $scope.article.articleItem[$scope.num].coverImage = data.coverImage;
                                $scope.articleItem.coverImage = data.coverImage;
                                $('.preview_article_img').attr("src",data.coverImage);
                                $('.preview_img'+$scope.num).attr("src",data.coverImage);
                            }
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };
            /**删除图片*/
            $scope.remove = function(coverImage){
                if(confirm("确认删除？")){
                    $http.post('article/remove',{coverImage: coverImage, accountId: $rootScope.accountId})
                        .success(function(){
                            if($scope.article_edit_area){
                                $scope.hasArticleCoverImage = false;
                                $scope.article.coverImage = "";
                            }else{
                                $('.preview').css('display','none');
                                $('.preview_img'+$scope.num).css('display','none');
                                $scope.article.articleItem[$scope.num].coverImage = "";
                            }
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        })
                }
            };
            $scope.submit = function(){
                if($scope.article.coverImage==""){
                    alert("请选择一张图片");
                    $scope.article_edit_area = true;
                    $scope.articleItem_edit_area = false;
                    return ;
                }
                for(var i=0;i<$scope.article.articleItem.length;i++){
                    if($scope.article.articleItem[i].coverImage==""){
                        alert("请选择一张图片");
                        $scope.article_edit_area = false;
                        $scope.articleItem_edit_area = true;
                        $scope.articleItem = angular.copy($scope.article.articleItem[i]);
                        break;
                    }
                }
                $http.post('article/updateArticleItem',{article: $scope.article, accountId: $rootScope.accountId})
                    .success(function(){
                        $state.transitionTo('material.article.list');
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
    module.controller('MaterialArticleCreateController',['$rootScope','$scope','$state','$http','Article',
        function($rootScope,$scope,$state,$http,Article){
            $scope.showDescription = false;
            $scope.file_changed = function(){
                var fileSize = 0;
                var obj = document.getElementById("coverImage");
                fileSize = obj.files[0].size;
                if(fileSize > 5*1024*1024){
                    alert("上传的图片超过5M!");
                    $('#coverImage').val("");
                    return
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
                    data: {accountId: $rootScope.accountId},
                    success: function (data){
                        if(data){
                            $('.fengmian').css('display','none');
                            $('.hasCoverImage').css('display','block');
                            $scope.coverImage = data.coverImage;
                            $('.preview').attr("src",data.coverImage);
                        }
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
            };
            $scope.remove = function(coverImage){
                if(confirm("确认删除？")){
                    $http.post('article/remove',{coverImage: coverImage, accountId: $rootScope.accountId})
                        .success(function(){
                            $('.hasCoverImage').css('display','none');
                            $('.preview').attr("src","");
                            $('.fengmian').css('display','inline-block');
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        })
                }
            };

            $scope.submit = function(){
                var article = angular.copy($scope.article)
                if(!$scope.coverImage){
                    alert("请选择一张图片！");
                    return
                }else{
                    article.coverImage =  $scope.coverImage;
                }
                $http.post('article/save',{article: article, accountId: $rootScope.accountId})
                    .success(function(){
                        $state.transitionTo('material.article.list');
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
})(angular);