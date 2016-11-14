'use strict';
define(['angularAMD', 'angular-route'], function (angularAMD) {

    var app = angular.module('myApp', ['ngRoute']);
    app.config(function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.when("/", angularAMD.route({
            templateUrl: '../map-main.html',
            controller: 'HeatMapCtrl',
            controllerUrl: 'heatmap-controller'
        }))
            .otherwise({redirectTo: "/"});
    });
    return angularAMD.bootstrap(app);
});
