const kafka = require('kafka-node');
const Consumer = kafka.Consumer;
// const client = new kafka.Client("10.0.7.12:2181");
const client = new kafka.Client("10.48.98.232:2181");
const offset = new kafka.Offset(client);

// Setup http server
const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);
server.listen(8000);

let sockets = [];

app.get('/', (req, res) => {
    res.sendFile('index.html', { root: '../client/' });
});

app.get('/linear_regression', (req, res) => {
    res.sendFile('linear_regression.html', { root: '../client/' });
});

io.on('connection', (socket) => {
    sockets.push(socket);
});

const topicName = 'ram-usage-data';
const partition = 0;

offset.fetchLatestOffsets([ topicName ], function (error, offsets) {
    if (error)
        return handleError(error);

    let latestOffset = offsets[topicName][partition];

    var consumer = new Consumer(
        client,
        [
            { topic: topicName, partition: 0, offset: latestOffset }
        ],
        {
            autoCommit: false,
            //fromOffset: true
        }
    );

    consumer.on('message', function (message) {
        for (var socket of sockets) {
            socket.emit('data', message.value);
        }
    });

    consumer.on('error', function (err) {
        console.error(err);
    });
});