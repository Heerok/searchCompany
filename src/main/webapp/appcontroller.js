var app = angular.module('app', []);
app.directive('fileModel', ['$parse', function ($parse) {
   return {
       restrict: 'A',
       link: function(scope, element, attrs) {
           var model = $parse(attrs.fileModel);
           var modelSetter = model.assign;

           element.bind('change', function(){
               scope.$apply(function(){
                   modelSetter(scope, element[0].files[0]);
               });
           });
       }
   };
}]);
app.service('fileUpload', ['$http', function ($http) {
    var service = {};
    service.uploadFileToUrl = function(file,uploadUrl,dryRun,sync){
        var fd = new window.FormData();
        fd.append('file', file);
        fd.append('dryrun',dryRun);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function(data){
                sync(data)
            })
            .error(function(data){
                sync(data)
                alert("Missing Input");
            });
    };
    return service;
}]);

app.controller("AppController", ['$rootScope', '$scope', '$http','fileUpload',
function ($rootScope, $scope, $http,fileUpload) {

    $scope.init = function(){

        $http.get("/api/pricing/listData").then(function(res){
            $scope.list = res.data;
            $scope.TotalRecordCount = res.data.length;
        });

    }

    $scope.modal = {};
    $scope.modal.enable = true;
    $scope.modal.dryRun = false;
    $scope.modal.result = null;
    $scope.modal.BatchResults = [];

    $scope.uploadWork = function(){
        $scope.modal.enable = false;
        $scope.modal.result = null;
        var file = $scope.modal.myFile;
        var uploadUrl = '/upload'
        fileUpload.uploadFileToUrl(file,uploadUrl,$scope.modal.dryRun,function(sync){
            if(sync!=null){
                if($scope.modal.dryRun){
                    $scope.modal.showBatch = false;
                }else{
                    $scope.modal.showBatch = true;
                }
                $scope.modal.result = sync;
                if(sync!=null)
                    $scope.modal.BatchResults = sync.results;
            }
            $scope.modal.enable = true;
        });
    };

    $scope.aa="asdasda";

}]);

