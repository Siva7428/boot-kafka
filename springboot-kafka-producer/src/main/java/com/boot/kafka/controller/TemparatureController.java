package com.boot.kafka.controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.kafka.model.TemparatureModel;
import com.boot.kafka.producer.ProducerService;

@RestController
@RequestMapping("/kafka")
public class TemparatureController {

	private static final Logger logger = LoggerFactory.getLogger(TemparatureController.class);

	@Autowired
	private ProducerService producerService;

	private KafkaTemplate<String, Object> template;
	private String topicName;
	private int messagesPerRequest;
	private CountDownLatch latch;

	public TemparatureController(KafkaTemplate<String, Object> template, @Value("${tpd.topic-name}") String topicName,
			@Value("${tpd.messages-per-request}") int messagesPerRequest) {
		super();
		this.template = template;
		this.topicName = topicName;
		this.messagesPerRequest = messagesPerRequest;
	}

	@PostMapping(value = "/publish")
	public void sendMessageKafka(@RequestParam("message") String message) {
		this.producerService.sendMessage(message);
	}

	@GetMapping("/hello")
	public String messageGet() throws Exception {
		latch = new CountDownLatch(messagesPerRequest);
		IntStream.range(0, messagesPerRequest).forEach(
				i -> this.template.send(topicName, String.valueOf(i), new TemparatureModel(i, "A Practical Advice")));
		latch.await(60, TimeUnit.SECONDS);
		logger.info("All messages received");
		return "Hello Kafka!";
	}

}
