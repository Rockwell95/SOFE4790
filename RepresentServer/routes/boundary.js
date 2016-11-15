var express = require('express');
var represent = require('represent');
var router = express.Router();

/*POST Boundary Data*/
router.post('/', function (req, res, next) {
    var edata = [];
    var ELECTORAL_DISTRICTS = 318;
    represent.boundaries("federal-electoral-districts/?limit=400&offset=20", function(error, data) {
        console.log("Got federal districts");
        for(var district in data.objects){
            //console.log("test", $scope.data.objects[district]);
            var districtId = {data: data.objects[district].external_id};
            // console.log("Sending:", districtId);
            requestBlock(districtId.data);
        }
    });

    function requestBlock(id){
        represent.boundaries("federal-electoral-districts/" + id + "/simple_shape", function(error2, data2) {
            console.log("Got shapes");
            represent.boundaries("federal-electoral-districts/" + id + "/representatives", function (error3, data3) {
                console.log("Got parties");
                if (data3.objects[0]) {
                    console.log(data3.objects[0].party_name);
                    data2.party = data3.objects[0].party_name;
                }
                callback(data2);
            })
        });
    }

    function callback(data){
        edata.push(data);
        if(edata.length === ELECTORAL_DISTRICTS){
            console.log("Done:",edata.length);
            res.send(edata);
        }
    }
});

module.exports = router;
