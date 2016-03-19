'use strict';
 
app.factory('emailService', function($http, $q){
 
	var svc = {
			sendContact: sendContact
			};
	
	function sendContact(url, $scope) {
    			    return $http({
                    		method: 'POST',
                            url: url ,
                            params: $scope.contact
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