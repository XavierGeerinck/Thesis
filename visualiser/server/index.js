const kafka = require('kafka-node');
const Consumer = kafka.Consumer;
// const client = new kafka.Client("10.0.7.12:2181");
const client = new kafka.Client("10.48.98.232:2181");
const offset = new kafka.Offset(client);

// Setup http server
const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);
server.listen(5001);

let sockets = [];

app.get('/', (req, res) => {
    res.sendFile('index.html', { root: '../client/' });
});

app.get('/linear_regression', (req, res) => {
    res.sendFile('linear_regression.html', { root: '../client/' });
});

io.on('connection', (socket) => {
    console.log(`Client ${socket.id} connected`);

    sockets[socket.id] = socket;

    // socket.on('event', (data) => {
    // 	// Not implemented
    // });

    // On disconnect, remove it
    socket.on('disconnect', () => {
        console.log(`Client ${socket.id} disconnected, removing it...`);
        delete sockets[socket.id];
    });
});

const topicName = 'models-bundled';
const partition = 0;
offset.fetchLatestOffsets([ 'container_stats', 'models-bundled' ], function (error, offsets) {
    if (error)
        return handleError(error);

    let latestOffset = offsets[topicName][partition];

    var consumer = new Consumer(
        client,
        [
            { topic: 'models-bundled', partition: 0, offset: offsets['models-bundled'][0] },
            { topic: 'container_stats', partition: 0, offset: offsets['container_stats'][0] }
        ],
        {
            autoCommit: false,
            fromOffset: true
        }
    );

    consumer.on('message', function (message) {
        console.log(`sending to ${Object.keys(sockets).length} sockets`);

        switch (message.topic) {
            case 'models-bundled':
                for (var socket in sockets) {
                    sockets[socket].emit('data', {
                        type: 'MODEL',
                        data: JSON.parse(message.value)
                    });
                }
                break;
            case 'container_stats':
                for (var socket in sockets) {
                    sockets[socket].emit('data', {
                        type: 'STATS',
                        data: JSON.parse(message.value)
                    });
                }
                break;
            default:
                // Not implemented
        }
    });

    consumer.on('error', function (err) {
        console.error(err);
    });
});