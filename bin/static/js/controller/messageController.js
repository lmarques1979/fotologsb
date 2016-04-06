app.controller('messageController', function ($scope, $http, $filter, $timeout, messageService, imageService) {
	
	var message = $scope.message = [];
	
	$scope.sort = function(keyname){
        $scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
    }
	
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
		        	   
		        	   var total  	= response.data.images.length;
		        	   var images 	= response.data.images;
		        	   var urlimage = response.data.urlimage;
		        	   for (var i = 0, l = total; i < l; i++) {
               			   var image=images[i];
               			   var totalmsg=images[i].messages.length;
               			  
			        		   for (var k = 0, m = totalmsg; k < m; k++) {
			        			   message.push({
			        				   	  id: images[i].messages[k].id,
			        				 message: images[i].messages[k].message,
			        				fromuser: images[i].messages[k].fromuser,
			        			   fromemail: images[i].messages[k].fromemail,
			        			 datemessage: images[i].messages[k].datemessage,
			        			 	  active: images[i].messages[k].active,
			        				   image: image
			        				    });
			        		   }
		        	   }
		        	   $scope.message=message;
		        	   $scope.urlimage=urlimage;
		        	},
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
		 
       
	}
	
	$scope.saveComments = function() {
		  
			var messages=$scope.message;
		    
			angular.forEach(messages, function(message,key) {
				
				messageService.saveComments(message)
			      .then(
				           function(response) {
				        	   $scope.comment=response.data.message;
				        	   $timeout(function () { $scope.comment=""; }, 2500);
				        	   
				        	},
				            function(errResponse){
				        	   $scope.error = errResponse.statusText;
				        	}
			           )
			});
			
	}
});