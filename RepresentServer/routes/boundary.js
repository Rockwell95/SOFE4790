var express = require('express');
var represent = require('represent');
var router = express.Router();

/*POST Boundary Data*/
router.post('/', function (req, res, next) {
    var edata;
    represent.boundaries("federal-electoral-districts/?limit=400&offset=20", function(error, data) {
        edata = data;
        res.send(edata);
    });
});

module.exports = router;
