(function(angular){
    "use strict";
    var module = angular.module("chat.singleSend");
    module.controller("SingleSendIndexController",['$scope', '$http', '$state', '$stateParams', '$modal', '$timeout', 'Member',
        function($scope, $http, $state, $stateParams, $modal, $timeout, Member){
            $scope.userId = $stateParams.userId;
            $scope.userName = $stateParams.userName;
            $scope.messageOk = false;

            $scope.quick_reply = false;
            $scope.currentMessagePage = 1;
            $scope.max = 10;

            Member.get({id: $scope.userId}, function (data) {
                $scope.member = data;
                $scope.accountId = data.account.id;
            });

            $scope.refresh = function(){
                $http.get("chatMessage/findUserChatRecords",
                    { params: {
                        offset: ($scope.currentMessagePage - 1) * $scope.max, max: $scope.max,
                        userId : $scope.userId}
                    })
                    .success(function(datas){
                        $scope.messages = datas.messages;
                        $scope.totalItems = parseInt(datas.totalCount);
                    })
            };
            $scope.$watch('currentMessagePage',function(){
                $scope.refresh();
            });

            var msgType = "TEXT" ;
            var sendContent = null;
            $scope.sendText = "";
            $scope.customerSend_text = true;
            $scope.customerSend_image = false;
            $scope.customerSend_article = false;

            $scope.showTextArea = function(){
                msgType = "TEXT" ;
                $scope.customerSend_text = true;
                $scope.customerSend_image = false;
                $scope.customerSend_article = false;
            };
            $scope.openImageModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-image-xlarge',
                    templateUrl: 'app/singleSend/imageModal.html',
                    controller: 'SingleSendImageModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedPicture){
                    msgType = "IMAGE";
                    sendContent = selectedPicture.id;
                    $scope.imageUrl = selectedPicture.url;
                    $scope.customerSend_image = true;
                    $scope.customerSend_text = false;
                    $scope.customerSend_article = false;
                });
            };
            $scope.openArticleModal = function(){
                var modalInstance = $modal.open({
                    windowClass: 'model-article-xlarge',
                    templateUrl: 'app/singleSend/articleModal.html',
                    controller: 'SingleSendArticleModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedArticle){
                    msgType = "NEWS";
                    sendContent = selectedArticle.id;
                    $scope.article = selectedArticle;
                    $scope.customerSend_article = true;
                    $scope.customerSend_text = false;
                    $scope.customerSend_image = false;
                });
            };

            var hideMessage = function(){
                $timeout(function(){
                    $scope.messageOk = false;
                },2000)
            };

            $scope.customSend = function(){
                if(!$scope.accountId){
                    alert("未选择微信公众号");
                    return false;
                }
                if(msgType == "TEXT"){
                    sendContent = $scope.sendText;
                }
                if(!sendContent){
                    $scope.message = "请先选择要发送的内容";
                    $scope.messageOk = true;
                    hideMessage();
                    return false;
                }

                $http.post('chatCustomMessage/customSend', {msgType: msgType,sendContent: sendContent,
                    accountId: $scope.accountId,toUserId: $scope.userId})
                    .success(function(data){
                        if(data.errorCode==0){
                            location.reload()
                        }
                        else{
                            $scope.message = data.message;
                            $scope.messageOk = true;
                            hideMessage();
                        }
                    })
                    .error(function(){
                        alert("系统错误，请稍后重试!");
                    });
            };
            $scope.quickSendText = "";
            $scope.customSendText =  function(){
                if(!$scope.accountId){
                    alert("未选择微信公众号");
                    return false;
                }
                msgType = "TEXT";
                sendContent = $('#quick_reply_text').text();
//                sendContent = $scope.quickSendText;
                if(!sendContent){
                    alert("请出入文字!");
                    return false;
                }
                $http.get('chatCustomMessage/customSend', {params: {msgType: msgType,sendContent: sendContent,
                    accountId: $scope.accountId, toUserId: $scope.userId}})
                    .success(function(data,status){
//                        if(status==200){
//                            alert(data.msg);
//                        }
                        $('#quick_reply_text').text("");
                        $scope.refresh();
                    })
                    .error(function(){
                        alert("系统错误，请稍后重试!");
                    });
            };

            $scope.downLoad = function(path,fileName){
                if(path){
                    var accountId = $rootScope.accountId
                    if(!accountId){
                        alert("请选择微信公众号");
                        return;
                    }
                    window.open("picture/download?path=" + path +"&fileName="+fileName+"&accountId="+accountId);
                }
            };
        }
    ]);

    module.controller('SingleSendImageModalController', ['$scope', '$state','$http', '$modalInstance', 'Picture', 'accountId', 'PictureGroup',
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
    module.controller('SingleSendArticleModalController', ['$scope', '$state', '$modalInstance', 'Article', 'accountId',
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
