package com.citi.paymenthub.kafka.service;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.paymenthub.kafka.costant.ConstantUtils;
import com.citi.paymenthub.kafka.model.TopicMessagesMaster;

/**
 * Error Message Master Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Service
public class PaymentTopicMessageService {

	private static Logger logger = LogManager.getLogger(DynamicQueryService.class);

	@Autowired
	private DynamicQueryService dynamicQueryService;

	/**
	 * Method fetches payment messages from mongoDB according to filter.
	 * @param dataStr
	 * @return It returns records and page details.
	 */
	public Map<String, Object> fetchPaymentDataOnFilter(String dataStr) {

		TopicMessagesMaster topicMessagesMaster = new TopicMessagesMaster();
		Map<String, Object> topicMessagesPage = null;
		try {
			JSONObject jObject = new JSONObject(dataStr);
			Object topicName = jObject.getString(ConstantUtils.TOPICNAME);
			Object uter = jObject.getString(ConstantUtils.MESSAGEUTER);
			Object msgData = jObject.getString(ConstantUtils.MSGDATA);
			Object msgTimestamp = jObject.getString(ConstantUtils.MSGTIMESTAMP);
			Integer pageNumber = Integer.parseInt(jObject.getString(ConstantUtils.PAYPAGENUMBER));
			Integer pageSize = Integer.parseInt(jObject.getString(ConstantUtils.PAGESIZE));
			topicMessagesMaster.setTopicName((String) topicName);
			topicMessagesMaster.setUter((String) uter);
			topicMessagesMaster.setMsgData((String) msgData);
			topicMessagesMaster.setMsgTimestamp(msgTimestamp.equals("") ? 0l : Long.valueOf(msgTimestamp.toString()));
			topicMessagesPage = dynamicQueryService.paymentMessagesFilterQuery(topicMessagesMaster, pageNumber,
					pageSize);
		} catch (Exception e) {
			logger.error(e);
		}
		return topicMessagesPage;
	}

}
