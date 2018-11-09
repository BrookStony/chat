(function (angular) {
    "use strict";
    var module = angular.module('chat.material');
    module.controller('MaterialAudioController',['$scope','Audio',
        function($scope,Audio){
            $scope.currentPage = 1;
            $scope.max = 10;
            $scope.refresh = function(){
                Audio.query({offset: ($scope.currentPage - 1) * $scope.max, max: $scope.max},
                    function (datas, headers) {
                        if(datas.length>0){
                            $scope.hasAudio = true;
                        }else{
                            $scope.hasAudio = false;
                        }
                        $scope.audios = datas;
                        $scope.totalItems = parseInt(headers('totalItems'));
                    });
            };
            $scope.$watch('currentPage', function () {
                $scope.refresh();
            }, true);

            $scope.ajaxFileUpload = function() {
                $.ajaxFileUpload({
                    url: 'audio/upload',
                    secureuri: false,
                    fileElementId: 'importFile',
                    dataType: 'json',
                    data: {},
                    success: function (data, status){
                        $scope.refresh();
                    },
                    error: function (){
                        alert("发生未知异常，请联系管理员!");
                    }
                });
                return false;
            };

            $scope.down = function(path,fileName,id){
                if(path){
                    window.open("audio/download?path=" + path +"&fileName="+fileName+"&id="+id);
                }
            };

            $scope.remove = function(){
                $scope.remove = function(path,fileName,id){
                    if(confirm("确认删除!")){
                        $http.post("picture/delete",{path: path, id: id})
                            .success(function(data, status){
                                alert("success");
                                $scope.refresh();
                            })
                            .error(function(){
                                alert("删除失败!");
                            });
                    }
                };
            }

        }
    ]);
})(angular);