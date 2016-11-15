(function () {
    'use strict';
    angular.module('myApp', [
        'ngRoute',
        'ngSanitize',
        'ngMap',
        'myApp.mapper'
    ]).config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');

        $routeProvider.otherwise({redirectTo: '/view1'});
    }]);
})();