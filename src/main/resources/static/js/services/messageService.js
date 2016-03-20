'use strict';
 
app.factory('messageService', function($http, $q){
 
	var svc = {
			sendMessage:sendMessage
	};
	
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