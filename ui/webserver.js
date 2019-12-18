var express = require('express');
var app = express();
var request = require('request');
var host = process.argv[2];

// Define static routes
app.use('/assets', express.static('src/assets'));
app.use('/js', express.static('src/js'));
app.use('/html', express.static('src/html'));
app.use('/bootstrap', express.static('node_modules/bootstrap/dist'));
app.use('/angular', express.static('node_modules/angular'));

app.use(express.static(__dirname + '/html'));

app.get('/', function(req, res) {
    res.sendFile(__dirname + '/src/html/index.html');
});

app.get('/calculate', function(req, res, body) {
  console.log(req.query.league_name)
  var newurl = 'http://' + host + ':8100/calculate?league_name=' + req.query.league_name;
  request(newurl).pipe(res);
});

var server = require('http').createServer(app); 

var port = 3001;
console.log("ARGS", process.argv)
console.log("Listening on " + port);
server.listen(port);


