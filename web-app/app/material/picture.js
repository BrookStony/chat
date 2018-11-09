(function (angular) {
    "use strict";
    var module = angular.module('chat.material');
    module.controller('MaterialPictureController',['$rootScope', '$scope', '$state', '$stateParams', 'Picture', '$http','PictureGroup','Account',
        function($rootScope, $scope, $state, $stateParams, Picture, $http, PictureGroup, Account){
            $scope.accountId = $stateParams.accountId || store.get(CURRENT_ACCOUNT_KEY);
            if(!$scope.accountId){
                alert("未选择微信公众号");
            }
            $scope.$watch('$root.accountId', $scope.refresh);

            $scope.checked = false;
            $scope.someSelected = true;

            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function(groupId){
                Picture.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max, accountId: $scope.accountId, pictureGroupId: groupId},
                    function (datas, headers) {
                        if(datas.length>0){
                            $scope.hasPicture = true;
                        }else{
                            $scope.hasPicture = false;
                        }
                        $scope.pictures = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage', function () {
                $scope.refresh();
            }, true);

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

            $scope.ajaxFileUpload = function() {
                if(!$rootScope.accountId){
                    alert("请选择微信公众号");
                    return;
                }
                var file = angular.element("#uploadFile").val();
                if(!file){
                    alert("请选择图片！");
                    return false;
                }
                $.ajaxFileUpload({
                    url: 'picture/upload',
                    secureuri: false,
                    fileElementId: 'uploadFile',
                    dataType: 'json',
                    data: {accountId: $rootScope.accountId},
                    success: function (data, status){
                        location.reload();
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
                return false;
            };
            /**全选**/
            $scope.toggleCheckedAll = function () {
                $scope.checked = !$scope.checked;
                angular.forEach($scope.pictures, function (value) {
                    value.checked = $scope.checked;
                });
            };
            $scope.$watch('pictures',function(pictures){
                $scope.closeDisabled = _.all(pictures, function (picture) {
                    return picture.checked == false || picture.checked == undefined;
                });
                $scope.checked = pictures && pictures.length > 0 && _.all(pictures, function(picture){
                    return picture.checked == true;
                });
                $scope.someSelected = $scope.closeDisabled || !_.any(pictures, function(picture){
                    return picture.checked == true;
                });
            }, true);

            $scope.groupType = {id: 0, name: "未分组"};
            $scope.selectGroupType ={};
            $scope.onGroupClick = function(group){
                $scope.selectGroupType = group;
            };
            /**修改图片名称**/
            $scope.editPictureName = function(picture){
                $http.post("picture/updateName",{id: picture.id, name: picture.name})
                    .success(function(){
                        $scope.refresh();
                    })
            };
            /**批次或单个图片移动分组**/
            $scope.moveToGroup = function(picture){
                var ids = [];
                if(picture){
                    ids.push(picture.id);
                }else {
                    ids = checkedIds($scope.pictures);
                }
                $http.post("picture/moveToGroup", {ids: ids, groupId: $scope.selectGroupType.id})
                    .success(function(){
                        $scope.refresh();
                        $scope.refreshPictureGroup();
                    })
                    .error(function(){
                        alert("分组失败!");
                    });
            };
            /**分组列表的样式*/
            $scope.addClass = function(event){
                angular.element(event).addClass("current_group");
                angular.element(event).siblings().removeClass("current_group");
            };

            Account.get({id: $scope.accountId}, function(data){
                $scope.currentAccount = data;
            });
            $scope.pictureGroup = { name: "", account: []} ;
            /**新建分组**/
            $scope.createGroup = function(){
                if(!$scope.accountId){
                    alert("未选择微信公众号");
                    return false;
                }
                var pictureGroup = angular.copy($scope.pictureGroup);
                pictureGroup.account = $scope.currentAccount;
                PictureGroup.save(pictureGroup, function(){
                    $scope.pictureGroup.name = "";
                    $scope.pictureGroup.account = [];
                    $scope.refreshPictureGroup();
                }, function () {
                    alert("发生未知异常，请联系管理员!");
                });
            };
            /**修改分组名称**/
            $scope.editGroupName = function(group){
                PictureGroup.update(group, function(){
                    $scope.refreshPictureGroup();
                }, function () {
                    alert("发生未知异常，请联系管理员!");
                });
            };
            /**删除分组**/
            $scope.deleteGroup = function(group){
                $http.post("pictureGroup/remove",{id: group.id, accountId: $scope.accountId})
                    .success(function(){
                        $scope.refresh();
                        $scope.refreshPictureGroup();
                    })
            };

            $scope.download = function(id, fileName){
                if(id && fileName){
                    window.open("picture/download?id=" + id +"&fileName="+fileName);
                }
            };
            /**删除图片**/
            $scope.removePicture = function(picture){
                if(picture.article){
                    alert("图片在图文被使用，不能删除！");
                    return false;
                }
                $http.post("picture/remove?id=" + picture.id)
                    .success(function(data, status){
                        $scope.refresh();
                        $scope.refreshPictureGroup();
                    })
                    .error(function(){
                        alert("删除失败!");
                    });
            };
            /**批量删除选中图片**/
            $scope.removePictures = function(id){
                var ids = checkedIds($scope.pictures);
                $http.post("picture/removeAll", {ids: ids})
                    .success(function(data, status){
                        $scope.refresh();
                        $scope.refreshPictureGroup();
                    })
                    .error(function(){
                        alert("删除失败!");
                    });
            };
        }
    ]);
    function checkedIds(pictures){
        return _.chain(pictures).filter(function(picture){
            return picture.checked == true;
        }).map(function(p){
                return p.id;
        }).value();
    }

})(angular);