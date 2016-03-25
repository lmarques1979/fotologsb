'use strict';

var rootUrl = '/fotolog'

var app=angular.module('fotolog', ['ngAnimate',
                                   'ngRoute', 
                                   'ngResource', 
                                   'ui.bootstrap',
                                   'ngSanitize',
                                   'ngTouch',
                                   'inform',
                                   'inform-exception',
                                   'inform-http-exception',
                                   'blockUI',
                                   'ngFileUpload'])
	.config(function($provide, $httpProvider, $locationProvider, blockUIConfig) {
		$provide.value('rootUrl', rootUrl);
		$httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
		
		// Change the default overlay message
		//blockUIConfig.message = '';
        // Change the default delay to 100ms before the blocking is visible
		//blockUIConfig.delay = 50;

}).run(function($rootScope){
	$rootScope.rootUrl = rootUrl;
});

app.directive('ngConfirmClick', function(){
             return {
                 priority: 1,
                 terminal: true,
                 link: function (scope, element, attr) {
                     var msg = attr.ngConfirmClick || "Are you sure?";
                     var clickAction = attr.ngClick;
                     element.bind('click',function (event) {
                         if ( window.confirm(msg) ) {
                             scope.$eval(clickAction)
                         }
                     });
                 }
             };
});
