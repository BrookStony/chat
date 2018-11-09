(function (angular) {
    "use strict";
    var module = angular.module('chat.massSend');
    module.controller('MassSendListController', ['$scope', '$rootScope', '$http', '$state', '$stateParams', 'MassMessage',
        function ($scope, $rootScope, $http, $state, $stateParams, MassMessage) {
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);
            $scope.sort = {
                sort: 'msgTime',
                order: 'desc'
            };
            var sort = store.get(MASS_MESSAGE_KEY);
            if (sort) {
                $scope.sort = sort;
            }
            $scope.$watch("sort", function () {
                store.set(MASS_MESSAGE_KEY, $scope.sort);
            }, true);
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function () {
                MassMessage.query({sort: $scope.sort.sort,
                        order: $scope.sort.order,
                        accountId: $scope.accountId,
                        name: $scope.filter || '',
                        offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max}, function (datas, headers) {
                            $scope.datas = datas;
                            $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage+sort.sort+sort.order', $scope.refresh);
            $scope.remove = function (item) {

            };
        }
    ]);
    module.controller('MassSendIndexController',['$scope','$http', '$rootScope', '$stateParams','$state','MemberGroup','Location','$modal',
        function($scope,$http,$rootScope,$stateParams,$state,MemberGroup,Location,$modal){
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.massSendTargets = [
                {id: 0, label: "全部用户"},
                {id: 1, label: "按分组选择"}
            ];
            $scope.sendTarget = $scope.massSendTargets[0];
            $scope.massSendSexTypes = [
                { id: 0, label: "全部"},
                { id: 1, label: "男"},
                { id: 2, label: "女"}
            ];
            $scope.sendSexType = $scope.massSendSexTypes[0];
            $http.get("memberGroup/search", {params: {accountId: $scope.accountId}}).success(function(datas){
                $scope.memberGroups = datas;
                if(datas) {
                    $scope.sendGroup = datas[0];
                }
            });

            //选择发送区域
            $scope.loc = {country: null, province: null, city: null};
            $scope.countries = [];
            $scope.provinces = null;
            $scope.cities = null;
            $http.get("location/search", {params: {type: "COUNTRY"}}).success(function(datas){
                $scope.countries = datas;
            });
            $http.get("chatMassMessage/searchSentCount", {params: {accountId: $scope.accountId}}).success(function(data){
                $scope.sentCount = data.count;
                $scope.remainCount = data.remainCount;
                $scope.accountType = data.accountType;
            });
            // 更换国家的时候清空area
            $scope.$watch('loc.country',function(){
                if($scope.loc.country) {
                    $http.get("location/search", {params: {type: "PROVINCE", parentId: $scope.loc.country.id}}).success(function(datas){
                        $scope.provinces = datas;
                    });
                }
                else {
                    $scope.provinces = null;
                    $scope.cities = null;
                    $scope.loc.province = null;
                    $scope.loc.city = null;
                }
            });
            $scope.$watch('loc.province',function(){
                if($scope.loc.province) {
                    $http.get("location/search", {params: {type: "CITY", parentId: $scope.loc.province.id}}).success(function(datas){
                        $scope.cities = datas;
                    });
                }
                else {
                    $scope.cities = null;
                    $scope.loc.city = null;
                }
            });

            //发送消息内容
            var selectedSendMessage = $scope.sendText;
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
                    templateUrl: 'app/template/imageModal.html',
                    controller: 'MassSendImageModalController',
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
                    templateUrl: 'app/template/articleModal.html',
                    controller: 'MassSendArticleModalController',
                    resolve: {accountId: function() { return $scope.accountId}}
                });
                modalInstance.result.then(function(selectedArticle){
                    $scope.messageType = "NEWS";
                    $scope.sendArticleId = selectedArticle.id;
                    $scope.article = selectedArticle;
                });
            };

            $scope.submit = function () {
                if(null != $scope.accountType && null != $scope.sentCount) {
                    if("SERVICE" == $scope.accountType) {
                        if($scope.sentCount >= 4) {
                            alert("发送失败，服务号一个月能群发4条消息，您本月已群发" + $scope.sentCount + "条消息！");
                            return false;
                        }
                    }
                    else {
                        if($scope.sentCount >= 1) {
                            alert("发送失败，订阅号一天能群发1条消息，您已群发" + $scope.sentCount + "条消息！");
                            return false;
                        }
                    }
                }
                var params = {accountId: $scope.accountId, sendTarget: $scope.sendTarget.id, sendSexType: $scope.sendSexType.id};
                if(0 != $scope.sendTarget.id && $scope.sendGroup) {
                    params.sendGroup = $scope.sendGroup.groupId;
                }
                if($scope.loc.city) {
                    params.sendLocation = $scope.loc.city.id;
                    params.locationName = $scope.loc.city.name;
                    params.locationType = "city";
                }
                else if($scope.loc.province) {
                    params.sendLocation = $scope.loc.province.id;
                    params.locationName = $scope.loc.province.name;
                    params.locationType = "province";
                }
                else if($scope.loc.country) {
                    params.sendLocation = $scope.loc.country.id;
                    params.locationName = $scope.loc.country.name;
                    params.locationType = "country";
                }
                if("TEXT" == $scope.messageType) {
                    params.messageType = "TEXT";
                    params.content = $scope.sendText;
                    params.materialId = null;
                    params.articleId = null;
                }
                else if("NEWS" == $scope.messageType) {
                    params.messageType = "NEWS";
                    params.content = null;
                    params.materialId = null;
                    params.articleId = $scope.sendArticleId;
                }
                else {
                    params.messageType = $scope.messageType;
                    params.content = null;
                    params.materialId = $scope.sendMaterialId;
                    params.articleId = null;
                }
                $http.get("chatMassMessage/massSend",{params: params})
                    .success(function(){
                        $state.transitionTo('massSend.list');
                    })
                    .error(function(){
                        alert("发送失败!");
                    });
            }
        }
    ]);
    module.controller('MassSendImageModalController', ['$scope', '$state', '$http', '$modalInstance', 'Picture','PictureGroup','accountId',
        function($scope, $state, $http, $modalInstance, Picture, PictureGroup, accountId){
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
    module.controller('MassSendArticleModalController', ['$scope', '$state', '$modalInstance', 'Article', 'accountId',
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
