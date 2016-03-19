'use strict';
 
app.factory('imageService', function($http, $q){
 
	var svc = {
			searchImagesAlbum:searchImagesAlbum,
			editImage:editImage,
			deleteImage:deleteImage,
			updateImage:updateImage
	};
	
	function searchImagesAlbum(album_id) {
		
		var url = rootUrl + '/image/searchbyalbum/' + album_id;
		
		return $http({
      		method:'GET',
              url:url 
      		})
              .then(
                      function(response){
                      	return response;
                      }, 
                      function(errResponse){
                          console.error(errResponse);
                          return $q.reject(errResponse);
                      }
              );
    }
	
	function editImage(imageId) {
			
			var url = rootUrl + '/image/show/' + imageId;
			
			return $http({
	      		method:'GET',
	              url:url 
	      		})
	              .then(
	                      function(response){
	                         return response;
	                      }, 
	                      function(errResponse){
	                          console.error(errResponse);
	                          return $q.reject(errResponse);
	                      }
	        );
	}
	
	function updateImage(image, albumid) {
		
		var url = rootUrl + '/image/image';
		
		return $http({
      		method:'POST',
            url:url ,
            params: {jsonimg:image,
            	   albumid:albumid}
      		})
              .then(
                      function(response){
                         return response;
                      }, 
                      function(errResponse){
                          console.error(errResponse);
                          return $q.reject(errResponse);
                      }
        );
	}
	
	function deleteImage(imageId) {
		
		var url = rootUrl + '/image/delete/' + imageId;
		
		return $http({
      		method:'GET',
              url:url 
      		})
              .then(
                      function(response){
                        return response;
                      }, 
                      function(errResponse){
                          console.error(errResponse);
                          return $q.reject(errResponse);
                      }
           );
	}
	
	return svc;
 
});