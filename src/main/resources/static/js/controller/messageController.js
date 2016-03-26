app.controller('messageController', function ($scope, $http, $timeout, messageService, imageService) {
	
	var images = $scope.images = [];
	
	$scope.deleteMessage = function (messageId, index) {
	  	
		messageService.deleteMessage(messageId) 
	      .then(
		           function(response) {
		        	   $scope.message.splice(index,1);
		        	   $scope.comment=response.data.message;
                       $timeout(function () { $scope.comment=""; }, 2500);
		           },
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
	}
	
	$scope.searchComments = function() {
		  
		imageService.searchComments() 
	      .then(
		           function(response) {
		        	   $scope.images=response.data.images;
		        	},
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
		 
       
	}
	
	$scope.saveComments = function() {
		  
			var messages=$scope.messages;
		    
			angular.forEach(messages, function(message,key) {
				
				messageService.saveComments(message)
			      .then(
				           function(response) {
				        	   $scope.comment=response.data.message;
				        	   $timeout(function () { $scope.comment=""; }, 2500);
				        	   /*if ($scope.message[key].active==true){
				        		   $scope.message.splice(key,1);
				        	   }*/
				        	},
				            function(errResponse){
				        	   $scope.error = errResponse.statusText;
				        	}
			           )
			});
			
	}
});