var express = require('express');
var represent = require('represent');
var request = require('request');
var router = express.Router();

/*POST Riding Data*/
router.post('/', function (req, res, next) {
    var coords = req.body;
    var localData;
    var localPop;
    var replyPayload;
    //console.log(req.body);

    represent.boundaries("federal-electoral-districts/?contains=" +  coords.lat + "," + coords.lng, function(error, data) {
        if(error){
            console.log(error)
        }
        else{
            console.log("Got external id");
            localData = JSON.parse(JSON.stringify(data.objects[0]));
            console.log(localData.url);
            setPopulation(localData.url);
        }
    });

    function setPartyAndMP(url) {
        var subUrl = url.replace('/boundaries/', '') + "representatives";
        //console.log(subUrl);
        represent.boundaries(subUrl, function (error, data) {
            if(error){
                console.error(error);
                sendReply({name: localData.name, mp: "N/A", party: "Vacant", population: localPop});
            }
            else{
                localReps = JSON.parse(JSON.stringify(data.objects[0]));
                replyPayload = {
                    name: localData.name,
                    mp: data.objects[0].name,
                    party: data.objects[0].party_name,
                    population: localPop
                };
                //console.log(replyPayload);
                sendReply(replyPayload)
            }
        })
    }

    function setPopulation(url) {
        var subUrl = url.replace('/boundaries/', '');
        represent.boundaries(subUrl, function (error, data) {
            localPop = data.metadata.DECPOPCNT;
            setPartyAndMP(subUrl);
        });
    }

    function sendReply(replyObj) {
        console.log("Replying with:", replyObj);
        res.send(replyObj);
    }
});

module.exports = router;
