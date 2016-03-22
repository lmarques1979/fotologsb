app.controller('messageController', function ($scope, $http, $timeout, messageService) {
	
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
	
	$scope.searchInactive = function() {
		  
		messageService.searchInactive()
	      .then(
		           function(response) {
		        	   $scope.message=response.data.messages;
		        	},
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
		 
       
	}
	
	$scope.saveActive = function() {
		  
			var messages=$scope.message;
		    
			angular.forEach(messages, function(message,key) {
				
				messageService.saveActive(message)
			      .then(
				           function(response) {
				        	   $scope.comment=response.data.message;
				        	   $timeout(function () { $scope.comment=""; }, 2500);
				        	   if ($scope.message[key].active==true){
				        		   $scope.message.splice(key,1);
				        	   }
				        	},
				            function(errResponse){
				        	   $scope.error = errResponse.statusText;
				        	}
			           )
			});
			
	}
});