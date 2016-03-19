'use strict';
 
app.factory('messageService', function($http, $q){
 
	var svc = {
			sendMessage:sendMessage
	};
	
	function sendMessage(imageId) {
			
			var url = rootUrl + '/message/send/' + imageId;
			
			return $http({
	      		method:'POST',
	            url:url ,
	            params: {jsonimg:message,
	            	     imageidid:imageId}
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