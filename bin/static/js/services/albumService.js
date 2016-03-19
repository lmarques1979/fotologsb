'use strict';
 
app.factory('albumService', function($http, $q){
 
	var svc = {
			getAlbuns: getAlbuns
			};
	
	function getAlbuns() {
		
		var url = rootUrl + '/album/searchalbuns';
		
		return $http({
    		method:'POST',
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