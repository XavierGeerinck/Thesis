var shaman = require('shaman');
let plotly = require('plotly')('XavierGeerinck', 'ehfldmdtn5');
var kafka = require('kafka-node'),
	Consumer = kafka.Consumer,
	// client = new kafka.Client("10.48.98.232:2181");
	client = new kafka.Client("10.0.7.12:2181");

var offset = new kafka.Offset(client);

offset.fetch([{ topic: 'container_stats', partition: 0, time: -1 }], function (err, data) {
	var latestOffset = data['container_stats']['0'][0];
	console.log("Consumer current offset: " + latestOffset);
});

const numberOfMessages = 1000; // Number of messages to plot
const partition = 0;
const topic = "container_stats";
offset.fetchLatestOffsets([ topic ], function (error, offsets) {
	if (error)
		return handleError(error);

	let latestOffset = offsets[topic][partition];


	var consumer = new Consumer(
		client,
		[
			{ topic: 'container_stats', partition: 0, offset: latestOffset }
		],
		{
			autoCommit: false,
			//fromOffset: true
		}
	);

	let container = {};
	let container_y = {};
	let done = false;

	consumer.on('message', function (message) {
		console.log(message.value);
// 		let parsed = JSON.parse(message.value);
//
// 		container[parsed.machine_name] = container[parsed.machine_name] || {};
// 		container[parsed.machine_name][parsed.container_Name] = container[parsed.machine_name][parsed.container_Name] || {
// 				x: [],
// 				y: []
// 			};
//
// 			console.log(container[parsed.machine_name][parsed.container_Name]['x'].length);
// 		if (container[parsed.machine_name][parsed.container_Name]['x'].length < numberOfMessages) {
// 			container[parsed.machine_name][parsed.container_Name]['x'].push(Date.parse(parsed.timestamp));
// 			container[parsed.machine_name][parsed.container_Name]['y'].push(parsed.container_stats.memory.usage);
// 		}
//
// 		if (container[parsed.machine_name][parsed.container_Name]['x'].length == numberOfMessages && parsed.container_Name == 'kafka.1.d13y4q7ol39r7vk4qftotpsre' && !done) {
// 			done = true;
// 			// console.log('TRAINING');
// 			// console.log(container[parsed.machine_name][parsed.container_Name]['x'].length);
// 			// console.log(container[parsed.machine_name][parsed.container_Name]['x']);
// 			// console.log(container[parsed.machine_name][parsed.container_Name]['y'].length);
// 			// console.log(container[parsed.machine_name][parsed.container_Name]['y']);
// 			var lr = new shaman.LinearRegression(container[parsed.machine_name][parsed.container_Name]['x'],
// 				container[parsed.machine_name][parsed.container_Name]['y'], {
// 					// algorithm: 'GradientDescent',
// 					// numberOfIterations: 1000, // defaults to 8500
// 					// learningRate: 0.5, // defaults to 0.1,
// 					// saveCosts: true // defaults to false
// 				});
// 			lr.train(function(err) {
// 				if (err) { throw err; }
//
// 				// you can now start using lr.predict:
// 				console.log(container[parsed.machine_name][parsed.container_Name]['x']);
// 				console.log(container[parsed.machine_name][parsed.container_Name]['y']);
// 				console.log('Machine: ' + parsed.machine_name + ' container: ' + parsed.container_Name + ' : ' + lr.predict(new Date().getTime()) / 1024 / 1024 / 1024);
//
// 				let y2 = [];
// 				container[parsed.machine_name][parsed.container_Name]['x'].forEach((x) => {
// 					y2.push(lr.predict(x));
// 				});
//
// 				// PLOTLY
// 				var layout = {
// 					title: 'Time vs RAM',
// 					xaxis: {
// 						title: 'Time'
// 					},
// 					yaxis: {
// 						title: 'RAM'
// 					}
// 				};
//
// 				var trace1 = {
// 					x: container[parsed.machine_name][parsed.container_Name]['x'],
// 					y: container[parsed.machine_name][parsed.container_Name]['y'],
// 					name: 'Training Data',
// 					mode: "markers",
// 					type: "scatter"
// 				};
//
// 				var trace2 = {
// 					x: container[parsed.machine_name][parsed.container_Name]['x'],
// 					y: y2,
// 					name: 'Linear Regression',
// 					mode: "lines",
// 					type: "scatter"
// 				};
//
// 				// Push prediction data now! of what happens in 3 secs
// 				trace2.x.push(trace2.x[trace2.x.length - 1] + 3000);
// 				trace2.y.push(lr.predict(trace2.x[trace2.x.length - 1] + 3000));
//
//
// 				// Push prediction data now! of what happens in 60 secs
// 				trace2.x.push(trace2.x[trace2.x.length - 1] + 60000);
// 				trace2.y.push(lr.predict(trace2.x[trace2.x.length - 1] + 60000));
//
//
// 				// Push prediction data now! of what happens in 5 minutes
// 				trace2.x.push(trace2.x[trace2.x.length - 1] + (5 * 60 * 1000));
// 				trace2.y.push(lr.predict(trace2.x[trace2.x.length - 1] + (5 * 60 * 1000)));
//
//
// 				// Push prediction data now! of what happens in 10 minutes
// 				trace2.x.push(trace2.x[trace2.x.length - 1] + (10 * 60 * 1000));
// 				trace2.y.push(lr.predict(trace2.x[trace2.x.length - 1] + (10 * 60 * 1000)));
//
// 				// Push prediction data now! of what happens in 60 minutes
// 				trace2.x.push(trace2.x[trace2.x.length - 1] + (60 * 60 * 1000));
// 				trace2.y.push(lr.predict(trace2.x[trace2.x.length - 1] + (60 * 60 * 1000)));
//
// 				var plotData = [trace1, trace2];
// 				var graphOptions = {layout: layout,filename: "cars-linear-regression-with-shaman", fileopt: "overwrite"}
// 				plotly.plot(plotData, graphOptions, function (err, msg) {
// 					if (err) {
// 						console.log(err);
// 						process.exit(3);
// 					} else {
// 						console.log('Success! The plot (' + msg.filename + ') can be found at ' + msg.url);
// 						process.exit();
// 					}
// 				});
// 			});
// 		}
//
// 		//console.log(parsed.machine_name + ': ' + parsed.container_Name + ' : ' + container[parsed.machine_name][parsed.container_Name]['x']);
// 		//container_x.push(Date.parse(parsed.timestamp));
// 		//container_y.push(parsed.container_stats.memory_usage);
//
// //console.log(message.value);
// 		//console.log('X: ' + Date.parse(parsed.timestamp) + ' Y: ' + parsed.container_stats.memory.usage);
	});

	consumer.on('error', function (err) {
		console.error(err);
	});
});

//
//
// // var X = [1, 2, 3, 4, 5];
// // var Y = [2, 2, 3, 3, 5];
// // var lr = new LinearRegression(X,Y);
// // lr.train(function(err) {
// // 	if (err) { throw err; }
// //
// // 	// you can now start using lr.predict:
// // 	console.log(lr.predict(1));
// // });