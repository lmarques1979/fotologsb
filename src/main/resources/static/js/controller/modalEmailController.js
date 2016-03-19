app.controller('ModalEmailController', function ($scope, $uibModal, $log , $http) {

	//reset Form
	$scope.contact = {};
	$scope.animationsEnabled = true;
	
    $scope.open = function (size) {

    var modalInstance = $uibModal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'ModalEmailContent.html',
      controller: 'ModalInstanceCtrl',
      size: size
    });

    modalInstance.result.then(function () {
      $log.info('Modal dismissed at: ' + new Date());
    });
  };
});

// Please note that $uibModalInstance represents a modal window (instance) dependency.
// It is not the same as the $uibModal service used above.
app.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, $uibModal , $http , emailService, blockUI) {

  $scope.submit = function () {
	  
	  var url = rootUrl + '/user/sendemail';
	  $scope.code = null;
	  $scope.response = null; 

	  emailService.sendContact(url, $scope)
      .then(
	           function(response) {
	        	   $scope.message = response.data.message;
	        	   //Reset Form
	        	   $scope.contact = {};
	        	},
	            function(errResponse){
	        	   $scope.error = errResponse.statusText;
	        	}
           );
	  
	};

	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
});