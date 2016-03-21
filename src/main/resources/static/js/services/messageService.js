'use strict';
 
app.factory('messageService', function($http, $q){
 
	var svc = {
			sendMessage:sendMessage,
			saveActive:saveActive,
			searchInactive:searchInactive
	};
	
	function searchInactive() {
		
		var url = rootUrl + '/message/searchinactive';
		
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

	function saveActive(message) {
		
		var url = rootUrl + '/message/saveactive';
		
		return $http({
      		method:'POST',
            url:url,
            params: {jsonmsg:message}
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
	
	function sendMessage(message,imageId) {
			
			var url = rootUrl + '/message/message';
			
			return $http({
	      		method:'POST',
	            url:url ,
	            params: {jsonimg:message,
	            	     imageid:imageId}
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