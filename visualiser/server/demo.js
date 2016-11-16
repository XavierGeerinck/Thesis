// Setup http server
const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);
server.listen(5001);

let sockets = {};

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

setInterval(() => {
	console.log(`sending to ${Object.keys(sockets).length} sockets`);
	for (var socket in sockets) {
		sockets[socket].emit('data', {
			type: 'MODEL',
			data: {
				machine_name: 'ba27f8f02',
				container_name: 'cadvisor.0.elawk0axd64n1f3f4tijlnf1e',
				model_intercept: 0.0,
				model_slope: 0.010572855809731304,
				model_slope_err: 8.437805751404645E-5,
				x: 1478078397,
				y: 17448960
			}
		});

		sockets[socket].emit('data', {
			type: 'STATS',
			data: {
				machine_name: 'ba27f8f02',
				container_name: 'kafka.1.e2yxidp40zaiqsozyot11w1l3',
				model_intercept: 0.0,
				model_slope: 0.010572855809731304,
				model_slope_err: 8.437805751404645E-5,
				x: 1478078397,
				y: 17448960
			}
		});
	}
}, 5000);
