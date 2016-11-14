/**
 * Created by 100517944 on 11/13/16.
 */
'use strict';
define(['app','represent'], function (app) {
    app.controller('HeatMapCtrl', heatMapController);

    function heatMapController($scope) {
        var currentTime = new Date();
        $scope.electionYear = 1900 + currentTime.getYear();

        represent.boundarySets("federal-electoral-districts", function (error, data) {
            console.log(data);
        })
    }
});