app.controller('imageController', function ($scope, $http, $timeout, Upload) {

	$scope.image= {active: true, fullpermited: true};
	
	$scope.uploadFiles = function(files, errFiles) {
		  
		  	var url = rootUrl + '/image/upload';
		    var image=Upload.json($scope.image);
		    var album_id=$scope.album_id;
		    $scope.files = files;
	        $scope.errFiles = errFiles;
	        
	        angular.forEach(files, function(file) {
	        	
	            file.upload = Upload.upload({
	            			url:  url,
	            			data: {file: file,
	            				   jsonimg:image,
	            				   albumid:album_id}
	            });

	            file.upload.then(function (response) {
	                $timeout(function () {
	                    file.result = response.data.message; 
	                });
	            }, function (response) {
	                if (response.status > 0)
	                    $scope.errorMsg = response.status + ': ' + response.data;
	            }, function (evt) {
	                file.progress = Math.min(100, parseInt(100.0 * 
	                                         evt.loaded / evt.total));
	            });
	        });
	}
});