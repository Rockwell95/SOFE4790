/**
 * Created by 100517944 on 11/13/16.
 */
(function () {
    'use strict';
    angular
        .module('myApp.mapper', ['ngRoute', 'ngMap','ngSanitize'])
        .controller('HeatMapController', heatMapController);

    function heatMapController($scope, $http, NgMap, $timeout, $window) {
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
        console.log("Downloading data, please wait");
        if(!$window.localStorage.getItem("electionData")){
            $http.post("http://localhost:3000/boundary")
                .success(function (response) {
                    $scope.data = response;
                    console.log(response);
                })
                .error(function (response, status) {
                    console.log(response, status);
                });
        }
        else{
            $scope.data = JSON.parse($window.localStorage.getItem("electionData"));
        }

        $scope.$watch('data', function (newVal, oldVal) {
            console.log("Got data with length:", $scope.data.length);
            if($scope.data.length === 318){
                $window.localStorage.setItem("electionData", JSON.stringify($scope.data));
                drawDistricts($scope.data)
            }
        });


        function drawDistricts(data){
            $scope.mapRender = [];
            var color;
            //console.log("Data:", data);
            for(var region in data){
                if(data[region].party === "Conservative"){
                    color = '#0000FF';
                }
                else if(data[region].party === "Liberal"){
                    color = '#FF0000';
                }
                else if(data[region].party === "NDP"){
                    color = '#FFA500';
                }
                else if(data[region].party === "Bloc Québécois"){
                    color = "#00FFFF";
                }
                else if(data[region].party === "Independent"){
                    color = "#C0C0C0";
                }
                else if(data[region].party === "Green Party"){
                    color = "#00FF00";
                }
                else{
                    color = '#FF00FF';
                }
                $scope.mapRender.push({
                    name: 'polygon',
                    fillColor: color,
                    paths: []
                });
                //console.log(data[region].coordinates);
                for(var coords in data[region].coordinates){

                    for (var regcoords in data[region].coordinates[coords][0]) { //$scope.mapRender += "[" +data[region].coordinates[0][0][coords][1] + "," + data[region].coordinates[0][0][coords][0] + "],"
                        $scope.mapRender[region].paths.push([data[region].coordinates[coords][0][regcoords][1], data[region].coordinates[coords][0][regcoords][0]])
                    }
                }
                //break;
            }

            console.log($scope.mapRender);
        }
    }
})();