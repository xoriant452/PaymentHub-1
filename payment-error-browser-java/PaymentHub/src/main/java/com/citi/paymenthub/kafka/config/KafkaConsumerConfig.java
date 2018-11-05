package com.citi.paymenthub.kafka.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.citi.paymenthub.kafka.common.CommonUtils;
import com.citi.paymenthub.kafka.costant.ConstantUtils;
/**
 * Kafka Consumer Config
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Configuration
public class KafkaConsumerConfig {

	private static Logger logger = LogManager.getLogger(KafkaConsumerConfig.class);
	private static Properties props = null;

	/**
	 * This static block reads all configuration properties regarding the consumer
	 * while starting the server. This will happens only once in application.
	 */

	static {
		InputStream is = null;
		try {
			props = new Properties();
			is = ClassLoader.class.getResourceAsStream(ConstantUtils.CONFIG_FILE_PATH);
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getPropertyValue(ConstantUtils.BOOTSTRAP_SERVERS_CONFING));
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, ConstantUtils.LATEST);
		props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 2147483647);
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, ConstantUtils.ISOLATIONLEVEL);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, ConstantUtils.AUTOOFFSETRESET);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ConstantUtils.AUTOCOMMIT);
	}
/**
 * Method creates the Consumer according to provided configuration for scheduler.
 * @return
 */
	@Bean
	public Consumer<Long, String> createConsumer() {
		props.put(ConsumerConfig.GROUP_ID_CONFIG, getPropertyValue(ConstantUtils.GROUP_ID));
		return new KafkaConsumer<>(props);
	}

	/**
	 * Create the object on first request and return the same object on next call.
	 * 
	 * @return Consumer instance
	 */
	public static Consumer<Long, String> getConsumerInstance() {
		props.put(ConsumerConfig.GROUP_ID_CONFIG, getPropertyValue(ConstantUtils.GROUP_ID_1));
		return new KafkaConsumer<>(props);
	}

	/**
	 * Return value according to key.
	 * 
	 * @param key
	 * @return value
	 */
	private static String getPropertyValue(String key) {
		return props.getProperty(key);
	}

	/**
	 * This method returns the Consumer instance with subscribed topic.
	 * 
	 * @param kafkaTopic
	 * @param startingOffset
	 * @return Consumer<Long, String>
	 */
	public static Consumer<Long, String> getSubscribeTopic(String kafkaTopic, String startingOffset) {
		Consumer<Long, String> consumerInstance = getConsumerInstance();
		try {
		Long offsetId = Long.valueOf(startingOffset);
		consumerInstance.subscribe(Arrays.asList(kafkaTopic), new ConsumerRebalanceListener() {
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				logger.debug("{} topic-partitions are revoked from this consumer\n"+Arrays.toString(partitions.toArray()));
			}

			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				logger.debug("{} topic-partitions are revoked from this consumer\n"+Arrays.toString(partitions.toArray()));
				Iterator<TopicPartition> topicPartitionIterator = partitions.iterator();
				while (topicPartitionIterator.hasNext()) {
					TopicPartition topicPartition = topicPartitionIterator.next();
					System.out.println("Current offset is " + consumerInstance.position(topicPartition)
							+ " committed offset is ->" + consumerInstance.committed(topicPartition));
					consumerInstance.seek(topicPartition, offsetId);
				}
			}
		});
		}catch(Exception e) {
			logger.error(e);
		}
		return consumerInstance;
	}

	/**
	 * Subscribe the topic with newly created Consumer and seek the specific offset
	 * of particular partition.
	 * 
	 * @param fromTimestamp
	 * @param pageNumber
	 * @param pageSize
	 * @return It provides the page details on the basis on time stamp ,page Number
	 *         and page size.
	 */
	public static Map<String, Object> getSubscribeTopicDatewisePagination(Long fromTimestamp, Integer pageNumber,
			Integer pageSize) {
		Map<String, Object> pageMap = new HashMap<String, Object>();
		Consumer<Long, String> consumerInstance = getConsumerInstance();
		try {
			consumerInstance.subscribe(Arrays.asList(ConstantUtils.ERRORMESSAGETOPIC), new ConsumerRebalanceListener() {
				public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
					logger.debug("{} topic-partitions are revoked from this consumer\n"+Arrays.toString(partitions.toArray()));
				}

				public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
					logger.debug("{} topic-partitions are revoked from this consumer\n"+Arrays.toString(partitions.toArray()));
					Iterator<TopicPartition> topicPartitionIterator = partitions.iterator();
					Map<TopicPartition, Long> offsetCount = consumerInstance.endOffsets(partitions);
					while (topicPartitionIterator.hasNext()) {
						Long partitionLastOffset = 0l;
						TopicPartition topicPartition = topicPartitionIterator.next();
						partitionLastOffset = offsetCount.get(topicPartition);
						Long startingOffset = getStartingOffsetOfDate(consumerInstance, ConstantUtils.ERRORMESSAGETOPIC,
								pageNumber, pageSize, pageMap, partitionLastOffset, fromTimestamp);
						consumerInstance.seek(topicPartition, startingOffset);
					}
				}
			});
		} catch (Exception e) {
			logger.error(e);
		}
		pageMap.put(ConstantUtils.CONSUMER, consumerInstance);
		return pageMap;
	}

	/**
	 * According to time stamp it calculates the first offset of the day and page details.
	 * 
	 * @param consumer
	 * @param topic
	 * @param pageNumber
	 * @param pageSize
	 * @param pageMap
	 * @param partitionLastOffset
	 * @param fromTimestamp
	 * @return It returns the first offset of day.
	 */
	private static Long getStartingOffsetOfDate(Consumer<Long, String> consumer, String topic, Integer pageNumber,
			Integer pageSize, Map<String, Object> pageMap, Long partitionLastOffset, Long fromTimestamp) {
		Map<String, Long> startEndOffseet = getStartEndOffsetsForTimes(consumer, topic, partitionLastOffset,
				fromTimestamp);
		Long startOffset = startEndOffseet.get(ConstantUtils.STARTOFFSET);
		Long lastOffset = startEndOffseet.get(ConstantUtils.LASTOFFSET);
		Long fromOffset = 0l;
		Long totalOffset = lastOffset - startOffset;
		fromOffset = ((lastOffset - (pageSize * pageNumber)));
		fromOffset = fromOffset > startOffset ? fromOffset : startOffset;

		pageMap.put(ConstantUtils.PAGESIZE, pageSize == null ? 0 : pageSize);
		pageMap.put(ConstantUtils.PAGENUMBER, pageNumber - 1);
		pageMap.put(ConstantUtils.TOTALELEMENTS, totalOffset == null ? 0 : totalOffset);
		int totalPages = (int) (totalOffset / pageSize);
		int totalNumberOfPages = totalOffset % pageSize == 0 ? totalPages : (totalPages + 1);
		pageMap.put(ConstantUtils.TOTALPAGES, totalNumberOfPages);
		return fromOffset > 0 ? fromOffset : 0;
	}

	/**
	 * According to time stamp it calculates the first and last offset of the day.
	 * @param consumer
	 * @param topic
	 * @param partitionLastOffset
	 * @param fromTimestamp
	 * @return It returns the first and last offset of the day.
	 */
	public static Map<String, Long> getStartEndOffsetsForTimes(Consumer<Long, String> consumer, String topic,
			Long partitionLastOffset, Long fromTimestamp) {
		final Map<String, Long> partitionOffsetMap = new HashMap<String, Long>();
		TopicPartition topicPartition = new TopicPartition(topic, 0);
		long ToTimestamp = fromTimestamp + 86400000;
		final Map<TopicPartition, Long> timestampMap = new HashMap<>();
		timestampMap.put(topicPartition, fromTimestamp);
		final Map<TopicPartition, OffsetAndTimestamp> offsetMap = consumer.offsetsForTimes(timestampMap);
		Set<Entry<TopicPartition, OffsetAndTimestamp>> set = offsetMap.entrySet();
		long offsetFlag = 0l;
		for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : set) {
			if (null != entry && null != entry.getValue()) {
				offsetFlag = entry.getValue().offset();
				partitionOffsetMap.put(ConstantUtils.STARTOFFSET, offsetFlag);
			} else if (!fromTimestamp.equals(CommonUtils.getCurrentDayMilisecond())) {
				partitionOffsetMap.put(ConstantUtils.STARTOFFSET, 0l);
			} else {
				offsetFlag = -1l;
				partitionOffsetMap.put(ConstantUtils.STARTOFFSET, 0l);
			}
		}
		timestampMap.clear();
		timestampMap.put(topicPartition, ToTimestamp);
		final Map<TopicPartition, OffsetAndTimestamp> offsetMap1 = consumer.offsetsForTimes(timestampMap);

		for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : offsetMap1.entrySet()) {
			if (null != entry && null != entry.getValue()) {
				partitionOffsetMap.put(ConstantUtils.LASTOFFSET, entry.getValue().offset());
			} else if (offsetFlag != -1l) {
				partitionOffsetMap.put(ConstantUtils.LASTOFFSET, partitionLastOffset);
			} else {
				partitionOffsetMap.put(ConstantUtils.LASTOFFSET, 0l);
			}
		}
		return partitionOffsetMap;
	}

}
