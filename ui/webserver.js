var express = require('express');
var app = express();

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

var server = require('http').createServer(app); 

var port = 3001;
console.log("Listening on " + port);
server.listen(port);

