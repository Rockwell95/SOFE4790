/**
 * Created by 100517944 on 11/13/16.
 */
(function () {
    'use strict';
    angular
        .module('myApp.mapper', ['ngRoute', 'ngMap','ngSanitize'])
        .controller('HeatMapController', heatMapController);

    function heatMapController($scope, $http, NgMap, $window) {
        var monthNames = [
            "January", "February", "March",
            "April", "May", "June", "July",
            "August", "September", "October",
            "November", "December"
        ];

        var currentTime = new Date();
        $scope.electionYear = monthNames[currentTime.getMonth()] + " " + currentTime.getDate() + ", " + currentTime.getFullYear();
        $scope.data = {};
        $scope.shapes = [];
        $scope.ridingInfo = {
            population: -1
        };
        $scope.render = true;
        $scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=AIzaSyAhs2Msocps3-q2lslapwxLrgMYdofeVs0";

        NgMap.getMap().then(function (map) {
            console.log(map.getCenter());
            console.log('markers', map.markers);
            console.log('shapes', map.shapes);
            $scope.map = map;
            console.log(map);
        });

        console.log("Downloading data, please wait");
        if(!$window.localStorage.getItem("electionData")){
            getData();
        }
        else{
            $scope.data = JSON.parse($window.localStorage.getItem("electionData"));
        }

        //Deletes shapes from map and forces it to re-render and re-download ridings
        $scope.refreshMap = function () {
            console.log("Refreshing map data, please wait...");
            $window.localStorage.clear();
            $scope.mapRender.length = 0;
            //force map reload
            $scope.render = false;
            $scope.render = true;
            getData();
        };

        // Sends request to server for information on riding (shape) that was clicked on. API allows geocoding lookup
        $scope.click = function(event, shape) {
            var lat = event.latLng.lat();
            var lng = event.latLng.lng();
            //console.log(lat + " " + lng);
            getRidingInfo(lat, lng, event)
        };


        $scope.$watch('data', function (newVal, oldVal) {
            console.log("Got data with length:", $scope.data.length);
            if($scope.data.length === 318){
                $window.localStorage.setItem("electionData", JSON.stringify($scope.data));
                drawDistricts($scope.data)
            }
        });

        $scope.numberWithCommas = function(x) {
            return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        };

        function getRidingInfo(lat, lng, evt) {
            $http.post("http://localhost:3000/getinfo",{lat: lat, lng: lng})
                .success(function (response) {
                    $scope.ridingInfo = response;
                    //console.log(response);
                })
                .error(function (response, status) {
                    console.log(response, status);
                });

            $scope.map.showInfoWindow('ridingInfo', evt.latLng);
        }

        function getData() {
            $http.post("http://localhost:3000/boundary")
                .success(function (response) {
                    $scope.data = response;
                    console.log(response);
                })
                .error(function (response, status) {
                    console.log(response, status);
                });
        }

        // Colors and draws districts based on ruling party. Magenta is used for other and/or vacant ridings.
        // As of the time of construction, only Liberal, Conservative, NDP, Bloc and Green parties held parliamentary seats
        function drawDistricts(data){
            $scope.mapRender = [];
            var color;
            //console.log("Data:", data);
            for(var region in data){
                if (data) {
                    if (data[region].party === "Conservative") {
                        color = '#0000FF';
                    }
                    else if (data[region].party === "Liberal") {
                        color = '#FF0000';
                    }
                    else if (data[region].party === "NDP") {
                        color = '#FFA500';
                    }
                    else if (data[region].party === "Bloc Québécois") {
                        color = "#00FFFF";
                    }
                    else if (data[region].party === "Independent") {
                        color = "#838383";
                    }
                    else if (data[region].party === "Green Party") {
                        color = "#228B22";
                    }
                    else {
                        color = '#FF00FF';
                    }
                }
                $scope.mapRender.push({
                    name: 'polygon',
                    fillColor: color,
                    paths: []
                });
                //console.log(data[region].coordinates);
                for(var coords in data[region].coordinates){

                    for (var regcoords in data[region].coordinates[coords][0]) {
                        $scope.mapRender[region].paths.push([data[region].coordinates[coords][0][regcoords][1], data[region].coordinates[coords][0][regcoords][0]])
                    }
                }
                //break;
            }

            console.log($scope.mapRender);
        }
    }
})();