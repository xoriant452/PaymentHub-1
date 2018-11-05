package com.citi.paymenthub.kafka.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import com.citi.paymenthub.kafka.costant.ConstantUtils;
/**
 * Kafka Producer Config
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Component
public class KafkaProducerConfig {

	private static Properties props = new Properties();
/**
 * This static block reads all configuration properties while starting the server.
 * This will happens only once in application.
 */
	static {
		InputStream is = null;
		try {
			props = new Properties();
			is = ClassLoader.class.getResourceAsStream(ConstantUtils.CONFIG_FILE_PATH);
			props.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,getPropertyValue(ConstantUtils.BOOTSTRAP_SERVERS_CONFING));
		props.put(ProducerConfig.CLIENT_ID_CONFIG, getPropertyValue(ConstantUtils.GROUP_ID));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,2147483647);
	}

	/**
	 * This method creates the Producer instance. 
	 * @return KafkaProducer<Long, String>
	 */
	
	public static Producer<Long, String> getProducer() {
		return new KafkaProducer<>(props);
	}
/**
 *This method returns the value of key accordingly. 
 * @param key
 * @return String
 */
	private static String getPropertyValue(String key) {
		return props.getProperty(key);
	}

}
