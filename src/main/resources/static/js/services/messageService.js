'use strict';
 
app.factory('messageService', function($http, $q){
 
	var svc = {
			messagesImage:messagesImage,
			saveComments:saveComments,
			sendMessage:sendMessage,
			deleteMessage:deleteMessage
	};
	
	

	function messagesImage(imageId) {
		
		var url = rootUrl + '/message/messagesimage/' + imageId;
		
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


	function saveComments(message) {
		
		var url = rootUrl + '/message/savecomments';
		var messagejson=angular.toJson(message);
		
		return $http({
      		method:'POST',
            url:url,
            params: {jsonmsg:messagejson}
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
	
	function deleteMessage(messageId) {
		
		var url = rootUrl + '/message/delete/' + messageId;
		 
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