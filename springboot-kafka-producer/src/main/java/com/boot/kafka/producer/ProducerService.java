package com.boot.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProducerService.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String payload) {
		LOGGER.info("sending payload='{}'", payload);
		kafkaTemplate.send("message send successful", payload);

	}
}
