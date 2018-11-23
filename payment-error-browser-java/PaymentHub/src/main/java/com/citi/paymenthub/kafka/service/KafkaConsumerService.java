package com.citi.paymenthub.kafka.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.citi.paymenthub.kafka.common.CommonUtils;
import com.citi.paymenthub.kafka.config.KafkaConsumerConfig;
import com.citi.paymenthub.kafka.costant.ConstantUtils;
import com.citi.paymenthub.kafka.model.ConsumedTopicMessage;
import com.citi.paymenthub.kafka.model.ErrorMessageTrans;
import com.citi.paymenthub.kafka.model.TopicMessagesMaster;

/**
 * Kafka Consumer Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@EnableScheduling
@Component
public class KafkaConsumerService {

	private static final Logger logger = LogManager.getLogger(KafkaConsumerService.class);
	private static final Integer MESSAGESIZE = 180;

	@Autowired
	private KafkaConsumerConfig consumerConfig;

	@Autowired
	private ErrorMessageMasterService errorMessageMasterService;

	@Autowired
	private ErrorMessageTransService errorMessageTransService;

	@Autowired
	private TopicMasterService topicMasterService;

	private Consumer<String, String> consumer;

	/**
	 * This method will initialize the consumer
	 */
	@PostConstruct
	public void initConsumer() {
		consumer = consumerConfig.createConsumer();
		List<String> topicList = topicMasterService.getAllTopics();
		consumer.subscribe(topicList);
	}

	/**
	 * This method will run periodically every 30 seconds and will consume all the
	 * messages
	 * @throws JSONException 
	 */
	@Scheduled(fixedRate = 1000)
	public void receiveMessage() throws JSONException {
		int count = 0;
		List<TopicMessagesMaster> list = new ArrayList<TopicMessagesMaster>();
		ConsumerRecords<String, String> records = consumer.poll(100);
		logger.info("received records " + records.count());
		for (ConsumerRecord<String, String> record : records) {
			logger.info("Recoed [{}] [{}] offser [{}]", record.key(), record.value(), record.offset());
			// logger.debug("Received Message topic ={}, partition ={}, offset = {}, key =
			// {}, value = {}\n", record.topic(), record.partition(), record.offset(),
			// record.key(), record.value());

			/*TopicMessagesMaster topicMessagesMaster = new TopicMessagesMaster();
			topicMessagesMaster.setMsgId(count);
			String message = record.value();
			topicMessagesMaster
					.setMsgData(message.length() > MESSAGESIZE ? message.substring(0, MESSAGESIZE) : message);
			topicMessagesMaster.setUter(getUTER(message));
			topicMessagesMaster.setOffsetId(record.offset());
			topicMessagesMaster.setTopicName(record.topic());
			topicMessagesMaster.setMsgTimestamp(record.timestamp());
			list.add(topicMessagesMaster);
			if (list.size() == 50) {
				errorMessageMasterService.saveMessages(list);
				list.clear();
			}*/
		}
		if (!CollectionUtils.isEmpty(list)) {
			errorMessageMasterService.saveMessages(list);
		}
		consumer.commitAsync();
	}

	/**
	 * This method parse the JSON string and fetch the UTER.
	 * 
	 * @param message
	 * @return UTER
	 * @throws JSONException 
	 */
	private String getUTER(String message) throws JSONException {
		JSONObject jObject = new JSONObject(message);
		return (String) jObject.get(ConstantUtils.UTER);
	}

	/**
	 * This method poll the messages from kafka server.
	 * 
	 * @param fromOffset
	 * @param pageNumber
	 * @param pageSize
	 * @return It returns the polled messages and page details.
	 * @throws Exception
	 */
	public Map<String, Object> runConsumerOnTimestamp(Long fromOffset, Integer pageNumber, Integer pageSize) {
		Map<String, Object> recordsMap = new HashMap<String, Object>();
		Map<String, Object> pagination = KafkaConsumerConfig.getSubscribeTopicDatewisePagination(fromOffset, pageNumber,
				pageSize);
		@SuppressWarnings("unchecked")
		final Consumer<Long, String> consumerInstance = (Consumer<Long, String>) pagination.get(ConstantUtils.CONSUMER);
		final List<String> uterList = errorMessageTransService.transactionAndHistoryUterList();
		try {
			List<ConsumedTopicMessage> list = new ArrayList<ConsumedTopicMessage>();
			int count = 0;
			int actualPageSize = pageSize;
			while (count < actualPageSize) {
				boolean recordLimit = true;
				final ConsumerRecords<Long, String> consumerRecords = consumerInstance.poll(100);
				Integer totalPages = (Integer) pagination.get(ConstantUtils.TOTALPAGES);
				if (null != totalPages && totalPages != 0 && pageNumber <= totalPages) {
					if (count == 0 && pageNumber == totalPages) {
						Long totalCount = (Long) pagination.get(ConstantUtils.TOTALELEMENTS);
						int pageRecordCount = (int) (totalCount % pageSize);
						pageRecordCount = pageRecordCount == 0 ? pageSize : pageRecordCount + 1;
						actualPageSize = pageRecordCount < pageSize ? pageRecordCount : pageSize;
					}
					for (ConsumerRecord<Long, String> record : consumerRecords) {
						ConsumedTopicMessage ctm = (ConsumedTopicMessage) CommonUtils
								.getInstance(ConsumedTopicMessage.class);
						ctm.setMessageKey(record.key());
						String message = record.value();
						ctm.setMessage(message.length() > MESSAGESIZE ? message.substring(0, MESSAGESIZE) : message);
						ctm.setOffset(record.offset());
						String uterStr = getUTER(message);
						ctm.setUter(uterStr);
						ctm.setPartitionNo(record.partition());
						ctm.setTimeStamp(record.timestamp());
						ctm.setIsProcessed(uterList.contains(uterStr) ? true : false);
						list.add(ctm);
						if (list.size() == actualPageSize) {
							recordLimit = false;
							break;
						}
					}
					consumerInstance.commitAsync();
					if (!recordLimit) {
						break;
					}
					count++;
				} else {
					actualPageSize = 0;
				}
			}
			Collections.sort(list);
			recordsMap.put(ConstantUtils.CONTENT, list);
			recordsMap.put(ConstantUtils.PAGESIZE, pagination.get(ConstantUtils.PAGESIZE));
			recordsMap.put(ConstantUtils.PAGENUMBER, pagination.get(ConstantUtils.PAGENUMBER));
			recordsMap.put(ConstantUtils.TOTALELEMENTS, pagination.get(ConstantUtils.TOTALELEMENTS));
			recordsMap.put(ConstantUtils.TOTALPAGES, pagination.get(ConstantUtils.TOTALPAGES));
		} catch (Exception e) {
			logger.error(e);
		} finally {
			consumerInstance.close();
		}
		return recordsMap;
	}

	/**
	 * This method poll the single offset from kafka server.
	 * 
	 * @param topic
	 * @param offsetId
	 * @return
	 * @throws InterruptedException
	 */
	public ErrorMessageTrans getRecordByOffset(String topic, String offsetId) throws InterruptedException {
		ErrorMessageTrans errorMessageTrans = null;
		Consumer<Long, String> consumerInstance = null;
		try {
			consumerInstance = KafkaConsumerConfig.getSubscribeTopic(topic, offsetId);
			errorMessageTrans = (ErrorMessageTrans) CommonUtils.getInstance(ErrorMessageTrans.class);
			final ConsumerRecords<Long, String> consumerRecords = consumerInstance.poll(100);
			for (ConsumerRecord<Long, String> record : consumerRecords) {
				errorMessageTrans.setOffsetId(record.offset());
				errorMessageTrans.setErrorMsgData(record.value());
				errorMessageTrans.setUter(getUTER(errorMessageTrans.getWholeErrorMsgData(true)));
				errorMessageTrans.setRequestedBy(ConstantUtils.SYSTEM_ADMIN_MK);
				errorMessageTrans.setStatus(ConstantUtils.STATUS_P);
				errorMessageTrans.setRequestedDate(LocalDate.now().toString());
				errorMessageTrans.setTopicName(record.topic());
				break;
			}
			consumerInstance.commitAsync();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			consumerInstance.close();
		}
		return errorMessageTrans;
	}

	/**
	 * Method returns the message of requested offset.
	 * @param topic
	 * @param offsetId
	 * @return message
	 */
	public Map<String, Object> getMessageByOffset(String topic, String offsetId) {
		final Consumer<Long, String> consumerInstance = KafkaConsumerConfig.getSubscribeTopic(topic, offsetId);
		Map<String, Object> consumeMap = new HashMap<String, Object>();
		final ConsumerRecords<Long, String> consumerRecords = consumerInstance.poll(100);
		for (ConsumerRecord<Long, String> record : consumerRecords) {
			consumeMap.put(ConstantUtils.MESSAGE, record.value());
			break;
		}
		consumerInstance.close();
		return consumeMap;
	}

}
