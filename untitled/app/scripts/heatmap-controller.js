/**
 * Created by 100517944 on 11/13/16.
 */
(function () {
    'use strict';
    angular
        .module('myApp.mapper', ['ngRoute'])
        .controller('HeatMapController', heatMapController);

    function heatMapController($scope, $http) {
        var currentTime = new Date();
        $scope.electionYear = 1900 + currentTime.getYear();
        $scope.data = {};

        $http({
            method: "POST",
            url: "http://localhost:3000/boundary",
            data: $scope.data,
            headers: {'Content-Type':'application/json'}
        })
            .success(function (response) {
                console.log(response);
            })
            .error(function (response, status) {
                console.log(response, status);
            })


    }
})();