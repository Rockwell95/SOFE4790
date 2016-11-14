var express = require('express');
var represent = require('represent');
var router = express.Router();

/*POST Coords Data*/
router.post('/', function (req, res, next) {
    var data = res;
    //console.log(req.body);
    represent.boundaries("federal-electoral-districts/" + req.body.data + "/simple_shape", function(error, data) {
        represent.boundaries("federal-electoral-districts/" + req.body.data + "/representatives", function (error2, data2) {
            if (data2.objects[0]) {
                data.party = data2.objects[0].party_name;
            }
            res.send(data);
        })
    });
});

module.exports = router;
