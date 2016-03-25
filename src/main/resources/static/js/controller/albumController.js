app.controller('albumController', function ($scope, $http,  $uibModal, $log,  $timeout, albumService, imageService, messageService) {

	vm=this;
	$scope.myInterval=0;
	$scope.noWrapSlides=false;
	$scope.active=0;
	var slides = $scope.slides = [];
	var currIndex = 0;
	$scope.total=0;
	//reset Form
	$scope.image = {};
	$scope.animationsEnabled = true;
		
	$scope.deleteMessageCarousel = function (messageId, parentindex, index) {
	  	
		messageService.deleteMessage(messageId) 
	      .then(
		           function(response) {
		        	   $scope.slides[parentindex].messages.splice(index,1);
		        	   $scope.commentuser=response.data.message;
                       $timeout(function () { $scope.commentuser=""; }, 2500);
		           },
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
	}
	
	
	$scope.openModalMessage = function (size, imageId) {

	    var modalInstance = $uibModal.open({
	      animation: $scope.animationsEnabled,
	      templateUrl: 'ModalMessageContent.html',
	      controller: 'ModalMessageCtrl',
	      size: size,
	      resolve: {
              param: function () {
                  return {'imageid':imageId};
              }
          }
	    });

	};
	
	$scope.openModalImage = function (size, imageId, index) {

	    $scope.code = null;
	    $scope.response = null; 
	   
	    imageService.editImage(imageId)
	   .then(
		           function(response) {
		        	   $scope.image = response.data.image;
		        	   $scope.urlimage = response.data.urlimage;
		        	},
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	    );
	    
	    var modalInstance = $uibModal.open({
	      animation: $scope.animationsEnabled,
	      templateUrl: 'ModalImageContent.html',
	      controller: 'ModalImageCtrl',
	      size: size,
	      scope: $scope,
	      resolve: {
              param: function () {
                  return {'index':index};
              }
          }
	    });

	   
	};
	
	$scope.addSlide = function(image) {
	    var newWidth = 600 + slides.length + 1;
	    var urlimage='https://fotologlmdcm.s3.amazonaws.com/';
	    var photo=urlimage+image.name;
	    
	    /*var messages;
	    
	    messageService.messagesImage(image.id)
		   .then(
			           function(response) {
			        	   messages = response.data.message;
			        	},
			            function(errResponse){
			        	   $scope.error = errResponse.statusText;
			        	}
		);*/
	    
	    slides.push({
	      image: photo,
	      text: image.description,
	      id: currIndex++,
	      idimage:image.id,
	      active:image.active,
	      messages:image.messages
	    });
	    $scope.total=currIndex;
	};
	 
	$scope.randomize = function() {
	    var indexes = generateIndexesArray();
	    assignNewIndexesToSlides(indexes);
	};
	
	//Randomize logic below
	function assignNewIndexesToSlides(indexes) {
	    for (var i = 0, l = slides.length; i < l; i++) {
	      slides[i].id = indexes.pop();
	    }
	}
	
	function generateIndexesArray() {
	    var indexes = [];
	    for (var i = 0; i < currIndex; ++i) {
	      indexes[i] = i;
	    }
	    return shuffle(indexes);
	}

	// http://stackoverflow.com/questions/962802#962890
	function shuffle(array) {
	    var tmp, current, top = array.length;

	    if (top) {
	      while (--top) {
	        current = Math.floor(Math.random() * (top + 1));
	        tmp = array[current];
	        array[current] = array[top];
	        array[top] = tmp;
	      }
	    }

	    return array;
	}
	
	function updateSlides() {
		  currIndex--;
		  for (var i = 0, l = currIndex; i < l; i++) {
		      slides[i].id = i;
		  }
		  $scope.total=currIndex;
	 }
	  
	  
	$scope.getAlbuns = function () {
    	
		albumService.getAlbuns()
	      .then(
		           function(response) {
		        	   $scope.albuns=response.data.albuns;
		        	   //Reset Form
		        	   $scope.contact = {};
		        	},
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
	  }
	
	  $scope.searchImagesAlbum = function () {
    	
		  var album_id = $scope.album_id;
		  //Reinicio variaveis
	      slides = $scope.slides = [];
	      currIndex = 0;
	      $scope.total=0;
	    	
		  imageService.searchImagesAlbum(album_id)
	      .then(
		           function(response) {
		        	   var images=response.data.images;
                    	$scope.total=images.length;
                    	
                    	if ($scope.total==0){
                    		$scope.warning=response.data.message;
                    	}
                    	
                    	for (var i = 0, l = images.length; i < l; i++) {
                    			$scope.addSlide(images[i]);
                	    }
		        	},
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
	  }

	  $scope.deleteImage = function (imageId, index) {
		  	
			imageService.deleteImage(imageId)
		      .then(
			           function(response) {
			        	   $scope.slides.splice(index,1);
	                       $scope.message=response.data.message;
	                       $timeout(function () { $scope.message=""; }, 2500);
	                       updateSlides();
			           },
			            function(errResponse){
			        	   $scope.error = errResponse.statusText;
			        	}
		           )
	  }
});

//Please note that $uibModalInstance represents a modal window (instance) dependency.
//It is not the same as the $uibModal service used above.
app.controller('ModalImageCtrl', function ($scope, $uibModalInstance, $uibModal , $http , imageService, param) {
	
	$scope.submit = function () {
	  
		var index=param.index;
    	var image=angular.toJson($scope.image);
		var albumid=$scope.album_id;
		
		imageService.updateImage(image,albumid)
	      .then(
		           function(response) {
		        	   $scope.slides[index].text = $scope.image.description;
		        	   $scope.slides[index].active = $scope.image.active;
		        	   $scope.message=response.data.message;
		 		   },
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
	  
	  
	};

	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
});


app.controller('ModalMessageCtrl', function ($scope, $uibModalInstance, $uibModal , $http , messageService, param) {
	
	var imageid = param.imageid;
	
	$scope.submit = function () {
	 
		var message=angular.toJson($scope.message);
		
		messageService.sendMessage(message,imageid)
	      .then(
		           function(response) {
		        	  $scope.comment=response.data.message;
		        	  //Reset Form
		        	  $scope.message = {};
		 		   },
		            function(errResponse){
		        	   $scope.error = errResponse.statusText;
		        	}
	           )
  	};

	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
});