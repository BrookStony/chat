(function (angular) {
    "use strict";
    var module = angular.module('chat.material');
    module.controller('MaterialVideoController',['$rootScope','$scope','Video','$http','$state','$stateParams','$sce',
        function($rootScope,$scope,Video,$http,$state,$stateParams,$sce){
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function(){
                Video.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function (datas, headers) {
                        if(datas.length>0){
                            $scope.hasVideo = true;
                        }else{
                            $scope.hasVideo = false;
                        }
                        $scope.videos = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage', function () {
                $scope.refresh();
            }, true);

            $scope.transToCreateHtml = function(){
                $state.transitionTo('material.video.create');
            };

            $scope.down = function(video){
                if(video){
                    var accountId = $rootScope.accountId
                    window.open("video/download?path=" + video.path +"&fileName="+video.name+"&id="+video.id+"&accountId="+accountId);
                }
            };

            $scope.remove = function(video){
                if(confirm("确认删除!")){
                    $http.post("video/remove",{path: video.path, id: video.id, accountId: $rootScope.accountId})
                        .success(function(){
                            $scope.refresh();
                        })
                        .error(function(){
                            alert("发生未知异常，请联系管理员!");
                        });
                }
            };
        }
    ]);
    module.controller('MaterialVideoEditController',['$rootScope','$scope','$state','$http','$stateParams',
        function($rootScope,$scope,$state,$http,$stateParams){
            $http.post('video/edit',{id: $stateParams.id})
                .success(function(data){
                    $scope.video = data;
                    $('#videoPreview').attr("src",data.path);
                    $('#showVideo').css('display','block');
                });

            $scope.uploadVideo = function(){
                $.ajaxFileUpload({
                    url: 'video/upload',
                    secureuri: false,
                    fileElementId: 'video',
                    dataType: 'json',
                    data: {accountId: $rootScope.accountId},
                    success: function (data, status){
                        $scope.video = data;
                        $('#videoPreview').attr("src",data.path);
                        $('#showVideo').css('display','block');
                    },
                    error: function (){
                        alert("上传失败!");
                    }
                });
            };

            $scope.remove = function(path){
                $http.post('video/remove',{path: path, accountId: $rootScope.accountId })
                    .success(function(){
                        $('#videoPreview').attr("src","");
                        $('#showVideo').css('display','none');
                    })
                    .error(function(){
                        alert("上传失败！");
                    });
            };

            $scope.submit = function(){
                if(!($scope.video).path){
                    alert("请上传视频");
                    return
                }
                $http.post("video/updateVideo",{video: $scope.video,id: $stateParams.id})
                    .success(function(){
                        $state.transitionTo("material.video.list");
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
    module.controller('MaterialVideoCreateController',['$rootScope','$scope','$state','Video','$stateParams','$http',
        function($rootScope,$scope,$state,Video,$stateParams,$http){

//            $scope.getURL = function(){
//                console.log($scope.url);
//                $('#weishiPreview').attr("href",$scope.url);
//                $('#weishi').css('display','block');
//            };

            $scope.file_changed = function(){
                var fileSize = 0;
                var obj = document.getElementById("video")
                fileSize = obj.files[0].size
                if(fileSize > 20*1024*1024){
                    alert("上传的视频超过20M!")
                    $('#video').val("")
                }else{
                    uploadVideo();
                }
            };

            var uploadVideo = function(){
                $.ajaxFileUpload({
                    url: 'video/upload',
                    secureuri: false,
                    fileElementId: 'video',
                    dataType: 'json',
                    data: {accountId: $rootScope.accountId},
                    success: function (data, status){
                        $scope.path = data.path ;
                        $scope.video = data;
                        $('#videoPreview').attr("src",data.path);
                        $('#showVideo').css('display','block');
                    },
                    error: function (){
                        alert("上传失败!");
                    }
                });
            };

            $scope.remove = function(path){
                $http.post('video/remove',{path: path, accountId: $rootScope.accountId })
                    .success(function(){
                        $('#videoPreview').attr("src","");
                        $('#showVideo').css('display','none');
                    })
                    .error(function(){
                        alert("上传失败！");
                    });
            };

            $scope.submit = function(){
                if(!$scope.path){
                    alert("请上传视频");
                    return
                }
                $http.post("video/saveVideo",{video: $scope.video})
                    .success(function(){
                        $state.transitionTo("material.video.list");
                    })
                    .error(function(){
                        alert("发生未知异常，请联系管理员!");
                    });
            };
        }
    ]);
})(angular);