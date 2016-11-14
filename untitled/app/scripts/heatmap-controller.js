/**
 * Created by 100517944 on 11/13/16.
 */
(function () {
    'use strict';
    angular
        .module('myApp.mapper', ['ngRoute', 'ngMap'])
        .controller('HeatMapController', heatMapController);

    function heatMapController($scope, $http, NgMap, $timeout) {
        var currentTime = new Date();
        $scope.electionYear = 1900 + currentTime.getYear();
        $scope.data = {};
        $scope.shapes = [];
        $scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=AIzaSyAhs2Msocps3-q2lslapwxLrgMYdofeVs0";

        NgMap.getMap().then(function (map) {
            console.log(map.getCenter());
            console.log('markers', map.markers);
            console.log('shapes', map.shapes);
        });

        $http({
            method: "POST",
            url: "http://localhost:3000/boundary",
            data: $scope.data,
            headers: {'Content-Type': 'application/json'}
        })
            .success(function (response) {
                $scope.data = response;
                console.log(response);
            })
            .error(function (response, status) {
                console.log(response, status);
            });

        $scope.$watch('data', function (newVal, oldVal) {
            if($scope.data.objects){
                for(var district in $scope.data.objects){
                    //console.log("test", $scope.data.objects[district]);
                    var districtId = {data: $scope.data.objects[district].external_id};
                    console.log("Sending:", districtId);
                    $http.post('http://localhost:3000/coords', districtId)
                        .success(function (response) {
                            //console.log("GOT:", response);
                            $scope.shapes.push(response);
                        })
                        .error(function (response, status) {
                            console.log(response, status);
                        });
                }
            }
            console.log($scope.shapes);
        })
    }
})();