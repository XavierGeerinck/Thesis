/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dataartisans;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;

/**
 * Simple example on how to read with a Kafka consumer
 *
 * Note that the Kafka source is expecting the following parameters to be set
 *  - "bootstrap.servers" (comma separated list of kafka brokers)
 *  - "zookeeper.connect" (comma separated list of zookeeper servers)
 *  - "group.id" the id of the consumer group
 *  - "topic" the name of the topic to read data from.
 *
 * You can pass these required parameters using "--bootstrap.servers host:port,host1:port1 --zookeeper.connect host:port --topic testTopic"
 *
 * This is a valid input example:
 * 		--topic test --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup
 *
 * 	--bootstrap.servers 10.0.7.12:9092 --zookeeper.connect 10.0.7.12:2181 --topic container_stats --group.id myGroup
 * 	http://dataartisans.github.io/flink-training/exercises/toFromKafka.html
 *
 *
 */
public class ReadFromKafka {

	public static void main(String[] args) throws Exception {
		// parse input arguments
		final ParameterTool parameterTool = ParameterTool.fromArgs(args);

		if(parameterTool.getNumberOfParameters() < 4) {
			System.out.println("Missing parameters!\nUsage: Kafka --topic <topic> " +
					"--bootstrap.servers <kafka brokers> --zookeeper.connect <zk quorum> --group.id <some id>");
			return;
		}

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.getConfig().disableSysoutLogging();
		//env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
		env.enableCheckpointing(5000); // create a checkpoint every 5 secodns
		env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface

		// configure Kafka consumer
		Properties props = new Properties();
		props.setProperty("zookeeper.connect", "10.0.7.13:2181"); // Zookeeper default host:port
		props.setProperty("bootstrap.servers", "10.0.7.13:9092"); // Broker default host:port
		props.setProperty("group.id", "myGroup");                 // Consumer group ID
		props.setProperty("auto.offset.reset", "earliest");       // Always read topic from start

		DataStream<String> messageStream = env
				.addSource(new FlinkKafkaConsumer082<>(
						parameterTool.getRequired("topic"),
						new SimpleStringSchema(),
						props));

		// write kafka stream to standard out.
		messageStream.print();

		env.execute("Read from Kafka example");
	}
}
