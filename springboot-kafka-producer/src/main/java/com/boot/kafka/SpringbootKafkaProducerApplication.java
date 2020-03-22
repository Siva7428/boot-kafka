package com.boot.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SpringbootKafkaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootKafkaProducerApplication.class, args);
	}

	@Autowired
	private KafkaProperties kafkaProperties;

	// Producer configuration

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	// Consumer Configuration
	@Bean
	public ConsumerFactory<String, String> stringConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(),
				new StringDeserializer());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(stringConsumerFactory());

		return factory;
	}

}
