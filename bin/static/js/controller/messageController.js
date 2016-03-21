app.controller('messageController', function ($scope, $http, $timeout, messageService) {

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
		  
			var message=angular.toJson($scope.message);
		
		    messageService.saveActive(message)
		      .then(
			           function(response) {
			        	   $scope.comment=response.data.message;
			        	},
			            function(errResponse){
			        	   $scope.error = errResponse.statusText;
			        	}
		           )
			 
	       
	}
});